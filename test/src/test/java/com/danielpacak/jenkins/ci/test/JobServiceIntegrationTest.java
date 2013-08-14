package com.danielpacak.jenkins.ci.test;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.service.JobService;

/**
 * Integration tests for {@link JobService}.
 */
public class JobServiceIntegrationTest extends AbstractJenkinsIntegrationTest {

	@Test
	public void testCreateSimpleProjectAndTriggerBuild() throws Exception {
		JenkinsClient jenkinsClient = new JenkinsClient();
		JobService jobService = new JobService(jenkinsClient);

		Job job = newJobWithRandomName();
		JobConfiguration config = new ClassPathJobConfiguration("job/config/free-style.xml");

		job = jobService.createJob(job, config);
		Long buildNumber = jobService.triggerBuild(job);

		TimeUnit.SECONDS.sleep(10);

		Build build = jobService.getBuild(job, buildNumber);
		Assert.assertEquals(Build.Status.SUCCESS, build.getStatus());
		Assert.assertEquals(buildNumber, build.getNumber());
	}

}
