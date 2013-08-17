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
package com.danielpacak.jenkins.ci.core.http.converter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.danielpacak.jenkins.ci.core.util.Streams;

/**
 * Tests for {@link JobConfigurationHttpMessageConverter}.
 */
public class JobConfigurationHttpMessageConverterTest {

   private JobConfigurationHttpMessageConverter converter;

   @Before
   public void beforeTest() {
      converter = new JobConfigurationHttpMessageConverter();
   }

   @Test
   public void canRead() throws Exception {
      assertTrue(converter.canRead(JobConfiguration.class));
      assertFalse(converter.canRead(JobConfiguration[].class));
      assertFalse(converter.canRead(Object.class));
   }

   @Test
   public void canWrite() throws Exception {
      assertTrue(converter.canWrite(JobConfiguration.class));
      assertTrue(converter.canWrite(ClassPathJobConfiguration.class));
      assertFalse(converter.canWrite(Object.class));
   }

   @Test
   public void read() throws Exception {
      HttpInputMessage inputMessage = new MockHttpInputMessage("<freeStyleProject></freeStyleProject>");
      JobConfiguration jobConfiguration = converter.read(JobConfiguration.class, inputMessage);
      Assert.assertEquals("<freeStyleProject></freeStyleProject>", Streams.toString(jobConfiguration.getInputStream()));
   }

}
