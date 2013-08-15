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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.danielpacak.jenkins.ci.core.http.converter.JobHttpMessageConverter;

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
	public void testRead() throws Exception {
		// @formatter:off
		HttpInputMessage inputMessage = new TestInputMessage(""
			+	"<freeStyleProject>"
			+		"<name>vacuum.my.room</name>"
			+		"<displayName>Vacuum my room</displayName>"
			+	"</freeStyleProject>");
		// @formatter:on

		Job job = converter.read(Job.class, inputMessage);

		assertEquals("vacuum.my.room", job.getName());
		assertEquals("Vacuum my room", job.getDisplayName());
	}

	@Test
	public void testReadWithParameters() throws Exception {
		// @formatter:off
		HttpInputMessage inputMessage = new TestInputMessage(""
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

	class TestInputMessage implements HttpInputMessage {

		String xml;

		TestInputMessage(String xml) {
			this.xml = xml;
		}

		@Override
		public HttpHeaders getHeaders() {
			return new HttpHeaders();
		}

		@Override
		public InputStream getBody() throws IOException {
			return new ByteArrayInputStream(xml.getBytes());
		}
	}

}
