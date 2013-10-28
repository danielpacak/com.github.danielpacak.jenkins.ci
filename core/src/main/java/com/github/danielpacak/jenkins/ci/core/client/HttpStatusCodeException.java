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
package com.github.danielpacak.jenkins.ci.core.client;

import com.github.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.github.danielpacak.jenkins.ci.core.http.HttpStatus;

/**
 * Abstract base class for exceptions based on an {@link HttpStatus}.
 */
public abstract class HttpStatusCodeException extends JenkinsClientException {

   private static final long serialVersionUID = -2932109368688719724L;

   private final HttpStatus statusCode;

   private final String statusText;

   private final byte[] responseBody;

   private final HttpHeaders responseHeaders;

   /**
    * Construct a new instance of {@code HttpStatusCodeException} based on an {@link HttpStatus}.
    * 
    * @param statusCode the status code
    */
   protected HttpStatusCodeException(HttpStatus statusCode) {
      this(statusCode, statusCode.name(), null, null);
   }

   /**
    * Construct a new instance of {@code HttpStatusCodeException} based on an {@link HttpStatus} and status text.
    * 
    * @param statusCode the status code
    * @param statusText the status text
    */
   protected HttpStatusCodeException(HttpStatus statusCode, String statusText) {
      this(statusCode, statusText, null, null);
   }

   /**
    * Construct a new instance of {@code HttpStatusCodeException} based on an {@link HttpStatus}, status text, and
    * response body content.
    * 
    * @param statusCode the status code
    * @param statusText the status text
    * @param responseBody the response body content, may be {@code null}
    */
   protected HttpStatusCodeException(HttpStatus statusCode, String statusText, byte[] responseBody) {
      this(statusCode, statusText, null, responseBody);
   }

   /**
    * Construct a new instance of {@code HttpStatusCodeException} based on an {@link HttpStatus}, status text, and
    * response body content.
    * 
    * @param statusCode the status code
    * @param statusText the status text
    * @param responseHeaders the response headers, may be {@code null}
    * @param responseBody the response body content, may be {@code null}
    */
   protected HttpStatusCodeException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders,
         byte[] responseBody) {
      super(statusCode.value() + " " + statusText);
      this.statusCode = statusCode;
      this.statusText = statusText;
      this.responseHeaders = responseHeaders;
      this.responseBody = responseBody != null ? responseBody : new byte[0];
   }

   /**
    * Return the HTTP status code.
    */
   public HttpStatus getStatusCode() {
      return this.statusCode;
   }

   /**
    * Return the HTTP status text.
    */
   public String getStatusText() {
      return this.statusText;
   }

   /**
    * Return the HTTP response headers.
    */
   public HttpHeaders getResponseHeaders() {
      return this.responseHeaders;
   }

   /**
    * Return the response body as a byte array.
    */
   public byte[] getResponseBodyAsByteArray() {
      return responseBody;
   }

   /**
    * Return the response body as a string.
    */
   public String getResponseBodyAsString() {
      return new String(responseBody);
   }

}
