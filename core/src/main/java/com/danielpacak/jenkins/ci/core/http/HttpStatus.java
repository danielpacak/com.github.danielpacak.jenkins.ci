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
package com.danielpacak.jenkins.ci.core.http;

/**
 * Enumeration of HTTP status codes.
 * 
 * @since 1.0.0
 */
public enum HttpStatus {

   /**
    * {@code 200 OK}.
    */
   OK(200, "OK"),

   /**
    * {@code 201 Created}.
    */
   CREATED(201, "Created"),

   /**
    * {@code 202 Accepted}.
    */
   ACCEPTED(202, "Accepted"),

   /**
    * {@code 204 No Content}.
    */
   NO_CONTENT(204, "No Content"),

   /**
    * {@code 302 Found}.
    */
   FOUND(302, "Found"),

   /**
    * {@code 304 Not Modified}.
    */
   NOT_MODIFIED(304, "Not Modified"),

   /**
    * {@code 400 Bad Request}.
    */
   BAD_REQUEST(400, "Bad Request"),

   /**
    * {@code 403 Forbidden}.
    */
   FORBIDDEN(403, "Forbidden"),

   /**
    * {@code 404 Not Found}.
    */
   NOT_FOUND(404, "Not Found"),

   /**
    * {@code 500 Internal Server Error}.
    */
   INTERNAL_SERVER_ERROR(500, "Internal Server Error");

   private final int value;

   private final String reasonPhrase;

   private HttpStatus(int value, String reasonPhrase) {
      this.value = value;
      this.reasonPhrase = reasonPhrase;
   }

   /**
    * Return the integer value of this status code.
    */
   public int value() {
      return this.value;
   }

   /**
    * Return the reason phrase of this status code.
    */
   public String getReasonPhrase() {
      return reasonPhrase;
   }

   @Override
   public String toString() {
      return Integer.toString(value);
   }

   /**
    * Return the enum constant of this type with the specified numeric value.
    * 
    * @param statusCode the numeric value of the enum to be returned
    * @return the enum constant with the specified numeric value
    * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
    */
   public static HttpStatus valueOf(int statusCode) {
      for (HttpStatus status : values()) {
         if (status.value == statusCode) {
            return status;
         }
      }
      throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
   }

   public Series series() {
      return Series.valueOf(this);
   }

   public static enum Series {
      INFORMATIONAL(1), SUCCESSFUL(2), REDIRECTION(3), CLIENT_ERROR(4), SERVER_ERROR(5);

      private final int value;

      private Series(int value) {
         this.value = value;
      }

      /**
       * Return the integer value of this status series. Ranges from 1 to 5.
       */
      public int value() {
         return this.value;
      }

      public static Series valueOf(int status) {
         int seriesCode = status / 100;
         for (Series series : values()) {
            if (series.value == seriesCode) {
               return series;
            }
         }
         throw new IllegalArgumentException("No matching constant for [" + status + "]");
      }

      public static Series valueOf(HttpStatus status) {
         return valueOf(status.value);
      }
   }

}
