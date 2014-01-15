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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.github.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.github.danielpacak.jenkins.ci.core.http.HttpMethod;
import com.github.danielpacak.jenkins.ci.core.util.Streams;

public class SimpleClientHttpRequest implements ClientHttpRequest {

   private HttpHeaders headers = new HttpHeaders();

   private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

   private final HttpURLConnection connection;

   public SimpleClientHttpRequest(HttpURLConnection connection) {
      this.connection = connection;
   }

   @Override
   public HttpMethod getMethod() {
      return HttpMethod.valueOf(this.connection.getRequestMethod());
   }

   @Override
   public URI getURI() {
      try {
         return this.connection.getURL().toURI();
      } catch (URISyntaxException e) {
         throw new IllegalStateException("Could not get HttpURLConnection URI: " + e.getMessage(), e);
      }
   }

   @Override
   public ClientHttpResponse execute() throws IOException {
      for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
         String headerName = entry.getKey();
         for (String headerValue : entry.getValue()) {
            this.connection.addRequestProperty(headerName, headerValue);
         }
      }
      if (this.connection.getDoOutput()) {
         this.connection.setFixedLengthStreamingMode(outputStream.size());
         Streams.copy(outputStream.toByteArray(), connection.getOutputStream());
      }

      return new SimpleClientHttpResponse(this.connection);
   }

   @Override
   public HttpHeaders getHeaders() {
      return headers;
   }

   @Override
   public OutputStream getBody() throws IOException {
      return outputStream;
   }

}
