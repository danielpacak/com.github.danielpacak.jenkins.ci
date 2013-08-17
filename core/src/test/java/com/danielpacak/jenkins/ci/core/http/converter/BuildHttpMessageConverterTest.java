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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;

/**
 * Tests for {@link BuildHttpMessageConverter}.
 */
public class BuildHttpMessageConverterTest {

   private BuildHttpMessageConverter converter;

   @Before
   public void beforeTest() {
      this.converter = new BuildHttpMessageConverter();
   }

   @Test
   public void canRead() throws Exception {
      assertTrue(converter.canRead(Build.class));
      assertFalse(converter.canRead(Build[].class));
      assertFalse(converter.canRead(Object.class));
   }

   @Test
   public void canWrite() throws Exception {
      assertFalse(converter.canWrite(Build.class));
      assertFalse(converter.canWrite(Build[].class));
      assertFalse(converter.canWrite(Object.class));
   }

   @Test
   public void read_WithSuccessfulBuild() throws Exception {
      // @formatter:off
		HttpInputMessage inputMessage = new MockHttpInputMessage(""
			+	"<freeStyleBuild>"
			+		"<building>false</building>"
			+		"<duration>1450</duration>"
			+		"<number>6</number>"
			+		"<result>SUCCESS</result>"
			+		"<url>http://localhost:8080/job/vacuum.my.room/6</url>"
			+	"</freeStyleBuild>"
		);
		// @formatter:on

      Build build = converter.read(Build.class, inputMessage);
      assertEquals(new Long(6), build.getNumber());
      assertEquals(Build.Status.SUCCESS, build.getStatus());
      assertEquals(new Long(1450), build.getDuration());
      assertEquals("http://localhost:8080/job/vacuum.my.room/6", build.getUrl());
   }

   @Test
   public void read_WithPendingBuild() throws Exception {
      // @formatter:off
		HttpInputMessage inputMessage = new MockHttpInputMessage(""
			+	"<freeStyleBuild>"
			+		"<building>true</building>"
			+		"<duration>150</duration>"
			+		"<number>45</number>"
			+		"<url>http://localhost:8080/job/vacuum.my.room/6</url>"
			+	"</freeStyleBuild>"
		);
		// @formatter:on

      Build build = converter.read(Build.class, inputMessage);
      assertEquals(new Long(45), build.getNumber());
      assertEquals(Build.Status.PENDING, build.getStatus());
      assertEquals(new Long(150), build.getDuration());
      assertEquals("http://localhost:8080/job/vacuum.my.room/6", build.getUrl());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void write() throws Exception {
      converter.write(new Build(), null, null);
   }

}
