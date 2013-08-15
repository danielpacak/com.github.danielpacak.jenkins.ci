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

import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
	private JobService service;

	@Before
	public void before() throws Exception {
		service = new JobService(client);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateJobWithNullJob() throws Exception {
		service.createJob(null, new TestJobConfiguration());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateJobWithNullJobConfiguration() throws Exception {
		service.createJob(new Job().setName("j"), null);
	}

	@Test
	@Ignore
	public void testCreateJob() throws Exception {
		Job job = new Job().setName("j");
		JobConfiguration jobConfiguration = new TestJobConfiguration();
		service.createJob(job, jobConfiguration);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/xml");
		verify(client).post("/createItem?name=j", headers, TestJobConfiguration.RETURNED_INPUT_STREAM);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteJobWithNullJob() throws Exception {
		service.deleteJob(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteJobWithNullJobName() throws Exception {
		service.deleteJob(new Job());
	}

	@Test
	public void testDeleteJob() throws Exception {
		Job job = new Job().setName("j");
		service.deleteJob(job);
		verify(client).post("/job/j/doDelete");
	}

	static class TestJobConfiguration implements JobConfiguration {
		static final InputStream RETURNED_INPUT_STREAM = new ByteArrayInputStream(new byte[0]);

		@Override
		public InputStream getInputStream() {
			return RETURNED_INPUT_STREAM;
		}
	}

}
