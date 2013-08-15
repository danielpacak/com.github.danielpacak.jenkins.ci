/*
 * #%L
 * Jenkins Java API
 * %%
 * Copyright (C) 2013 Daniel Pacak
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.danielpacak.jenkins.ci.test;

import java.util.HashMap;
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

	private static final int WAIT_FOR_BUILD_COMPLETION_TIMEOUT = 10;

	private JobService jobService;

	@Before
	public void beforeTest() {
		jobService = new JobService(new JenkinsClient().setCredentials("dpacak", "passw0rd"));
	}

	@Test
	public void testCreateSimpleProjectAndTriggerBuild() throws Exception {

		Job job = newJobWithRandomName();
		JobConfiguration config = new ClassPathJobConfiguration("job/config/free-style.xml");

		job = jobService.createJob(job, config);
		Long buildNumber = jobService.triggerBuild(job);

		TimeUnit.SECONDS.sleep(WAIT_FOR_BUILD_COMPLETION_TIMEOUT);

		Build build = jobService.getBuild(job, buildNumber);
		Assert.assertEquals(Build.Status.SUCCESS, build.getStatus());
		Assert.assertEquals(buildNumber, build.getNumber());
		jobService.deleteJob(job);
	}
	
	@Test
	public void testTriggerBuildAndWait() throws Exception {
		Job job = newJobWithRandomName();
		JobConfiguration config = new ClassPathJobConfiguration("job/config/free-style.xml");
		job = jobService.createJob(job, config);
		
		Build build = jobService.triggerBuildAndWait(job);
		Assert.assertEquals(Build.Status.SUCCESS, build.getStatus());
	}

	@Test
	public void testCreateParameterizedJobAndTriggerBuild() throws Exception {
		JobConfiguration jobConfiguration = new ClassPathJobConfiguration("job/config/free-style-parameterized.xml");

		Job job = newJobWithRandomName();
		job = jobService.createJob(job, jobConfiguration);

		Map<String, String> parameters = new HashMap<String, String>();
		// Override default parameters values stored along with the job configuration.
		parameters.put("FIRST_NAME", "Mike");
		parameters.put("LAST_NAME", "Tyson");
		parameters.put("IS_SMART", "true");
		parameters.put("SECRET_PASSWORD", "cypher");

		Long buildNumber = jobService.triggerBuild(job, parameters);
		// TODO How to assert that the build was triggered with the given parameters? Get output and search for text
		// pattern?

		TimeUnit.SECONDS.sleep(WAIT_FOR_BUILD_COMPLETION_TIMEOUT);
		Build build = jobService.getBuild(job, buildNumber);
		Assert.assertEquals(Build.Status.SUCCESS, build.getStatus());
		Assert.assertEquals(buildNumber, build.getNumber());
		jobService.deleteJob(job);
	}

	@Test
	public void testGetJobs() throws Exception {
		jobService.getJobs();
	}

}
