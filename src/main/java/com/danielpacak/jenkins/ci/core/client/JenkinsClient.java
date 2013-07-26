package com.danielpacak.jenkins.ci.core.client;

import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

public class JenkinsClient {

	private final String host;
	private final Integer port;

	public JenkinsClient(String host, Integer port) {
		this.host = checkArgumentNotNull(host, "Host cannot be null");
		this.port = checkArgumentNotNull(port, "Port cannot be null");
	}

	public void setCredentials(String string, String string2) {
		throw new IllegalStateException("Not implemented yet");
	}

	public String getHost() {
		return host;
	}

	public Integer getPort() {
		return port;
	}

}
