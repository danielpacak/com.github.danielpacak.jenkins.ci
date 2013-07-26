package com.danielpacak.jenkins.ci.core.service;

import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Job service class.
 * 
 * @since 1.0.0
 */
public class JobService {

	private final JenkinsClient client;
	private final HttpClient httpClient;

	public JobService(JenkinsClient client) {
		this.client = checkArgumentNotNull(client,
				"JenkinsClient cannot be null");
		this.httpClient = new HttpClient();
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
	public Job createJob(String name, JobConfiguration configuration)
			throws IOException {
		checkArgumentNotNull(name, "Name cannot be null");
		checkArgumentNotNull(configuration, "Configuration cannot be null");
		String url = "http://" + client.getHost() + ":" + client.getPort()
				+ "/createItem?name=" + name;

		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/xml");
		post.setRequestEntity(new InputStreamRequestEntity(configuration
				.getInputStream(), "application/xml"));

		int responseCode = httpClient.executeMethod(post);
		if (responseCode == 200) {
			Job created = new Job();
			created.setName(name);
			return created;
		}
		throw new IllegalStateException("Error while creating job " + name
				+ ". " + post.getResponseBodyAsString());
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
		String url = "http://" + client.getHost() + ":" + client.getPort()
				+ "/job/" + job.getName() + "/doDelete";
		PostMethod post = new PostMethod(url);
		int responseCode = httpClient.executeMethod(post);
		// TODO CHECK RESPONSE CODE
	}

	/**
	 * Return a job with the given name.
	 * 
	 * @param name
	 *            the name of the job
	 * @return job model class or <code>null</code> a job with the given name
	 *         doesn't exist
	 * @throws IOException
	 * @since 1.0.0
	 */
	public Job getJob(String name) throws IOException {
		throw new IllegalStateException("Not implemented yet");
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
		String url = "http://" + client.getHost() + ":" + client.getPort()
				+ "/job/" + job.getName() + "/build?delay=0sec";
		GetMethod get = new GetMethod(url);
		int responseCode = httpClient.executeMethod(get);
		System.out.println(responseCode);
		if (responseCode != 201) {
			throw new IllegalStateException("Error while triggerring build of "
					+ job.getName() + ". " + get.getResponseBodyAsString());
		}
	}

	/**
	 * Return the build of the given job.
	 * 
	 * @param job
	 *            job
	 * @param numbe
	 *            build number
	 * @return the build model or <code>null</code> if the build wasn't
	 *         triggered yet
	 * @since 1.0.0
	 */
	public Build getBuild(Job job, Long number) throws IOException {
		checkArgumentNotNull(job, "Job cannot be null");
		checkArgumentNotNull(number, "Number cannot be null");
		String url = "http://" + client.getHost() + ":" + client.getPort()
				+ "/job/" + job.getName() + "/" + number + "/api/xml";
		GetMethod get = new GetMethod(url);
		int responseCode = httpClient.executeMethod(get);

		if (responseCode == 200) {
			XmlResponse xmlResponse = new XmlResponse(
					get.getResponseBodyAsStream());
			Build build = new Build();
			build.setNumber(number);

			Boolean building = xmlResponse
					.evaluateAsBoolean("//building/text()");
			if (building) {
				build.setStatus(Build.Status.PENDING);
			} else {
				build.setStatus(Build.Status.valueOf(xmlResponse
						.evaluateAsString("//result/text()")));
			}

			return build;

		}
		throw new IllegalStateException("Error while getting build " + number);
	}

}
