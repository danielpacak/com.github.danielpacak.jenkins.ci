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
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_CONFIG_XML;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_CREATE_ITEM;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_DO_DELETE;
import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_JOB;
import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;
import static java.util.Arrays.asList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.HttpClientErrorException;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsClientException;
import com.danielpacak.jenkins.ci.core.http.HttpStatus;

/**
 * Job service class.
 * 
 * @since 1.0.0
 */
public class JobService extends AbstractService {

   /**
    * Create a new job service for the {@link JenkinsClient#JenkinsClient() default} client.
    * 
    * @since 1.0.0
    */
   public JobService() {
      super();
   }

   /**
    * Create a new job service for the given client.
    * 
    * @param client the client
    * @throws IllegalArgumentException if the client is {@code null}
    * @since 1.0.0
    */
   public JobService(JenkinsClient client) {
      super(client);
   }

   /**
    * Get all jobs.
    * 
    * @return list of jobs
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @since 1.0.0
    */
   public List<Job> getJobs() {
      return asList(client().getForObject(SEGMENT_API_XML + "?depth=2", Job[].class));
   }

   /**
    * Create a new job with the given name and configuration.
    * 
    * @param name the name of the job
    * @param configuration the configuration of the job
    * @return the job that has been created
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if the name of the job or job configuration is {@code null}
    * @since 1.0.0
    **/
   public Job createJob(String name, JobConfiguration configuration) {
      checkArgumentNotNull(name, "Name cannot be null");
      checkArgumentNotNull(configuration, "JobConfiguration cannot be null");
      client().postForLocation(SEGMENT_CREATE_ITEM + "?name=" + name, configuration);
      return getJob(name);
   }

   /**
    * Update a job with the the given name with the given configuration.
    * 
    * @param name the name of the job to be updated
    * @param configuration the configuration of the job
    * @return the job that has been updated
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if the name of the job or job configuration is {@code null}
    * @since 1.0.0
    */
   public Job updateJob(String name, JobConfiguration configuration) {
      checkArgumentNotNull(name, "Name cannot be null");
      checkArgumentNotNull(configuration, "JobConfiguration cannot be null");
      client().postForLocation(SEGMENT_JOB + "/" + name + SEGMENT_CONFIG_XML, configuration);
      return getJob(name);
   }

   /**
    * Update the given job with the given configuration.
    * 
    * @param job the job to be updated
    * @param configuration the configuration of the job
    * @return the job that has been updated
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if the job or the name of the job, or job configuration is {@code null}
    * @since 1.0.0
    */
   public Job updateJob(Job job, JobConfiguration configuration) {
      return updateJob(job.getName(), configuration);
   }

   /**
    * Delete a job with the given name.
    * 
    * @param name the name of the job to be deleted
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if the name of the job is {@code null}
    * @since 1.0.0
    */
   public void deleteJob(String name) {
      checkArgumentNotNull(name, "Name cannot be null");
      client().postForLocation(SEGMENT_JOB + "/" + name + SEGMENT_DO_DELETE, null);
   }

   /**
    * Delete the given job.
    * 
    * @param job the job to be deleted
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if the job or the name of the job is {@code null}
    * @since 1.0.0
    */
   public void deleteJob(Job job) {
      checkArgumentNotNull(job, "Job cannot be null");
      deleteJob(job.getName());
   }

   /**
    * Get a job with the given name.
    * 
    * @param name the name of the job
    * @return job or {@code null} if a job with the given name does not exist
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if name is {@code null}
    * @since 1.0.0
    */
   public Job getJob(String name) {
      checkArgumentNotNull(name, "Name cannot be null");
      try {
         return client().getForObject(SEGMENT_JOB + "/" + name + SEGMENT_API_XML, Job.class);
      } catch (HttpClientErrorException e) {
         if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
            return null;
         }
         throw e;
      }
   }

   /**
    * Get the configuration of the given job.
    * 
    * @param job the job
    * @return job configuration or {@code null} if a job with the given name does not exist
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if job or the name of the job is {@code null}
    * @since 1.0.0
    */
   public JobConfiguration getJobConfiguration(Job job) {
      checkArgumentNotNull(job, "Job cannot be null");
      return getJobConfiguration(job.getName());
   }

   /**
    * Get the configuration of a job with the given name.
    * 
    * @param name the name of the job
    * @return job configuration or {@code null} if a job with the given name does not exist
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if the name of the job is {@code null}
    * @since 1.0.0
    */
   public JobConfiguration getJobConfiguration(String name) {
      checkArgumentNotNull(name, "Name cannot be null");
      return client().getForObject(SEGMENT_JOB + "/" + name + SEGMENT_CONFIG_XML, JobConfiguration.class);
   }

   /**
    * Trigger a build of the given job.
    * 
    * @param job the job to be built
    * @return build number
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if job or the name of the job is {@code null}
    * @since 1.0.0
    */
   public Long triggerBuild(Job job) {
      checkArgumentNotNull(job, "Job cannot be null");
      checkArgumentNotNull(job.getName(), "Job.name cannot be null");
      client().postForLocation(SEGMENT_JOB + "/" + job.getName() + "/build", null);
      return job.getNextBuildNumber();
   }

   /**
    * Trigger a build of the given job with the given parameters.
    * 
    * @param job the job to be built
    * @param parameters the build parameters
    * @return build number
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if job or the name of the job, or parameters is {@code null}
    * @since 1.0.0
    */
   public Long triggerBuild(Job job, Map<String, Object> parameters) {
      checkArgumentNotNull(job, "Job cannot be null");
      checkArgumentNotNull(job.getName(), "Job.name cannot be null");
      checkArgumentNotNull(parameters, "Parameters cannot be null");
      client().postForLocation(
            SEGMENT_JOB + "/" + job.getName() + "/buildWithParameters" + "?" + toQueryString(parameters), null);
      return job.getNextBuildNumber();
   }

   /**
    * Get a build of the given job.
    * 
    * @param job the job
    * @param buildNumber the build number
    * @return build or {@code null} if the build does not exist
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if job or the name of the job, or build number is {@code null}
    * @since 1.0.0
    */
   public Build getBuild(Job job, Long buildNumber) {
      checkArgumentNotNull(job, "Job cannot be null");
      return getBuild(job.getName(), buildNumber);
   }

   /**
    * Get a build of a job with the given name.
    * 
    * @param name the name of the job
    * @param buildNumber the build number
    * @return build or {@code null} if the build does not exist
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if the name of the job or build number is {@code null}
    * @since 1.0.0
    */
   public Build getBuild(String name, Long buildNumber) {
      checkArgumentNotNull(name, "Name cannot be null");
      checkArgumentNotNull(buildNumber, "BuildNumber cannot be null");
      try {
         return client().getForObject(SEGMENT_JOB + "/" + name + "/" + buildNumber + SEGMENT_API_XML, Build.class);
      } catch (HttpClientErrorException e) {
         if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
            return null;
         }
         throw e;
      }
   }

   private String toQueryString(Map<String, Object> parameters) {
      StringBuilder queryParams = new StringBuilder();
      for (Entry<String, Object> entry : parameters.entrySet()) {
         queryParams.append("&").append(entry.getKey()).append("=").append(encode(entry.getValue()));
      }
      queryParams.deleteCharAt(0);
      return queryParams.toString();
   }

   private String encode(Object value) {
      try {
         return URLEncoder.encode(String.valueOf(value), "UTF-8");
      } catch (UnsupportedEncodingException e) {
         throw new IllegalStateException(e);
      }
   }

}