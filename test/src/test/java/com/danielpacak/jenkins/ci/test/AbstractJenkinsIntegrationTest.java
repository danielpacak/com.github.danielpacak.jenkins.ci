package com.danielpacak.jenkins.ci.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.danielpacak.jenkins.ci.core.Job;

public abstract class AbstractJenkinsIntegrationTest {

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Starting Jenkins CI...");
	}

	@AfterClass
	public static void afterClass() {
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
