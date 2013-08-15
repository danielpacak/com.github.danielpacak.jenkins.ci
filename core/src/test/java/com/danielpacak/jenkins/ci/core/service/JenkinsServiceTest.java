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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.danielpacak.jenkins.ci.core.Jenkins;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsResponse;

/**
 * Tests for {@link JenkinsService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class JenkinsServiceTest {

	@Mock
	private JenkinsClient client;
	@Mock
	JenkinsResponse response;

	private JenkinsService service;

	@Before
	public void before() throws Exception {
		service = new JenkinsService(client);
	}

	@Test
	public void testGetJenkins() throws Exception {
		when(client.get("/api/xml")).thenReturn(response);
		Jenkins jenkins = service.getJenkins();
		System.out.println(jenkins);
		verify(client).get("/api/xml");
		verify(response).getModel((ResponseMapper<?>) Mockito.any());
	}

}
