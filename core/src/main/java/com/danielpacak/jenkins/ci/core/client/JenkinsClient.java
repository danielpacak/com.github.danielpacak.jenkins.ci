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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.danielpacak.jenkins.ci.core.util.Base64;
import com.danielpacak.jenkins.ci.core.util.Streams;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

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
	protected static final String USER_AGENT = "JenkinsJavaAPI/${version}";

	private String baseUri;
	private String userAgent = USER_AGENT;

	private String credentials;

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
		baseUri = prefix != null ? uri.append(prefix).toString() : uri.toString();
	}

	public JenkinsResponse get(String uri) throws IOException {
		URL connectionUrl = new URL(baseUri + uri);
		HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
		connection.setRequestMethod(METHOD_GET);
		setCommonHeaders(connection);
		return createJenkinsResponse(connection);
	}

	public JenkinsResponse post(String uri, Map<String, String> headers, InputStream payload) throws IOException {
		URL connectionUrl = new URL(baseUri + uri);
		HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
		connection.setRequestMethod(METHOD_POST);
		setCommonHeaders(connection);
		setRequestSpecificHeaders(connection, headers);

		connection.setDoOutput(true);
		OutputStream outputStream = connection.getOutputStream();
		Streams.copy(payload, outputStream);

		return createJenkinsResponse(connection);
	}

	private void setRequestSpecificHeaders(HttpURLConnection connection, Map<String, String> headers) {
		for (Map.Entry<String, String> header : headers.entrySet()) {
			connection.setRequestProperty(header.getKey(), header.getValue());
		}
	}

	public JenkinsResponse post(String uri) throws IOException {
		URL connectionUrl = new URL(baseUri + uri);
		HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
		connection.setRequestMethod(METHOD_POST);
		setCommonHeaders(connection);
		return createJenkinsResponse(connection);
	}

	private void setCommonHeaders(HttpURLConnection connection) {
		if (credentials != null) {
			connection.setRequestProperty(HEADER_AUTHORIZATION, credentials);
		}

		connection.setRequestProperty(HEADER_USER_AGENT, userAgent);
		connection.setRequestProperty(HEADER_ACCEPT, "application/xml");
	}

	private JenkinsResponse createJenkinsResponse(HttpURLConnection connection) throws IOException {
		int responseCode = connection.getResponseCode();
		if (isOk(responseCode)) {
			XmlResponse xmlResponse = createXmlResponse(connection);
			return new JenkinsResponse(xmlResponse);
		}
		if (connection.getErrorStream() != null) {
		}
		throw new IllegalStateException("There was an error calling the API" + connection.getURL());
	}

	private XmlResponse createXmlResponse(HttpURLConnection connection) throws IOException {
		String contentType = connection.getHeaderField(HEADER_CONTENT_TYPE);
		String contentLength = connection.getHeaderField(HEADER_CONTENT_LENGTH);

		boolean validContentType = contentType != null && contentType.startsWith("application/xml");
		boolean validContentLength = contentLength == null || Long.valueOf(contentLength) > 0;

		if (validContentType && validContentLength) {
			String resp = Streams.toString(connection.getInputStream());
			return new XmlResponse(resp);
		}
		return null;
	}

	private boolean isOk(int responseCode) {
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
		case HttpURLConnection.HTTP_CREATED:
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
