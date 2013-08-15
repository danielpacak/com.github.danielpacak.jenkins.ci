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
package com.danielpacak.jenkins.ci.core;

import java.io.InputStream;

import com.danielpacak.jenkins.ci.core.util.Preconditions;

/**
 * Job configuration stored as a class path resource.
 * 
 * @since 1.0.0
 */
public class ClassPathJobConfiguration implements JobConfiguration {

	private final String path;

	public ClassPathJobConfiguration(String path) {
		this.path = Preconditions.checkArgumentNotNull(path, "Path cannot be null");
	}

	@Override
	public InputStream getInputStream() {
		return getClass().getResourceAsStream("/" + path);
	}

}
