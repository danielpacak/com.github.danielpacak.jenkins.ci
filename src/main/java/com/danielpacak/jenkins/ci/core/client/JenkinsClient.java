package com.danielpacak.jenkins.ci.core.client;

import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.danielpacak.jenkins.ci.core.util.Streams;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Client class for interacting with Jenkins HTTP/XML API.
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
		connection.setRequestProperty(HEADER_USER_AGENT, userAgent);
		connection.setRequestProperty(HEADER_ACCEPT, "application/xml");
	}

	private JenkinsResponse createJenkinsResponse(HttpURLConnection connection) throws IOException {
		int responseCode = connection.getResponseCode();
		if (isOk(responseCode)) {
			XmlResponse xmlResponse = createXmlResponse(connection);
			return new JenkinsResponse(xmlResponse);
		}
		throw new IllegalStateException("There was an error calling the API");
	}

	private XmlResponse createXmlResponse(HttpURLConnection connection) throws IOException {
		String contentType = connection.getHeaderField(HEADER_CONTENT_TYPE);
		String contentLength = connection.getHeaderField(HEADER_CONTENT_LENGTH);

		boolean validContentType = contentType != null && contentType.startsWith("application/xml");
		boolean validContentLength = contentLength == null || Long.valueOf(contentLength) > 0;

		if (validContentType && validContentLength) {
			return new XmlResponse(connection.getInputStream());
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

}
