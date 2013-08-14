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
