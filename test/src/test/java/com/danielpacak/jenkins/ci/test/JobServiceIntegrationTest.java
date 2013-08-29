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
package com.danielpacak.jenkins.ci.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.HttpClientErrorException;
import com.danielpacak.jenkins.ci.core.service.JobService;

/**
 * Integration tests for {@link JobService}.
 */
public class JobServiceIntegrationTest extends AbstractJenkinsIntegrationTest {

   @Rule
   public ExpectedException thrown = ExpectedException.none();

   private static final int WAIT_FOR_BUILD_COMPLETION_TIMEOUT = 10;

   private JobService jobService;

   @Before
   public void beforeTest() {
      jobService = new JobService(getJenkinsClient());
   }

   @Test
   public void createSimpleProjectAndTriggerBuild() throws Exception {
      String name = newRandomName();
      JobConfiguration config = new ClassPathJobConfiguration("job/config/free-style.xml");

      Job job = jobService.createJob(name, config);
      Long buildNumber = jobService.triggerBuild(job);

      TimeUnit.SECONDS.sleep(WAIT_FOR_BUILD_COMPLETION_TIMEOUT);

      Build build = jobService.getBuild(job, buildNumber);
      assertEquals(Build.Status.SUCCESS, build.getStatus());
      assertEquals(buildNumber, build.getNumber());
      jobService.deleteJob(job);
   }

   @Test
   public void createParameterizedJobAndTriggerBuild() throws Exception {
      JobConfiguration jobConfiguration = new ClassPathJobConfiguration("job/config/free-style-parameterized.xml");

      String name = newRandomName();
      Job job = jobService.createJob(name, jobConfiguration);

      Map<String, Object> parameters = new HashMap<String, Object>();
      // Override default parameters values stored along with the job configuration.
      parameters.put("FIRST_NAME", "Mike");
      parameters.put("LAST_NAME", "Tyson");
      parameters.put("IS_SMART", true);
      parameters.put("SECRET_PASSWORD", "cypher");

      Long buildNumber = jobService.triggerBuild(job, parameters);
      // TODO How to assert that the build was triggered with the given parameters? Get output and search for text
      // pattern?

      TimeUnit.SECONDS.sleep(WAIT_FOR_BUILD_COMPLETION_TIMEOUT);
      Build build = jobService.getBuild(job, buildNumber);
      assertEquals(Build.Status.SUCCESS, build.getStatus());
      assertEquals(buildNumber, build.getNumber());
      jobService.deleteJob(job);
   }

   @Test
   public void updateJobConfiguration() throws Exception {
      String name = newRandomName();
      JobConfiguration configuration = new ClassPathJobConfiguration("job/config/free-style.xml");
      Job job = jobService.createJob(name, configuration);

      JobConfiguration newConfiguration = new ClassPathJobConfiguration("job/config/free-style-parameterized.xml");
      jobService.updateJob(job, newConfiguration);

      jobService.deleteJob(job);
   }

   @Test
   public void getJobs() throws Exception {
      List<Job> jobs = jobService.getJobs();
      assertTrue(jobs.isEmpty());

      String name = newRandomName();
      JobConfiguration configuration = new ClassPathJobConfiguration("job/config/free-style.xml");
      Job job = jobService.createJob(name, configuration);

      jobs = jobService.getJobs();
      assertEquals(1, jobs.size());

      jobService.deleteJob(job);
   }

   @Test
   public void getJob_WithNonExistingJob_ReturnsNull() throws Exception {
      assertNull(jobService.getJob("non.existing.job"));
   }

   @Test
   public void getBuild_WithNonExistingBuild_ReturnsNull() throws Exception {
      String name = newRandomName();
      JobConfiguration configuration = new ClassPathJobConfiguration("job/config/free-style.xml");
      Job job = jobService.createJob(name, configuration);

      assertNull(jobService.getBuild(job, new Long(72)));

      jobService.deleteJob(job);
   }

   @Test
   public void getBuild_WithNonExistingJob_ReturnsNull() throws Exception {
      Job job = new Job().setName("non.existing.job");

      assertNull(jobService.getBuild(job, new Long(72)));
   }

   @Test
   public void createJobCalledTwice() throws Exception {
      String name = newRandomName();
      JobConfiguration configuration = new ClassPathJobConfiguration("job/config/free-style.xml");
      Job job = jobService.createJob(name, configuration);

      try {
         jobService.createJob(name, configuration);
      } catch (HttpClientErrorException expected) {

      }
      jobService.deleteJob(job);
   }

   @Test
   public void triggerBuildAndWait() throws Exception {
      String name = newRandomName();
      JobConfiguration configuration = new ClassPathJobConfiguration("job/config/free-style.xml");

      Job job = jobService.createJob(name, configuration);

      Build build = jobService.triggerBuildAndWait(job);
      assertEquals(Build.Status.SUCCESS, build.getStatus());
   }

}
