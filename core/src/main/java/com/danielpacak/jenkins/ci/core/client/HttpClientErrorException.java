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

import com.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.danielpacak.jenkins.ci.core.http.HttpStatus;

/**
 * Exception thrown when an HTTP 4xx is received.
 */
public class HttpClientErrorException extends HttpStatusCodeException {

   private static final long serialVersionUID = -78271591735675365L;

   /**
    * Construct a new instance of {@code HttpClientErrorException} based on an {@link HttpStatus}.
    * 
    * @param statusCode the status code
    */
   public HttpClientErrorException(HttpStatus statusCode) {
      super(statusCode);
   }

   /**
    * Construct a new instance of {@code HttpClientErrorException} based on an {@link HttpStatus} and status text.
    * 
    * @param statusCode the status code
    * @param statusText the status text
    */
   public HttpClientErrorException(HttpStatus statusCode, String statusText) {
      super(statusCode, statusText);
   }

   /**
    * Construct a new instance of {@code HttpClientErrorException} based on an {@link HttpStatus}, status text, and
    * response body content.
    * 
    * @param statusCode the status code
    * @param statusText the status text
    * @param responseBody the response body content, may be {@code null}
    * @param responseCharset the response body charset, may be {@code null}
    */
   public HttpClientErrorException(HttpStatus statusCode, String statusText, byte[] responseBody) {
      super(statusCode, statusText, responseBody);
   }

   /**
    * Construct a new instance of {@code HttpClientErrorException} based on an {@link HttpStatus}, status text, and
    * response body content.
    * 
    * @param statusCode the status code
    * @param statusText the status text
    * @param responseHeaders the response headers, may be {@code null}
    * @param responseBody the response body content, may be {@code null}
    */
   public HttpClientErrorException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders,
         byte[] responseBody) {
      super(statusCode, statusText, responseHeaders, responseBody);
   }

}
