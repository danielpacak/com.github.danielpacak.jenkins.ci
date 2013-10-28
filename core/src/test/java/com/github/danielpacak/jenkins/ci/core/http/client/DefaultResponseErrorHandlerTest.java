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
package com.github.danielpacak.jenkins.ci.core.http.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.danielpacak.jenkins.ci.core.client.JenkinsClientException;
import com.github.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.github.danielpacak.jenkins.ci.core.http.HttpStatus;
import com.github.danielpacak.jenkins.ci.core.http.client.ClientHttpResponse;
import com.github.danielpacak.jenkins.ci.core.http.client.DefaultResponseErrorHandler;

/**
 * Tests for {@link DefaultResponseErrorHandler}.
 */
public class DefaultResponseErrorHandlerTest {

   @Rule
   public ExpectedException thrown = ExpectedException.none();

   private DefaultResponseErrorHandler errorHandler;

   @Before
   public void beforeTest() {
      this.errorHandler = new DefaultResponseErrorHandler();
   }

   @Test
   public void hasError() throws Exception {
      assertTrue(errorHandler.hasError(new MockClientHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR)));
      assertTrue(errorHandler.hasError(new MockClientHttpResponse(HttpStatus.BAD_REQUEST)));
      assertFalse(errorHandler.hasError(new MockClientHttpResponse(HttpStatus.ACCEPTED)));
   }

   @Test
   public void handleError() throws Exception {
      thrown.expect(JenkinsClientException.class);
      errorHandler.handleError(new MockClientHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR));
   }

   class MockClientHttpResponse implements ClientHttpResponse {
      HttpStatus status;

      MockClientHttpResponse(HttpStatus status) {
         this.status = status;
      }

      @Override
      public HttpHeaders getHeaders() {
         return new HttpHeaders();
      }

      @Override
      public int getRawStatusCode() throws IOException {
         return status.value();
      }

      @Override
      public HttpStatus getStatusCode() throws IOException {
         return status;
      }

      @Override
      public String getStatusText() throws IOException {
         return null;
      }

      @Override
      public InputStream getBody() throws IOException {
         return null;
      }

      @Override
      public void close() {
      }
   }

}
