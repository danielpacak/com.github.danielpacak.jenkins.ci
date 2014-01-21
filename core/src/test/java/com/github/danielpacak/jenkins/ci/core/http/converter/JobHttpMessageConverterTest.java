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
package com.github.danielpacak.jenkins.ci.core.http.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.danielpacak.jenkins.ci.core.Job;
import com.github.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.github.danielpacak.jenkins.ci.core.http.converter.JobHttpMessageConverter;

/**
 * Tests for {@link JobHttpMessageConverter}.
 */
public class JobHttpMessageConverterTest {

   private JobHttpMessageConverter converter;

   @Before
   public void beforeTest() {
      converter = new JobHttpMessageConverter();
   }

   @Test
   public void canRead() throws Exception {
      Assert.assertTrue(converter.canRead(Job.class));
      Assert.assertFalse(converter.canRead(String.class));
      Assert.assertFalse(converter.canRead(Object.class));
      Assert.assertFalse(converter.canRead(Job[].class));
   }

   @Test
   public void canWrite() throws Exception {
      Assert.assertFalse(converter.canWrite(Job.class));
      Assert.assertFalse(converter.canWrite(Job[].class));
      Assert.assertFalse(converter.canWrite(Object.class));
   }

   @Test
   public void read_SimpleJob() throws Exception {
      // @formatter:off
		HttpInputMessage inputMessage = new MockHttpInputMessage(""
			+	"<freeStyleProject>"
			+		"<name>vacuum.my.room</name>"
			+		"<displayName>Vacuum my room</displayName>"
			+		"<url>http://localhost:8080/job/vacuum.my.room</url>"
			+		"<buildable>true</buildable>"
			+		"<nextBuildNumber>23</nextBuildNumber>"
			+		"<inQueue>false</inQueue>"
			+	"</freeStyleProject>");
		// @formatter:on

      Job job = converter.read(Job.class, inputMessage);

      assertEquals("vacuum.my.room", job.getName());
      assertEquals("Vacuum my room", job.getDisplayName());
      assertEquals("http://localhost:8080/job/vacuum.my.room", job.getUrl());
      assertTrue(job.getBuildable());
      assertEquals(new Long(23), job.getNextBuildNumber());
      assertFalse(job.getInQueue());
      assertNull(job.getLastBuildNumber());
   }

   @Test
   public void read_JobWithParameters() throws Exception {
      // @formatter:off
		HttpInputMessage inputMessage = new MockHttpInputMessage(""
			+	"<freeStyleProject>"
			+		"<action>"
			+			"<parameterDefinition>"
			+				"<defaultParameterValue>"
			+					"<value>Daniel</value>"
			+				"</defaultParameterValue>"
			+				"<description/>"
			+				"<name>FIRST_NAME</name>"
			+				"<type>StringParameterDefinition</type>"
			+			"</parameterDefinition>"
			+		"</action>"
			+		"<name>vacuum.my.room</name>"
			+		"<displayName>Vacuum my room</displayName>"
			+	"</freeStyleProject>");
		// @formatter:on

      Job job = converter.read(Job.class, inputMessage);
      assertEquals("vacuum.my.room", job.getName());
      assertEquals("Vacuum my room", job.getDisplayName());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void write() throws Exception {
      converter.write(new Job(), null, null);
   }

   @Test
   public void read_JobWithLastBuild() throws IOException {
	   // @formatter:off
	   HttpInputMessage inputMessage = new MockHttpInputMessage(""
			+	"<freeStyleProject>"
			+		"<name>vacuum.my.room</name>"
			+		"<displayName>Vacuum my room</displayName>"
			+		"<url>http://localhost:8080/job/vacuum.my.room</url>"
			+		"<buildable>true</buildable>"
			+		"<nextBuildNumber>23</nextBuildNumber>"
			+		"<inQueue>false</inQueue>"
			+		"<lastBuild>"
			+			"<number>42</number>"
			+			"<url>http://localhost:8080/job/vacuum.my.room/42/</url>"
			+		"</lastBuild>"
			+	"</freeStyleProject>");
	   // @formatter:on
	   Job job = converter.read(Job.class, inputMessage);
	   assertEquals(Long.valueOf(42L), job.getLastBuildNumber());
   }

}
