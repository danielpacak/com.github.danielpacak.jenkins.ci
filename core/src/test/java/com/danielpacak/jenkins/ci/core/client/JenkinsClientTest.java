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

import static com.danielpacak.jenkins.ci.core.http.HttpMethod.GET;
import static com.danielpacak.jenkins.ci.core.http.HttpMethod.POST;
import static com.danielpacak.jenkins.ci.core.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.danielpacak.jenkins.ci.core.http.HttpMethod;
import com.danielpacak.jenkins.ci.core.http.HttpStatus;
import com.danielpacak.jenkins.ci.core.http.client.ClientHttpRequest;
import com.danielpacak.jenkins.ci.core.http.client.ClientHttpRequestFactory;
import com.danielpacak.jenkins.ci.core.http.client.ClientHttpResponse;
import com.danielpacak.jenkins.ci.core.http.client.ResponseErrorHandler;
import com.danielpacak.jenkins.ci.core.http.converter.HttpMessageConverter;

/**
 * Tests for {@link JenkinsClient}.
 */
@RunWith(MockitoJUnitRunner.class)
public class JenkinsClientTest {

   @Mock
   private ClientHttpRequestFactory httpRequestFactory;

   @Mock
   private ClientHttpRequest httpRequest;

   @Mock
   private ClientHttpResponse httpResponse;

   @SuppressWarnings("rawtypes")
   @Mock
   private HttpMessageConverter converter;

   @Mock
   private ResponseErrorHandler errorHandler;

   private JenkinsClient client;

   @Before
   public void beforeTest() throws Exception {
      client = new JenkinsClient();
      client.setClientHttpRequestFactory(httpRequestFactory);
      client.setMessageConverters(Collections.<HttpMessageConverter<?>> singletonList(converter));
      client.setResponseErrorHandler(errorHandler);
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
      verify(httpRequestFactory).setCredentials("dpacak", "passw0rd");
   }

   @Test(expected = IllegalArgumentException.class)
   public void setUserAgent_WithNullIdentifier_ThrowsException() throws Exception {
      client.setUserAgent(null);
   }

   @Test
   public void setUserAgent() throws Exception {
      client.setUserAgent("JenkinsJavaAPI");
      verify(httpRequestFactory).setUserAgent("JenkinsJavaAPI");
   }

   @SuppressWarnings("unchecked")
   @Test
   public void getForObject() throws Exception {
      given(converter.canRead(String.class)).willReturn(true);

      given(httpRequestFactory.createRequest(new URI("http://localhost:8080/api/xml"), GET)).willReturn(httpRequest);

      given(httpRequest.getHeaders()).willReturn(new HttpHeaders());
      given(httpRequest.execute()).willReturn(httpResponse);
      given(errorHandler.hasError(httpResponse)).willReturn(false);

      given(httpResponse.getStatusCode()).willReturn(HttpStatus.OK);
      given(httpResponse.getHeaders()).willReturn(new HttpHeaders());
      given(converter.read(String.class, httpResponse)).willReturn("Hello, Jenkins!");

      assertEquals("Hello, Jenkins!", client.getForObject("/api/xml", String.class));

      verify(httpResponse).close();
   }

   @SuppressWarnings("unchecked")
   @Test
   public void postForLocation() throws Exception {
      given(converter.canWrite(String.class)).willReturn(true);
      given(errorHandler.hasError(httpResponse)).willReturn(false);

      given(httpRequestFactory.createRequest(new URI("http://localhost:8080/api/xml"), POST)).willReturn(httpRequest);
      given(httpRequest.execute()).willReturn(httpResponse);

      HttpHeaders responseHeaders = new HttpHeaders().setLocation(new URI("http://example.com/location"));
      given(httpResponse.getHeaders()).willReturn(responseHeaders);

      assertEquals(new URI("http://example.com/location"), client.postForLocation("/api/xml", "Post payload"));

      verify(converter).write("Post payload", null, httpRequest);
      verify(httpResponse).close();
   }

