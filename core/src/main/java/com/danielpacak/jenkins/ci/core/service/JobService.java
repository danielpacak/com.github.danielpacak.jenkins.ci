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

import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.CREATE_ITEM_SEGMENT;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.DO_DELETE_SEGMENT;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.JOB_SEGMENT;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_API_XML;
import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsClientException;

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
		client.post(CREATE_ITEM_SEGMENT + "?name=" + job.getName(), configuration);
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
		client.post(JOB_SEGMENT + "/" + job.getName() + DO_DELETE_SEGMENT);
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
		return client.getForObject(JOB_SEGMENT + "/" + name + SEGMENT_API_XML, Job.class);
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
		return client.getForObject(JOB_SEGMENT + "/" + job.getName() + "/" + "config.xml", JobConfiguration.class);
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
		client.post(JOB_SEGMENT + "/" + job.getName() + "/build");
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
		client.post(JOB_SEGMENT + "/" + job.getName() + "/buildWithParameters" + "?" + toQueryParams(parameters));
		return job.getNextBuildNumber();
	}

	/**
	 * Trigger a build of the given job and wait for its completion.
	 * 
	 * @param job
	 *            the job to be built
	 * @return build
	 * @throws IOException
	 *             if an error occurred connecting to Jenkins
	 * @since 1.0.0
	 */
	public Build triggerBuildAndWait(final Job job) throws IOException {
		final Long buildNumber = triggerBuild(job);
		PollBuildStatus poll = new PollBuildStatus(job, buildNumber);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Build> build = executor.submit(poll);
		try {
			return build.get();
		} catch (InterruptedException e) {
			throw new JenkinsClientException(e);
		} catch (ExecutionException e) {
			throw new JenkinsClientException(e);
		}
	}

	private class PollBuildStatus implements Callable<Build> {

		private final Job job;

		private final Long buildNumber;

		private PollBuildStatus(Job job, Long buildNumber) {
			this.job = job;
			this.buildNumber = buildNumber;
		}

		@Override
		public Build call() throws Exception {
			Build build = null;
			do {
				TimeUnit.SECONDS.sleep(1);
				build = getBuild(job, buildNumber);
				System.out.println("Got build: " + build);
			} while (build.getStatus() == Build.Status.PENDING);
			return build;
		}
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
		return client.getForObject(JOB_SEGMENT + "/" + job.getName() + "/" + number + "/api/xml", Build.class);
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