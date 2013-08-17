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
package com.danielpacak.jenkins.ci.core.http.client;

import java.io.IOException;
import java.io.InputStream;

import com.danielpacak.jenkins.ci.core.client.HttpClientErrorException;
import com.danielpacak.jenkins.ci.core.client.HttpServerErrorException;
import com.danielpacak.jenkins.ci.core.client.JenkinsClientException;
import com.danielpacak.jenkins.ci.core.http.HttpStatus;
import com.danielpacak.jenkins.ci.core.util.Streams;

public class DefaultResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();
		return statusCode.series() == HttpStatus.Series.CLIENT_ERROR
				|| statusCode.series() == HttpStatus.Series.SERVER_ERROR;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		HttpStatus statusCode = response.getStatusCode();
		switch (statusCode.series()) {
		case CLIENT_ERROR:
			throw new HttpClientErrorException(statusCode, response.getStatusText(), response.getHeaders(),
					getResponseBody(response));
		case SERVER_ERROR:
			throw new HttpServerErrorException(statusCode, response.getStatusText(), response.getHeaders(),
					getResponseBody(response));
		default:
			throw new JenkinsClientException("Unknown status code [" + statusCode + "]");
		}
	}

	private byte[] getResponseBody(ClientHttpResponse response) {
		try {
			InputStream responseBody = response.getBody();
			if (responseBody != null) {
				return Streams.toByteArray(responseBody);
			}
		} catch (IOException ex) {
			// ignore
		}
		return new byte[0];
	}

}
