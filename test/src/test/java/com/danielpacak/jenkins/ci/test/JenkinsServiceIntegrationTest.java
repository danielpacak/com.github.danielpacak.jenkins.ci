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

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Jenkins;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.service.JenkinsService;

public class JenkinsServiceIntegrationTest extends AbstractJenkinsIntegrationTest {

	@Test
	public void testGetJenkins() throws Exception {
		JenkinsClient client = new JenkinsClient("localhost", 8080);
		JenkinsService service = new JenkinsService(client);
		Jenkins jenkins = service.getJenkins();
		assertEquals(Jenkins.MODE.NORMAL, jenkins.getMode());
		assertEquals(new Integer(2), jenkins.getNumExecutors());
		assertEquals("the master Jenkins node", jenkins.getNodeDescription());
		assertNull(jenkins.getNodeName());
	}

}
