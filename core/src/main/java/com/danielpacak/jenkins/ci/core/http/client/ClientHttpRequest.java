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

import com.danielpacak.jenkins.ci.core.http.HttpOutputMessage;
import com.danielpacak.jenkins.ci.core.http.HttpRequest;

/**
 * Represents a client-side HTTP request. Created via an implementation of the ClientHttpRequestFactory.
 * <p>
 * The request can be {@linkplain #execute() executed}, getting a {@link ClientHttpResponse} which can be read from.
 * 
 * @since 1.0.0
 */
public interface ClientHttpRequest extends HttpRequest, HttpOutputMessage {

	/**
	 * Execute this request, resulting in a {@link ClientHttpResponse} that can be read.
	 * 
	 * @return response result of the execution
	 * @throws IOException
	 *             in case of I/O errors
	 */
	ClientHttpResponse execute() throws IOException;

}
