package com.danielpacak.jenkins.ci.core.http.client;

import java.io.IOException;

import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

/**
 * Strategy interface used by {@link JenkinsClient} to determine whether a particular response has an error or not.
 */
public interface ResponseErrorHandler {

	boolean hasError(ClientHttpResponse response) throws IOException;

	void handleError(ClientHttpResponse response) throws IOException;

}
