package com.danielpacak.jenkins.ci.core.service;

import org.junit.Before;

import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

public abstract class AbstractServiceIntegrationTest {

	protected JenkinsClient client;

	@Before
	public void before() throws Exception {
		client = new JenkinsClient("localhost", 8080);
	}

}
