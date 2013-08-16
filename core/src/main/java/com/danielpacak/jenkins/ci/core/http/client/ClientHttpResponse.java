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

import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.danielpacak.jenkins.ci.core.http.HttpStatus;

/**
 * Represents a client-side HTTP response. Obtained via an calling of the {@link ClientHttpRequest#execute()} method.
 * <p>
 * A {@link ClientHttpResponse} must be closed, typically in a {@code finally} block.
 */
public interface ClientHttpResponse extends HttpInputMessage {

	void close();

	int getRawStatusCode() throws IOException;

	HttpStatus getStatusCode() throws IOException;

	String getStatusText() throws IOException;

}
