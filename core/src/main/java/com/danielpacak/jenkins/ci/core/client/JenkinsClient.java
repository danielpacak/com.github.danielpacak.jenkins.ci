/*
 * #%L
 * Jenkins Java API
 * %%
 * Copyright (C) 2013 Daniel Pacak
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.danielpacak.jenkins.ci.core.client;

import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.danielpacak.jenkins.ci.core.http.HttpMethod;
import com.danielpacak.jenkins.ci.core.http.client.ClientHttpRequest;
import com.danielpacak.jenkins.ci.core.http.client.ClientHttpRequestFactory;
import com.danielpacak.jenkins.ci.core.http.client.ClientHttpResponse;
import com.danielpacak.jenkins.ci.core.http.client.SimpleClientHttpRequestFactory;
import com.danielpacak.jenkins.ci.core.http.converter.BuildHttpMessageConverter;
import com.danielpacak.jenkins.ci.core.http.converter.HttpMessageConverter;
import com.danielpacak.jenkins.ci.core.http.converter.JenkinsHttpMessageConverter;
import com.danielpacak.jenkins.ci.core.http.converter.JobArrayHttpMessageConverter;
import com.danielpacak.jenkins.ci.core.http.converter.JobConfigurationHttpMessageConverter;
import com.danielpacak.jenkins.ci.core.http.converter.JobHttpMessageConverter;
import com.danielpacak.jenkins.ci.core.http.converter.PluginArrayHttpMessageConverter;
import com.danielpacak.jenkins.ci.core.util.Base64;

/**
 * Client class for interacting with the Jenkins HTTP/XML API.
 * 
 * @since 1.0.0
 */
public class JenkinsClient {

	public static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_USER_AGENT = "User-Agent";

	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

	public static final String JOB_SEGMENT = "/job";
	public static final String CREATE_ITEM_SEGMENT = "/createItem";
	public static final String DO_DELETE_SEGMENT = "/doDelete";
	public static final String SEGMENT_API_XML = "/api/xml";

	/**
	 * Default user agent request header value
	 */
	protected static final String USER_AGENT = "JenkinsJavaAPI";

	private String baseUri;

	private String userAgent = USER_AGENT;

	private String credentials;

	private ClientHttpRequestFactory httpRequestFactory;

	private List<HttpMessageConverter<?>> messageConverters = new LinkedList<HttpMessageConverter<?>>();

	public JenkinsClient() {
		this("localhost", 8080);
	}

	public JenkinsClient(String host, Integer port) {
		this("http", host, port, null);
	}

	/**
	 * Create client for scheme, host, port, and prefix. {scheme}://{host}:{port}/{prefix} http://localhost:8080/jenkins
	 * 
	 * @param hostname
	 * @param port
	 * @param scheme
	 */
	public JenkinsClient(String scheme, String host, Integer port, String prefix) {
		checkArgumentNotNull(scheme, "Scheme cannot be null");
		checkArgumentNotNull(host, "Host cannot be null");
		checkArgumentNotNull(port, "Port cannot be null");
		// @formatter:off
		StringBuilder uri = new StringBuilder()
			.append(scheme)
			.append("://")
			.append(host)
			.append(':')
			.append(port);
		// @formatter:on
		this.baseUri = prefix != null ? uri.append(prefix).toString() : uri.toString();
		this.httpRequestFactory = new SimpleClientHttpRequestFactory();
		this.messageConverters.add(new JobHttpMessageConverter());
		this.messageConverters.add(new JobArrayHttpMessageConverter());
		this.messageConverters.add(new JobConfigurationHttpMessageConverter());
		this.messageConverters.add(new JenkinsHttpMessageConverter());
		this.messageConverters.add(new PluginArrayHttpMessageConverter());
		this.messageConverters.add(new BuildHttpMessageConverter());
	}

	public void setClientHttpRequestFactory(ClientHttpRequestFactory httpRequestFactory) {
		this.httpRequestFactory = httpRequestFactory;
	}

	public ClientHttpRequestFactory getClientHttpRequestFactory() {
		return httpRequestFactory;
	}

	public <T> T getForObject(String uri, Class<T> clazz) throws IOException {
		ClientHttpRequest httpRequest = httpRequestFactory.createRequest(newURI(baseUri + uri), HttpMethod.GET);
		ClientHttpResponse httpResponse = httpRequest.execute();
		HttpMessageConverter<T> converter = findReadConverter(clazz);
		return converter.read(clazz, httpResponse);
	}

