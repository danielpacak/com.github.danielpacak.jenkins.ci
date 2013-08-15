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
package com.danielpacak.jenkins.ci.core.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents an HTTP output message, consisting of {@linkplain #getHeaders() headers} and a writable
 * {@linkplain #getBody() body}.
 * <p>
 * Typically implemented by an HTTP request on the client-side.
 * 
 * @since 1.0.0
 */
public interface HttpOutputMessage extends HttpMessage {

	/**
	 * Get the body of this message as an output stream.
	 * 
	 * @return output stream
	 * @throws IOException
	 *             in case of I/O errors
	 * @since 1.0.0
	 */
	OutputStream getBody() throws IOException;

}
