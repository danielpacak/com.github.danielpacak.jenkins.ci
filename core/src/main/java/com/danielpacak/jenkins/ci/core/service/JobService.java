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

import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_API_XML;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_CREATE_ITEM;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_DO_DELETE;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_JOB;
import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

/**
 * Job service class.
 * 
 * @since 1.0.0
 */
public class JobService extends AbstractService {

	/**
	 * Create job service for the default client.
	 * 
	 * @since 1.0.0
	 */
	public JobService() {
		super();
	}

	/**
	 * Create job service for the given client.
	 * 
	 * @param client
	 *            client
	 * @since 1.0.0
	 */
	public JobService(JenkinsClient client) {
		super(client);
	}

	/**
	 * Get all jobs.
	 * 
	 * @return list of jobs
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @since 1.0.0
	 */
	public List<Job> getJobs() throws IOException {
		return Arrays.asList(client.getForObject(SEGMENT_API_XML + "?depth=2", Job[].class));
	}

	/**
	 * Create a new job with the given name and configuration.
	 * 
	 * @param name
	 *            the name of the job
	 * @param configuration
	 *            the configuration of the job
	 * @return the job that has been created
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @throws IllegalArgumentException
	 *             if job or the name of the job, or job configuration is {@code null}
	 * @since 1.0.0
	 **/
	public Job createJob(Job job, JobConfiguration configuration) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(job.getName(), "Job.name cannot be null");
		checkArgumentNotNull(configuration, "JobConfiguration cannot be null");
		client.post(SEGMENT_CREATE_ITEM + "?name=" + job.getName(), configuration);
		return getJob(job.getName());
	}

	/**
	 * Delete the given job.
	 * 
	 * @param job
	 *            the job to be deleted
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @since 1.0.0
	 */
	public void deleteJob(Job job) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(job.getName(), "Job.name cannot be null");
		client.post(SEGMENT_JOB + "/" + job.getName() + SEGMENT_DO_DELETE);
	}

	/**
	 * Get a job with the given name.
	 * 
	 * @param name
	 *            the name of the job
	 * @return job or {@code null} if a job with the given name does not exist
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @throws IllegalArgumentException
	 *             if name is {@code null}
	 * @since 1.0.0
	 */
	public Job getJob(String name) throws IOException {
		return client.getForObject(SEGMENT_JOB + "/" + name + SEGMENT_API_XML, Job.class);
	}

	/**
	 * Get the configuration of the given job.
	 * 
	 * @param job
	 *            job
	 * @return job configuration or {@code null} if a job with the given name does not exist
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @throws IllegalArgumentException
	 *             if job or the name of the job is {@code null}
	 * @since 1.0.0
	 */
	public JobConfiguration getJobConfiguration(Job job) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(job.getName(), "Job.name cannot be null");
		return client.getForObject(SEGMENT_JOB + "/" + job.getName() + "/" + "config.xml", JobConfiguration.class);
	}

	/**
	 * Trigger a build of the given job.
	 * 
	 * @param job
	 *            the job to be built
	 * @return build number
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @throws IllegalArgumentException
	 *             if job or the name of the job is {@code null}
	 * @since 1.0.0
	 */
	public Long triggerBuild(Job job) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(job.getName(), "Job.name cannot be null");
		client.post(SEGMENT_JOB + "/" + job.getName() + "/build");
		return job.getNextBuildNumber();
	}

	/**
	 * Trigger a build of the given job with parameters.
	 * 
	 * @param job
	 *            the job to be built
	 * @param parameters
	 *            build parameters
	 * @return build number
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @since 1.0.0
	 */
	public Long triggerBuild(Job job, Map<String, String> parameters) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(job.getName(), "Job.name cannot be null");
		checkArgumentNotNull(parameters, "Parameters cannot be null");
		client.post(SEGMENT_JOB + "/" + job.getName() + "/buildWithParameters" + "?" + toQueryParams(parameters));
		return job.getNextBuildNumber();
	}

	/**
	 * Return the build of the given job.
	 * 
	 * @param job
	 *            job
	 * @param number
	 *            build number
	 * @return build or {@code null} if the build does not exist
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @throws IllegalArgumentException
	 *             if job or the name of the job, or build number is {@code null}
	 * @since 1.0.0
	 */
	public Build getBuild(Job job, Long number) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(job.getName(), "Job.name cannot be null");
		checkArgumentNotNull(number, "Number cannot be null");
		return client.getForObject(SEGMENT_JOB + "/" + job.getName() + "/" + number + "/api/xml", Build.class);
	}

	// TODO Parameters need to be encoded properly!
	private String toQueryParams(Map<String, String> parameters) {
		StringBuilder queryParams = new StringBuilder();
		for (Entry<String, String> entry : parameters.entrySet()) {
			queryParams.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		queryParams.deleteCharAt(0); // remove trailing &
		return queryParams.toString();
	}

}