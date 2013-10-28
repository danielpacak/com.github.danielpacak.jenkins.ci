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
package com.github.danielpacak.jenkins.ci.core.http;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents HTTP request and response headers, mapping string header names to list of string values.
 */
public class HttpHeaders extends HashMap<String, List<String>> {

   private static final long serialVersionUID = 4258600664539135240L;

   public static final String ACCEPT = "Accept";

   public static final String CONTENT_TYPE = "Content-Type";

   public static final String CONTENT_LENGTH = "Content-Length";

   private static final String LOCATION = "Location";

   public static final String AUTHORIZATION = "Authorization";

   public static final String USER_AGENT = "User-Agent";

   public void add(String headerName, String headerValue) {
      if (!containsKey(headerName)) {
         put(headerName, new LinkedList<String>());
      }
      get(headerName).add(headerValue);
   }

   /**
    * Return the first header value for the given header name, if any.
    * 
    * @param headerName the header name
    * @return the first header value; or {@code null}
    */
   public String getFirst(String headerName) {
      List<String> headerValues = get(headerName);
      return headerValues != null ? headerValues.get(0) : null;
   }

   public void setContentType(String mediaType) {
      add(CONTENT_TYPE, mediaType);
   }

   public void setUserAgent(String userAgent) {
      add(USER_AGENT, userAgent);
   }

   public String getContentType() {
      if (containsKey(CONTENT_TYPE) && !get(CONTENT_TYPE).isEmpty()) {
         return get(CONTENT_TYPE).iterator().next();
      }
      return null;
   }

   public void setAuthorization(String authorization) {
      add(AUTHORIZATION, authorization);
   }

   public Long getContentLength() {
      if (containsKey(CONTENT_LENGTH) && !get(CONTENT_LENGTH).isEmpty()) {
         return Long.valueOf(get(CONTENT_LENGTH).iterator().next());
      }
      return new Long(-1);
   }

   public void setContentLength(Integer length) {
      add(CONTENT_LENGTH, String.valueOf(length));
   }

   /**
    * Return the (new) location of a resource, as specified by the {@code Location} header.
    * 
    * @return the location or {@code null} when the location is unknown
    */
   public URI getLocation() {
      String value = getFirst(LOCATION);
      return value != null ? URI.create(value) : null;
   }

   public HttpHeaders setLocation(URI location) {
      add(LOCATION, location.toASCIIString());
      return this;
   }

}
