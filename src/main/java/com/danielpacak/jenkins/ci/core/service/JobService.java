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

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsResponse;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Job service class.
 * 
 * @since 1.0.0
 */
public class JobService extends AbstractService {

	public JobService() {
		super();
	}

	public JobService(JenkinsClient client) {
		super(client);
	}

	public List<Job> getJobs() throws IOException {
		throw new IllegalStateException("Not implemented yet");
	}

	/**
	 * Create a new job with the given name and configuration.
	 * 
	 * @param name
	 *            the name of the job
	 * @param configuration
	 *            the configuration of the job
	 * @return the job that has been created
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
	 * @since 1.0.0
	 */
	public void deleteJob(Job job) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(job.getName(), "Job.name cannot be null");
		client.post(JOB_SEGMENT + "/" + job.getName() + DO_DELETE_SEGMENT);
	}

	/**
	 * Return a job with the given name.
	 * 
	 * @param name
	 *            the name of the job
	 * @return job model class or <code>null</code> a job with the given name doesn't exist
	 * @throws IOException
	 * @since 1.0.0
	 */
	public Job getJob(String name) throws IOException {
		checkArgumentNotNull(name, "Name cannot be null");
		JenkinsResponse response = client.get(JOB_SEGMENT + "/" + name + SEGMENT_API_XML);
		return response.getModel(new JobResponseMapper());
	}

	/**
	 * Trigger a build of the given job.
	 * 
	 * @param job
	 *            the job to be built.
	 * @since 1.0.0
	 */
	public void triggerBuild(Job job) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(job.getName(), "Job.name cannot be null");
		client.get(JOB_SEGMENT + "/" + job.getName() + "/build?delay=0sec");
	}

	/**
	 * Return the build of the given job.
	 * 
	 * @param job
	 *            job
	 * @param numbe
	 *            build number
	 * @return the build model or <code>null</code> if the build wasn't triggered yet
	 * @since 1.0.0
	 */
	public Build getBuild(Job job, Long number) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(number, "Number cannot be null");
		JenkinsResponse response = client.get(JOB_SEGMENT + "/" + job.getName() + "/" + number + "/api/xml");
		return response.getModel(new BuildConverter());
	}

	// XmlResponseMapper or ResponseMapper -> maps xml respone to an instance of a model class
	private class BuildConverter implements ResponseMapper<Build> {
		@Override
		public Build map(XmlResponse xmlResponse) {
			Build build = new Build();
			build.setNumber(new Long(3));

			Boolean building = xmlResponse.evaluateAsBoolean("//building/text()");
			if (building) {
				build.setStatus(Build.Status.PENDING);
			} else {
				build.setStatus(Build.Status.valueOf(xmlResponse.evaluateAsString("//result/text()")));
			}

			return build;
		}
	}

}
