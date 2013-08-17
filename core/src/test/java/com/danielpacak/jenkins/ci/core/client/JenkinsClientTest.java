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
package com.danielpacak.jenkins.ci.core.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.danielpacak.jenkins.ci.core.http.client.ClientHttpRequestFactory;

/**
 * Tests for {@link JenkinsClient}
 */
@RunWith(MockitoJUnitRunner.class)
public class JenkinsClientTest {

   @Mock
   private ClientHttpRequestFactory httpRequestFactory;

   private JenkinsClient client;

   @Before
   public void beforeTest() throws Exception {
      client = new JenkinsClient();
      client.setClientHttpRequestFactory(httpRequestFactory);
   }

   @Test(expected = IllegalArgumentException.class)
   public void setCredentials_WithNullUser_ThrowsException() throws Exception {
      client.setCredentials(null, "passw0rd");
   }

   @Test(expected = IllegalArgumentException.class)
   public void setCredentials_WithNullPassword_ThrowsException() throws Exception {
      client.setCredentials("dpacak", null);
   }

   @Test
   public void setCredentials() throws Exception {
      client.setCredentials("dpacak", "passw0rd");
      Mockito.verify(httpRequestFactory).setCredentials("dpacak", "passw0rd");
   }

   @Test(expected = IllegalArgumentException.class)
   public void setUserAgent_WithNullIdentifier_ThrowsException() throws Exception {
      client.setUserAgent(null);
   }

   @Test
   public void setUserAgent() throws Exception {
      client.setUserAgent("JenkinsJavaAPI");
      Mockito.verify(httpRequestFactory).setUserAgent("JenkinsJavaAPI");
   }

}
