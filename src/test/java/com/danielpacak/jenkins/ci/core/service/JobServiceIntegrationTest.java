package com.danielpacak.jenkins.ci.core.service;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

/**
 * Integration tests for {@link JobService}.
 */
public class JobServiceIntegrationTest {

	@Test
	public void testCreateJob() throws Exception {
		JenkinsClient client = new JenkinsClient("localhost", 8080);
		JobService service = new JobService(client);
		
		// to make it unique
		String name = "junit-test-job-" + System.currentTimeMillis();

		Job job = new Job().setName(name);
		JobConfiguration configuration = new ClassPathJobConfiguration(
				"job/configuration/free-style-job-definition.xml");
		job = service.createJob(job, configuration);
		System.out.println("job.name: " + job.getName());
		System.out.println("job.buildable: " + job.getBuildable());
		System.out.println("job.url: " + job.getUrl());
		
		service.triggerBuild(job);
		//service.deleteJob(job);
	}

}
