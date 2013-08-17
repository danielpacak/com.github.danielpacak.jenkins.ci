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

import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;

/**
 * Tests for {@link JobArrayHttpMessageConverter}.
 */
public class JobArrayHttpMessageConverterTest {

	private JobArrayHttpMessageConverter converter;

	@Before
	public void beforeTest() {
		converter = new JobArrayHttpMessageConverter();
	}

	@Test
	public void canRead() throws Exception {
		assertTrue(converter.canRead(Job[].class));
		assertFalse(converter.canRead(Job.class));
		assertFalse(converter.canRead(Object.class));
	}

	@Test
	public void canWrite() throws Exception {
		assertFalse(converter.canWrite(Job[].class));
		assertFalse(converter.canWrite(Job.class));
		assertFalse(converter.canWrite(Object.class));
	}

	@Test
	public void read() throws Exception {
		// @formatter:off
		HttpInputMessage inputMessage = new MockHttpInputMessage(""
			+	"<hudson>"
			+		"<mode>NORMAL</mode>"
			+		"<nodeDescription>Node description</nodeDescription>"
			+		"<nodeName/>"
			+		"<numExecutors>2</numExecutors>"
			+		"<useSecurity>false</useSecurity>"
			+		"<job>"
			+			"<name>vacuum.my.room</name>"
			+			"<displayName>Vacuum my room</displayName>"
			+			"<url>http://localhost:8080/job/vacuum.my.room</url>"
			+			"<buildable>true</buildable>"
			+			"<inQueue>false</inQueue>"
			+			"<nextBuildNumber>69</nextBuildNumber>"
			+		"</job>"
			+		"<job>"
			+			"<name>do.shopping</name>"
			+			"<displayName>Do shopping</displayName>"
			+			"<url>http://localhost:8080/job/do.shopping</url>"
			+			"<buildable>false</buildable>"
			+			"<inQueue>true</inQueue>"
			+			"<nextBuildNumber>13</nextBuildNumber>"
			+		"</job>"
			+	"</hudson>"
		);
		// @formatter:on

		Job[] jobs = converter.read(Job[].class, inputMessage);

		assertEquals("vacuum.my.room", jobs[0].getName());
		assertEquals("Vacuum my room", jobs[0].getDisplayName());
		assertEquals("http://localhost:8080/job/vacuum.my.room", jobs[0].getUrl());
		assertTrue(jobs[0].getBuildable());
		assertFalse(jobs[0].getInQueue());
		assertEquals(new Long(69), jobs[0].getNextBuildNumber());

		assertEquals("do.shopping", jobs[1].getName());
		assertEquals("Do shopping", jobs[1].getDisplayName());
		assertEquals("http://localhost:8080/job/do.shopping", jobs[1].getUrl());
		assertFalse(jobs[1].getBuildable());
		assertTrue(jobs[1].getInQueue());
		assertEquals(new Long(13), jobs[1].getNextBuildNumber());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void write() throws Exception {
		converter.write(new Job[] {}, null, null);
	}

}
