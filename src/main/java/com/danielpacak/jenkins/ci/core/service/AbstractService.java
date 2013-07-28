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