	private <T> HttpMessageConverter<T> findReadConverter(Class<T> clazz) {
		Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
		while (iterator.hasNext()) {
			HttpMessageConverter<?> converter = iterator.next();
			if (converter.canRead(clazz)) {
				return (HttpMessageConverter<T>) converter;
			}
		}
		throw new IllegalArgumentException("Cannot find message converter for class [" + clazz + "]");
	}

	private <T> HttpMessageConverter<T> findWriteConverter(Class<T> clazz) {
		Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
		while (iterator.hasNext()) {
			HttpMessageConverter<?> converter = iterator.next();
			if (converter.canWrite(clazz)) {
				return (HttpMessageConverter<T>) converter;
			}
		}
		throw new IllegalArgumentException("Cannot find message converter for class [" + clazz + "]");
	}

	private URI newURI(String uri) {
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid uri [" + uri + "]", e);
		}
	}

	public void post(String uri) throws IOException {
		ClientHttpRequest httpRequest = httpRequestFactory.createRequest(newURI(baseUri + uri), HttpMethod.POST);
		setImplicitHeaders(httpRequest);
		ClientHttpResponse httpResponse = httpRequest.execute();
		validateHttpResponse(httpResponse);
	}

	public void post(String uri, Object request) throws IOException {
		ClientHttpRequest httpRequest = httpRequestFactory.createRequest(newURI(baseUri + uri), HttpMethod.POST);
		setImplicitHeaders(httpRequest);
		HttpMessageConverter converter = findWriteConverter(request.getClass());
		converter.write(request, null, httpRequest);
		ClientHttpResponse httpResponse = httpRequest.execute();
		validateHttpResponse(httpResponse);
	}

	// it can be a strategy for validating HTTP response
	private void validateHttpResponse(ClientHttpResponse httpResponse) throws IOException {
		// TODO In case of errors throw a JenkinsClientException exception with as much info as possible!
		// TODO Distinguish between client / server error codes and use appropriate exception subclass
		if (!isOk(httpResponse.getRawStatusCode())) {
			throw new JenkinsClientException(httpResponse.getStatusCode(), httpResponse.getStatusText(),
					httpResponse.getHeaders());
		}
	}

	private void setImplicitHeaders(ClientHttpRequest httpRequest) {
		HttpHeaders headers = httpRequest.getHeaders();
		headers.setUserAgent(userAgent);
		if (credentials != null) {
			headers.setAuthorization(credentials);
		}
	}

	private boolean isOk(int responseCode) {
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
		case HttpURLConnection.HTTP_CREATED:
		case HttpURLConnection.HTTP_MOVED_TEMP:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Set credentials for the basic access authentication.
	 * <p>
	 * The credentials are sent as the {@value #HEADER_AUTHORIZATION} HTTP request header. The header is constructed as
	 * follows:
	 * <ul>
	 * <li>User and password are combined into a string "user:password"</li>
	 * <li>The resulting string is encoded using Base64</li>
	 * <li>The authorization method and a space i.e. "Basic " is then put before the encoded string.</li>
	 * </ul>
	 * For example, if the user is 'dpacak' and the password is 'passw0rd' then the header is formed as follows:
	 * 
	 * <pre>
	 * {@code Authentication: Basic ZHBhY2FrOnBhc3N3MHJk}
	 * </pre>
	 * 
	 * The basic access authentication mechanism provides <b>no confidentiality protection</b> for the transmitted
	 * credentials. They are merely encoded with Base64 in transit, but not encrypted or hashed in any way. It is,
	 * therefore, typically used over HTTPS.
	 * 
	 * @param user
	 * @param password
	 * @return this client
	 * @throws IllegalArgumentException
	 *             if user or password is {@code null}
	 * @see <a href="http://en.wikipedia.org/wiki/Basic_access_authentication">Basic access authentication</a>
	 * @since 1.0.0
	 */
	public JenkinsClient setCredentials(String user, String password) {
		checkArgumentNotNull(user, "User cannot be null");
		checkArgumentNotNull(password, "Password cannot be null");
		this.credentials = "Basic " + Base64.encodeString(user + ':' + password);
		return this;
	}

	/**
	 * Set OAuth2 token.
	 * 
	 * @param token
	 * @return this client
	 */
	public JenkinsClient setOAuth2Token(String token) {
		throw new UnsupportedOperationException("The OAuth2 authentication mechanism is not yet implemented");
	}

}
