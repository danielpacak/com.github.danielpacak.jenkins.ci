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
package com.danielpacak.jenkins.ci.core.service;

import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.util.Preconditions;

/**
 * Base Jenkins API service.
 * 
 * @since 1.0.0
 */
public abstract class AbstractService {

	protected final JenkinsClient client;

	/**
	 * Create service for the default client.
	 */
	public AbstractService() {
		this(new JenkinsClient());
	}

	/**
	 * Create service for the given client.
	 * 
	 * @param client
	 */
	public AbstractService(JenkinsClient client) {
		this.client = Preconditions.checkArgumentNotNull(client, "Client cannot be null");
	}

	/**
	 * Get configured {@link JenkinsClient}.
	 * 
	 * @return configured client
	 */
	public JenkinsClient getClient() {
		return client;
	}

}