   @Test
   public void postForLocation_WithNullRequest() throws Exception {
      given(errorHandler.hasError(httpResponse)).willReturn(false);

      given(httpRequestFactory.createRequest(new URI("http://localhost:8080/api/xml"), POST)).willReturn(httpRequest);
      given(httpRequest.execute()).willReturn(httpResponse);

      HttpHeaders responseHeaders = new HttpHeaders().setLocation(new URI("http://example.com/location"));
      given(httpResponse.getHeaders()).willReturn(responseHeaders);

      assertEquals(new URI("http://example.com/location"), client.postForLocation("/api/xml", null));

      verify(httpResponse).close();
   }

   @SuppressWarnings("unchecked")
   @Test
   public void postForObject() throws Exception {
      given(converter.canRead(Integer.class)).willReturn(true);
      given(converter.canWrite(String.class)).willReturn(true);

      given(httpRequestFactory.createRequest(new URI("http://localhost:8080/api/xml"), POST)).willReturn(httpRequest);

      given(httpRequest.getHeaders()).willReturn(new HttpHeaders());
      given(httpRequest.execute()).willReturn(httpResponse);
      given(errorHandler.hasError(httpResponse)).willReturn(false);

      given(httpResponse.getStatusCode()).willReturn(HttpStatus.OK);
      given(httpResponse.getHeaders()).willReturn(new HttpHeaders());

      given(converter.read(Integer.class, httpResponse)).willReturn(new Integer(42));

      assertEquals(new Integer(42), client.postForObject("/api/xml", "Post payload", Integer.class));

      verify(converter).write("Post payload", null, httpRequest);
      verify(httpResponse).close();
   }

   @Test
   public void execute_WhenErrorHandlerThrowsException_ThrowsExceptionAndClosesHttpResponse() throws Exception {
      given(httpRequestFactory.createRequest(new URI("http://example.com"), GET)).willReturn(httpRequest);
      given(errorHandler.hasError(httpResponse)).willReturn(true);
      willThrow(new HttpServerErrorException(INTERNAL_SERVER_ERROR)).given(errorHandler).handleError(httpResponse);

      given(httpRequest.execute()).willReturn(httpResponse);

      try {
         client.execute(new URI("http://example.com"), HttpMethod.GET, null, null);
         fail();
      } catch (HttpServerErrorException expected) {
         assertEquals(INTERNAL_SERVER_ERROR, expected.getStatusCode());
      }

      verify(httpResponse).close();
   }

   @Test
   public void execute_WhenThrowsIOException_ThrowsException() throws Exception {
      given(httpRequestFactory.createRequest(new URI("http://example.com"), GET)).willReturn(httpRequest);
      willThrow(new IOException("Unplugged network cable")).given(httpRequest).execute();

      try {
         client.execute(new URI("http://example.com"), GET, null, null);
         fail();
      } catch (ResourceAccessException expected) {
         assertEquals("I/O error on GET request for \"http://example.com\": Unplugged network cable",
               expected.getMessage());
      }
   }

   @SuppressWarnings("unchecked")
   @Test
   public void postForObject_WithMissingWriteConverter() throws Exception {
      given(converter.canWrite(String.class)).willReturn(false);
      given(httpRequestFactory.createRequest(new URI("http://localhost:8080/api/xml"), POST)).willReturn(httpRequest);

      try {
         client.postForObject("/api/xml", "Post payload", String.class);
         fail();
      } catch (JenkinsClientException expected) {
         assertEquals(
               "Could not write request: no suitable HttpMessageConverter found for request type [java.lang.String]",
               expected.getMessage());
      }
   }

   @SuppressWarnings("unchecked")
   @Test
   public void postForObject_WithMissingReadConverter() throws Exception {
      given(converter.canWrite(String.class)).willReturn(true);
      given(converter.canRead(Integer.class)).willReturn(false);

      given(httpRequestFactory.createRequest(new URI("http://localhost:8080/api/xml"), POST)).willReturn(httpRequest);
      given(httpRequest.execute()).willReturn(httpResponse);
      given(httpResponse.getHeaders()).willReturn(new HttpHeaders());
      given(errorHandler.hasError(httpResponse)).willReturn(false);

      try {
         client.postForObject("/api/xml", "Post payload", Integer.class);
         fail();
      } catch (JenkinsClientException expected) {
         assertEquals(
               "Could not read response: no suitable HttpMessageConverter found for response type [java.lang.Integer]",
               expected.getMessage());
      }
   }

}
