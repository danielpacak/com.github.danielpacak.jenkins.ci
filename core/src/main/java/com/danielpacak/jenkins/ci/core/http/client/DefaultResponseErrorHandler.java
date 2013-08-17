package com.danielpacak.jenkins.ci.core.http.client;

import java.io.IOException;

import com.danielpacak.jenkins.ci.core.client.JenkinsClientException;
import com.danielpacak.jenkins.ci.core.http.HttpStatus;

public class DefaultResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();
		return statusCode.series() == HttpStatus.Series.CLIENT_ERROR
				|| statusCode.series() == HttpStatus.Series.SERVER_ERROR;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		throw new JenkinsClientException(response.getStatusCode(), response.getStatusText(), response.getHeaders());
	}

}
