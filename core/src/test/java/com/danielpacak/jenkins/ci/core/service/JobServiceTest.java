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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.client.HttpClientErrorException;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.http.HttpStatus;

/**
 * Tests for {@link JobService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {

   @Mock
   private JenkinsClient client;

   @Mock
   private JobConfiguration jobConfiguration;

   private JobService service;

   @Before
   public void beforeTest() throws Exception {
      service = new JobService(client);
   }

   @Test(expected = IllegalArgumentException.class)
   public void createJob_WithNullName_ThrowsException() throws Exception {
      service.createJob(null, jobConfiguration);
   }

   @Test(expected = IllegalArgumentException.class)
   public void createJob_WithNullJobConfiguration_ThrowsException() throws Exception {
      service.createJob("vacuum.my.room", null);
   }

   @Test
   public void createJob() throws Exception {
      service.createJob("vacuum.my.room", jobConfiguration);
      verify(client).postForLocation("/createItem?name=vacuum.my.room", jobConfiguration);
   }

   @Test(expected = IllegalArgumentException.class)
   public void deleteJob_WithNullJob_ThrowsException() throws Exception {
      service.deleteJob((Job) null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void deleteJob_WithNullJobName_ThrowsException() throws Exception {
      service.deleteJob(new Job());
   }

   @Test
   public void deleteJob() throws Exception {
      Job job = new Job().setName("j");
      service.deleteJob(job);
      verify(client).postForLocation("/job/j/doDelete", null);
   }

   @Test
   public void getJobs() throws Exception {
      Job[] someJobs = new Job[] {};
      when(client.getForObject("/api/xml?depth=2", Job[].class)).thenReturn(someJobs);
      assertEquals(Arrays.asList(someJobs), service.getJobs());
   }

   @Test(expected = IllegalArgumentException.class)
   public void getJobConfiguration_WithNullJob_ThrowsException() throws Exception {
      service.getJobConfiguration((Job) null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void getJobConfiguration_WithNullJobName_ThrowsException() throws Exception {
      service.getJobConfiguration(new Job());
   }

   @Test
   public void getJobConfiguration() throws Exception {
      Job job = new Job().setName("vacuum.my.room");
      when(client.getForObject("/job/vacuum.my.room/config.xml", JobConfiguration.class)).thenReturn(jobConfiguration);
      assertEquals(jobConfiguration, service.getJobConfiguration(job));
   }

   @Test(expected = IllegalArgumentException.class)
   public void triggerBuild_WithNullJob_ThrowsException() throws Exception {
      service.triggerBuild(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void triggerbuild_WithNullJobName_ThrowsException() throws Exception {
      service.triggerBuild(new Job());
   }

   @Test
   public void triggerBuild() throws Exception {
      Job job = new Job().setName("vacuum.my.room").setNextBuildNumber(new Long(23));
      Long buildNumber = service.triggerBuild(job);
      verify(client).postForLocation("/job/vacuum.my.room/build", null);
      assertEquals(new Long(23), buildNumber);
   }

   @Test(expected = IllegalArgumentException.class)
   public void triggerParameterizedBuild_withNullJob_throwsException() throws Exception {
      service.triggerBuild(null, new HashMap<String, Object>());
   }

   @Test(expected = IllegalArgumentException.class)
   public void triggerParameterizedBuild_withNullJobName_throwsException() throws Exception {
      service.triggerBuild(new Job(), new HashMap<String, Object>());
   }

   @Test(expected = IllegalArgumentException.class)
   public void triggerParameterizedBuild_withNullParameters_throwsException() throws Exception {
      service.triggerBuild(new Job().setName("vacuum.my.room"), null);
   }

   @Test
   public void triggerParameterizedBuild() throws Exception {
      Job job = new Job().setName("vacuum.my.room").setNextBuildNumber(new Long(69));
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("param1", "value1");
      parameters.put("param2", "value 2");
      parameters.put("param3", true);
      parameters.put("param4", 69);
      parameters.put("param5", "The string ü@foo-bar");

      Long buildNumber = service.triggerBuild(job, parameters);

      verify(client)
            .postForLocation(
                  "/job/vacuum.my.room/buildWithParameters?param1=value1&param2=value+2&param3=true&param4=69&param5=The+string+%C3%BC%40foo-bar",
                  null);
      assertEquals(new Long(69), buildNumber);
   }

   @Test(expected = IllegalArgumentException.class)
   public void getBuild_WithNullJob_ThrowsException() throws Exception {
      service.getBuild((Job) null, new Long(76));
   }

   @Test(expected = IllegalArgumentException.class)
   public void getBuild_WithNullJobName_ThrowsException() throws Exception {
      service.getBuild(new Job(), new Long(76));
   }

   @Test(expected = IllegalArgumentException.class)
   public void getBuild_WithNullBuildNumber_ThrowsException() throws Exception {
      service.getBuild(new Job().setName("vacuum.my.room"), null);
   }

   @Test
   public void getBuild() throws Exception {
      Job job = new Job().setName("vacuum.my.room");
      Build someBuild = new Build();
      when(client.getForObject("/job/vacuum.my.room/69/api/xml", Build.class)).thenReturn(someBuild);
      assertEquals(someBuild, service.getBuild(job, new Long(69)));
   }

   @Test
   public void getBuild_With404Response_ReturnsNull() throws Exception {
      Job job = new Job().setName("open.the.window");
      HttpClientErrorException notFoundError = new HttpClientErrorException(HttpStatus.NOT_FOUND);
      when(client.getForObject("/job/open.the.window/72/api/xml", Build.class)).thenThrow(notFoundError);
      assertNull(service.getBuild(job, new Long(72)));
   }

   @Test
   public void getBuild_With403Response_ThrowsException() throws Exception {
      Job job = new Job().setName("close.the.window");
      HttpClientErrorException forbiddenError = new HttpClientErrorException(HttpStatus.FORBIDDEN);
      when(client.getForObject("/job/close.the.window/77/api/xml", Build.class)).thenThrow(forbiddenError);
      try {
         service.getBuild(job, new Long(77));
      } catch (HttpClientErrorException expected) {
         assertEquals(forbiddenError, expected);
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void getJob_WithNullName_ThrowsException() throws Exception {
      service.getJob(null);
   }

   @Test
   public void getJob() throws Exception {
      Job job = new Job().setName("commit.your.changes");
      when(client.getForObject("/job/commit.your.changes/api/xml", Job.class)).thenReturn(job);
      assertEquals(job, service.getJob("commit.your.changes"));
   }

   @Test
   public void getJob_With404Response_ReturnsNull() throws Exception {
      HttpClientErrorException notFoundError = new HttpClientErrorException(HttpStatus.NOT_FOUND);
      when(client.getForObject("/job/paint.the.wall/api/xml", Job.class)).thenThrow(notFoundError);
      assertNull(service.getJob("paint.the.wall"));
   }

   @Test
   public void getJob_With403Response_ThrowsException() throws Exception {
      HttpClientErrorException forbiddenError = new HttpClientErrorException(HttpStatus.FORBIDDEN);
      when(client.getForObject("/job/paint.the.wall/api/xml", Job.class)).thenThrow(forbiddenError);
      try {
         service.getJob("paint.the.wall");
      } catch (HttpClientErrorException expected) {
         assertEquals(forbiddenError, expected);
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void updateJob_WithNullName_ThrowsException() throws Exception {
      service.updateJob((String) null, jobConfiguration);
   }

   @Test(expected = IllegalArgumentException.class)
   public void updateJob_WithNullJobName_ThrowsException() throws Exception {
      service.updateJob(new Job(), jobConfiguration);
   }

   @Test(expected = IllegalArgumentException.class)
   public void updateJob_WithNullJobConfiguration_ThrowsException() throws Exception {
      service.updateJob("my.job", null);

   }

   @Test
   public void updateJob() throws Exception {
      service.updateJob("my.job", jobConfiguration);
      verify(client).postForLocation("/job/my.job/config.xml", jobConfiguration);

   }

}
