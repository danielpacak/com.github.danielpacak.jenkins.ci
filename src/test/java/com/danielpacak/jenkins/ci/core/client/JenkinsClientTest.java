package com.danielpacak.jenkins.ci.core.client;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.service.JobService;

/**
 * Tests for {@link JenkinsClient}.
 */
public class JenkinsClientTest {

	JenkinsClient client;
	JobService jobService;

	@Before
	public void beforeTest() throws Exception {
		client = new JenkinsClient("localhost", 8080);
		jobService = new JobService(client);
	}

	@Ignore
	@Test
	public void testTriggerBuild() throws Exception {
		JobConfiguration config = new ClassPathJobConfiguration(
				"job/definition/free-style-job-definition.xml");

		Job job = jobService.createJob("release.and.deploy.jenkins.ci.client",
				config);
		jobService.triggerBuild(job);
	}

	@Test
	public void testGetBuild() throws Exception {
		Job job = new Job();
		job.setName("versioned-component-release");
		Build build = jobService.getBuild(job, new Long(7));
		System.out.println("Build status: " + build.getStatus());
		//Assert.assertEquals(new Long(6), build.getNumber());
		//Assert.assertEquals(Build.Status.SUCCESS, build.getStatus());
	}

}
