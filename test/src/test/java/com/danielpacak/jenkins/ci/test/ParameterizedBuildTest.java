package com.danielpacak.jenkins.ci.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.service.JobService;

public class ParameterizedBuildTest extends AbstractJenkinsIntegrationTest {

	@Test
	public void testCreateParameterizedJobAndTriggerBuild() throws Exception {
		JenkinsClient jenkinsClient = new JenkinsClient();
		JobConfiguration jobConfiguration = new ClassPathJobConfiguration("job/config/free-style-parameterized.xml");
		JobService jobService = new JobService(jenkinsClient);

		Job testJob = newJobWithRandomName();
		// FIXME Something is wrong with XPath -> returned job's name becomes first param name!!
		jobService.createJob(testJob, jobConfiguration);

		Map<String, String> parameters = new HashMap<String, String>();
		// Override default parameters values stored along with the job configuration.
		parameters.put("FIRST_NAME", "Mike");
		parameters.put("LAST_NAME", "Tyson");

		jobService.triggerBuild(testJob, parameters);
		// TODO How to assert? Get output and search for echo log?
	}

}
