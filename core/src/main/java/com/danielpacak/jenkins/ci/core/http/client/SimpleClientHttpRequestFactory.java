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
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import com.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.danielpacak.jenkins.ci.core.http.HttpMethod;
import com.danielpacak.jenkins.ci.core.util.Base64;

public class SimpleClientHttpRequestFactory implements ClientHttpRequestFactory {

	private String credentials;
	private String userAgent;

	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		HttpURLConnection connection = openConnection(uri.toURL());
		prepareConnection(connection, httpMethod);

		return new SimpleClientHttpRequest(connection);
	}

	protected HttpURLConnection openConnection(URL url) throws IOException {
		URLConnection urlConnection = url.openConnection();
		return (HttpURLConnection) urlConnection;
	}

	protected void prepareConnection(HttpURLConnection connection, HttpMethod httpMethod) throws IOException {
		connection.setDoInput(true);
		connection.setRequestMethod(httpMethod.name());
		if (HttpMethod.GET == httpMethod) {
			connection.setInstanceFollowRedirects(true);
		} else {
			connection.setInstanceFollowRedirects(false);
		}
		if (HttpMethod.PUT == httpMethod || HttpMethod.POST == httpMethod) {
			connection.setDoOutput(true);
		} else {
			connection.setDoOutput(false);
		}
		if (credentials != null) {
			connection.setRequestProperty(HttpHeaders.AUTHORIZATION, credentials);
		}
		if (userAgent != null) {
			connection.setRequestProperty(HttpHeaders.USER_AGENT, userAgent);
		}
	}

	@Override
	public void setCredentials(String user, String password) {
		this.credentials = "Basic " + Base64.encodeString(user + ':' + password);
	}

	@Override
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}
