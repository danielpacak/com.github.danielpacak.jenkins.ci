package com.danielpacak.jenkins.ci.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
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

	private JobService jobService;

	@Before
	public void beforeTest() {
		jobService = new JobService(new JenkinsClient());
	}

	@Test
	public void testCreateSimpleProjectAndTriggerBuild() throws Exception {

		Job job = newJobWithRandomName();
		JobConfiguration config = new ClassPathJobConfiguration("job/config/free-style.xml");

		job = jobService.createJob(job, config);
		Long buildNumber = jobService.triggerBuild(job);

		TimeUnit.SECONDS.sleep(10);

		Build build = jobService.getBuild(job, buildNumber);
		Assert.assertEquals(Build.Status.SUCCESS, build.getStatus());
		Assert.assertEquals(buildNumber, build.getNumber());
		jobService.deleteJob(job);
	}

	@Test
	public void testCreateParameterizedJobAndTriggerBuild() throws Exception {
		JobConfiguration jobConfiguration = new ClassPathJobConfiguration("job/config/free-style-parameterized.xml");

		Job testJob = newJobWithRandomName();
		// FIXME Something is wrong with XPath -> returned job's name becomes first param name!!
		jobService.createJob(testJob, jobConfiguration);

		Map<String, String> parameters = new HashMap<String, String>();
		// Override default parameters values stored along with the job configuration.
		parameters.put("FIRST_NAME", "Mike");
		parameters.put("LAST_NAME", "Tyson");
		parameters.put("IS_SMART", "true");
		parameters.put("SECRET_PASSWORD", "cypher");

		jobService.triggerBuild(testJob, parameters);
		// TODO How to assert? Get output and search for echo log?
		jobService.deleteJob(testJob);
	}

	@Test
	public void testGetJobs() throws Exception {
		List<Job> jobs = jobService.getJobs();
	}

}
