package com.danielpacak.jenkins.ci.core.service;

import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

/**
 * Job service class.
 * 
 * @since 1.0.0
 * 
 */
public class JobService {

	private final JenkinsClient client;
	private final HttpClient httpClient;

	public JobService(JenkinsClient client) {
		this.client = client;
		this.httpClient = new HttpClient();
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

	public Build getLastBuild(Job job) {
		throw new IllegalStateException("Not implemented yet");
	}

}
