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
package com.danielpacak.jenkins.ci.core.client;

import java.io.IOException;

/**
 * Exception thrown when an I/O error occurs.
 */
public class ResourceAccessException extends JenkinsClientException {

	private static final long serialVersionUID = 5220056020114484316L;

	/**
	 * Construct a new {@code ResourceAccessException} exception with the given message.
	 * 
	 * @param message
	 */
	public ResourceAccessException(String message) {
		super(message);
	}

	/**
	 * Construct a new {@code ResourceAccessException} with the given message and {@link IOException}.
	 * 
	 * @param message
	 *            message
	 * @param exception
	 *            {@code IOException}
	 */
	public ResourceAccessException(String message, IOException exception) {
		super(message, exception);
	}

}
