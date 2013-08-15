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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

public class JobResponseMapperTest {

	private JobResponseMapper mapper;

	@Before
	public void beforeTest() {
		mapper = new JobResponseMapper();
	}

	@Test
	public void testMap() throws Exception {
		// @formatter:off
		XmlResponse response = new XmlResponse(""
			+	"<freeStyleProject>"
			+		"<name>vacuum.my.room</name>"
			+		"<displayName>Vacuum my room</displayName>"
			+	"</freeStyleProject>");
		// @formatter:on

		Job job = mapper.map(response);
		Assert.assertEquals("vacuum.my.room", job.getName());
		Assert.assertEquals("Vacuum my room", job.getDisplayName());
	}

	@Test
	public void testMapWithParameters() throws Exception {
		// @formatter:off
		XmlResponse response = new XmlResponse(""
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

		Job job = mapper.map(response);
		Assert.assertEquals("vacuum.my.room", job.getName());
		Assert.assertEquals("Vacuum my room", job.getDisplayName());
	}

}
