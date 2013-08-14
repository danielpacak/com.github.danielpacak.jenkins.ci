package com.danielpacak.jenkins.ci.test;

import org.junit.After;
import org.junit.Before;

import com.danielpacak.jenkins.ci.core.Job;

public abstract class AbstractJenkinsIntegrationTest {

	@Before
	public void beforeTest() {
		System.out.println("Starting Jenkins CI...");
	}

	@After
	public void afterTest() {
		System.out.println("Stopping Jenkins CI...");
	}

	/**
	 * Return a new instance of the {@link Job} class with a random name. The name has the following patter
	 * <code>junit-{currentTimeMillis}</code>.
	 * 
	 * @return job
	 */
	protected Job newJobWithRandomName() {
		return new Job().setName("junit-" + System.currentTimeMillis());
	}

}
