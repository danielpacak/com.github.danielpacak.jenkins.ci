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
package com.danielpacak.jenkins.ci.core.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

/**
 * Tests for {@link JobService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {

	@Mock
	private JenkinsClient client;
	@Mock
	private JobConfiguration jobConfiguration;

	private JobService service;

	@Before
	public void beforeTest() throws Exception {
		service = new JobService(client);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createJob_WithNullJob_ThrowsException() throws Exception {
		service.createJob(null, jobConfiguration);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createJob_WithNullJobConfiguration_ThrowsException() throws Exception {
		service.createJob(new Job().setName("vacuum.my.room"), null);
	}

	@Test
	public void createJob() throws Exception {
		Job job = new Job().setName("vacuum.my.room");
		service.createJob(job, jobConfiguration);
		verify(client).post("/createItem?name=vacuum.my.room", jobConfiguration);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteJob_WithNullJob_ThrowsException() throws Exception {
		service.deleteJob(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteJob_WithNullJobName_ThrowsException() throws Exception {
		service.deleteJob(new Job());
	}

	@Test
	public void deleteJob() throws Exception {
		Job job = new Job().setName("j");
		service.deleteJob(job);
		verify(client).post("/job/j/doDelete");
	}

	@Test
	public void getJobs() throws Exception {
		Job[] someJobs = new Job[] {};
		when(client.getForObject("/api/xml?depth=2", Job[].class)).thenReturn(someJobs);
		assertEquals(Arrays.asList(someJobs), service.getJobs());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getJobConfiguration_WithNullJob_ThrowsException() throws Exception {
		service.getJobConfiguration(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getJobConfiguration_WithNullJobName_ThrowsException() throws Exception {
		service.getJobConfiguration(new Job());
	}

	@Test
	public void getJobConfiguration() throws Exception {
		Job job = new Job().setName("vacuum.my.room");
		when(client.getForObject("/job/vacuum.my.room/config.xml", JobConfiguration.class))
				.thenReturn(jobConfiguration);
		assertEquals(jobConfiguration, service.getJobConfiguration(job));
	}

	@Test(expected = IllegalArgumentException.class)
	public void triggerBuild_WithNullJob_ThrowsException() throws Exception {
		service.triggerBuild(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void triggerbuild_WithNullJobName_ThrowsException() throws Exception {
		service.triggerBuild(new Job());
	}

	@Test
	public void triggerBuild() throws Exception {
		Job job = new Job().setName("vacuum.my.room").setNextBuildNumber(new Long(23));
		Long buildNumber = service.triggerBuild(job);
		verify(client).post("/job/vacuum.my.room/build");
		assertEquals(new Long(23), buildNumber);
	}

	@Test(expected = IllegalArgumentException.class)
	public void triggerParameterizedBuild_withNullJob_throwsException() throws Exception {
		service.triggerBuild(null, new HashMap<String, String>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void triggerParameterizedBuild_withNullJobName_throwsException() throws Exception {
		service.triggerBuild(new Job(), new HashMap<String, String>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void triggerParameterizedBuild_withNullParameters_throwsException() throws Exception {
		service.triggerBuild(new Job().setName("vacuum.my.room"), null);
	}

	@Test
	public void triggerParameterizedBuild() throws Exception {
		Job job = new Job().setName("vacuum.my.room").setNextBuildNumber(new Long(69));
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("param1", "value1");
		parameters.put("param2", "value2");

		Long buildNumber = service.triggerBuild(job, parameters);

		verify(client).post("/job/vacuum.my.room/buildWithParameters?param1=value1&param2=value2");
		assertEquals(new Long(69), buildNumber);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getBuild_WithNullJob_ThrowsException() throws Exception {
		service.getBuild(null, new Long(76));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getBuild_WithNullJobName_ThrowsException() throws Exception {
		service.getBuild(new Job(), new Long(76));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getBuild_WithNullBuildNumber_ThrowsException() throws Exception {
		service.getBuild(new Job().setName("vacuum.my.room"), null);
	}

	@Test
	public void getBuild() throws Exception {
		Job job = new Job().setName("vacuum.my.room");
		Build someBuild = new Build();
		when(client.getForObject("/job/vacuum.my.room/69/api/xml", Build.class)).thenReturn(someBuild);
		assertEquals(someBuild, service.getBuild(job, new Long(69)));
	}

}
