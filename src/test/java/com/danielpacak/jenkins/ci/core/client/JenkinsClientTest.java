package com.danielpacak.jenkins.ci.core.client;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.service.JobService;

/**
 * Tests for {@link JenkinsClient}.
 */
public class JenkinsClientTest {

	@Test
	public void testMe() throws Exception {
		JenkinsClient client = new JenkinsClient("localhost", 8080);
		// client.setCredentials("user", "passw0rd");
		JobService jobService = new JobService(client);
		JobConfiguration config = new ClassPathJobConfiguration(
				"job/definition/free-style-job-definition.xml");

		Job job = jobService.createJob("release.and.deploy.jenkins.ci.client",
				config);
		jobService.triggerBuild(job);
	}

}
