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

	protected static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";
	protected static final String HEADER_AUTHORIZATION = "Authorization";
	protected static final String HEADER_USER_AGENT = "User-Agent";

	protected static final String METHOD_POST = "POST";
	protected static final String METHOD_GET = "GET";

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
	 * Create client for host, port, and scheme
	 * 
	 * @param hostname
	 * @param port
	 * @param scheme
	 */
	public JenkinsClient(String scheme, String host, Integer port, String prefix) {
		checkArgumentNotNull(scheme, "Scheme cannot be null");
		checkArgumentNotNull(host, "Host cannot be null");
		checkArgumentNotNull(port, "Port cannot be null");
		StringBuilder uri = new StringBuilder(scheme);
		uri.append("://");
		uri.append(host);
		uri.append(':').append(port);
		if (prefix != null) {
			uri.append(prefix);
		}
		baseUri = uri.toString();
	}

	public JenkinsResponse post(String uri, Map<String, String> headers, InputStream payload) throws IOException {
		URL connectionUrl = new URL(baseUri + uri);
		HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
		connection.setRequestMethod(METHOD_POST);
		connection.setRequestProperty(HEADER_USER_AGENT, userAgent);
		connection.setRequestProperty(HEADER_ACCEPT, "application/xml");

		for (Map.Entry<String, String> header : headers.entrySet()) {
			connection.setRequestProperty(header.getKey(), header.getValue());
		}

		connection.setDoOutput(true);
		OutputStream outputStream = connection.getOutputStream();
		Streams.copy(payload, outputStream);

		int responseCode = connection.getResponseCode();
		if (isOk(responseCode)) {
			return createJenkinsResponse(connection);
		} else {
			throw new IllegalStateException("Faild this post man: " + responseCode);
		}

	}

	public JenkinsResponse createJenkinsResponse(HttpURLConnection connection) throws IOException {
		XmlResponse xmlResponse = null;
		try {
			xmlResponse = new XmlResponse(connection.getInputStream());
		} catch (Exception e) {
			// ignore
			System.out.println("not parsable response??");
		}
		return new JenkinsResponse(xmlResponse);

	}

	public JenkinsResponse get(String uri) throws IOException {
		URL connectionUrl = new URL(baseUri + uri);
		HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
		connection.setRequestMethod(METHOD_GET);
		connection.setRequestProperty(HEADER_USER_AGENT, userAgent);
		connection.setRequestProperty(HEADER_ACCEPT, "application/xml");

		int responseCode = connection.getResponseCode();
		System.out.println("response code: " + responseCode);
		if (isOk(responseCode)) {
			return createJenkinsResponse(connection);
		} else {
			// prepare and throw exception
			throw new IllegalStateException("error with this request: " + responseCode);
		}
	}

	public JenkinsResponse post(String uri) throws IOException {
		URL connectionUrl = new URL(baseUri + uri);
		System.out.println("Posting to: " + connectionUrl.toString());
		HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
		HttpURLConnection.setFollowRedirects(false);
		connection.setRequestMethod(METHOD_POST);
		connection.setRequestProperty(HEADER_USER_AGENT, userAgent);
		connection.setRequestProperty(HEADER_ACCEPT, "application/xml");

		// not content this time
		connection.setDoOutput(true);
		connection.setFixedLengthStreamingMode(0);
		connection.setRequestProperty(HEADER_CONTENT_LENGTH, "0");

		int responseCode = connection.getResponseCode();
		// System.out.println("headers: " +connection.getRequestProperties());
		System.out.println("send or not send?" + responseCode);
		return new JenkinsResponse(new XmlResponse(connection.getInputStream()));
	}

	private boolean isOk(int responseCode) {
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
		case HttpURLConnection.HTTP_CREATED:
		case HttpURLConnection.HTTP_MOVED_TEMP: // for example for doDelete requests
			return true;
		default:
			return false;
		}
	}

}
