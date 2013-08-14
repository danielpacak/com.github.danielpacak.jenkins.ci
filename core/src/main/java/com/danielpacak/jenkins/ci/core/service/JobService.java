package com.danielpacak.jenkins.ci.core.service;

import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.CREATE_ITEM_SEGMENT;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.DO_DELETE_SEGMENT;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.HEADER_CONTENT_TYPE;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.JOB_SEGMENT;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_API_XML;
import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsResponse;

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
		JenkinsResponse response = client.get(SEGMENT_API_XML + "?depth=2");
		return response.getModel(new JobListResponseMapper());
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
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HEADER_CONTENT_TYPE, "application/xml");
		client.post(CREATE_ITEM_SEGMENT + "?name=" + job.getName(), headers, configuration.getInputStream());
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
		checkArgumentNotNull(name, "Name cannot be null");
		JenkinsResponse response = client.get(JOB_SEGMENT + "/" + name + SEGMENT_API_XML);
		return response.getModel(new JobResponseMapper());
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
		JenkinsResponse response = client.get(JOB_SEGMENT + "/" + job.getName() + "/" + "config.xml");
		// i need something that allows me to get raw bytes
		return null;
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
		client.get(JOB_SEGMENT + "/" + job.getName() + "/build");
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
		return null;
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
		JenkinsResponse response = client.get(JOB_SEGMENT + "/" + job.getName() + "/" + number + "/api/xml");
		return response.getModel(new BuildResponseMapper());
	}

	private String toQueryParams(Map<String, String> parameters) {
		StringBuilder queryParams = new StringBuilder();
		for (Entry<String, String> entry : parameters.entrySet()) {
			queryParams.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		queryParams.deleteCharAt(0); // remove trailing &
		return queryParams.toString();
	}

}