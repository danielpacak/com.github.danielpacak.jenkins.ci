package com.danielpacak.jenkins.ci.core.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;

/**
 * Integration tests for {@link JobService}.
 */
public class JobServiceIntegrationTest extends AbstractServiceIntegrationTest {

	private JobService service;

	@Before
	public void before() throws Exception {
		super.before();
		service = new JobService(client);
	}

	@Test
	public void testGetJobs() throws Exception {
		List<Job> jobs = service.getJobs();
		for (Job job : jobs) {
			System.out.println(job.getName());
		}
	}

	@Test
	public void testCreateJob() throws Exception {
		String name = "junit-test-job-" + System.currentTimeMillis();

		Job job = new Job().setName(name);
		JobConfiguration configuration = new ClassPathJobConfiguration(
				"job/configuration/free-style-job-definition.xml");
		job = service.createJob(job, configuration);
		System.out.println("job.name: " + job.getName());
		System.out.println("job.buildable: " + job.getBuildable());
		System.out.println("job.url: " + job.getUrl());
		System.out.println("job.nextBuildNumber: " + job.getNextBuildNumber());

		Long buildNumber = service.triggerBuild(job);
		TimeUnit.SECONDS.sleep(2);
		Build build = service.getBuild(job, buildNumber);
		System.out.println(build.getStatus());
		System.out.println(build.getNumber());

	}
	
	@Test
	public void testBuild() throws Exception {
		Job job = new Job().setName("junit-test-job-1375084701747");
		service.triggerBuild(job);
	}

}
