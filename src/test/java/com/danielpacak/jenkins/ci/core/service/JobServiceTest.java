package com.danielpacak.jenkins.ci.core.service;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

public class JobServiceTest {

	@Test
	public void testDeleteJob() throws Exception {
		JenkinsClient client = new JenkinsClient("localhost", 8080);
		JobService jobService = new JobService(client);
		
		Job toBeDeleted = new Job();
		toBeDeleted.setName("to-be-deleted");
		
		jobService.deleteJob(toBeDeleted);
	}

}
