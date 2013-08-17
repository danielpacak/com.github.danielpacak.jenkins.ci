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

/**
 * Base class for exceptions thrown by {@link JenkinsClient} whenever it encounters client-server HTTP errors.
 */
public class JenkinsClientException extends RuntimeException {

   private static final long serialVersionUID = 7198120052975691526L;

   /**
    * Construct a new {@code JenkinsClientException} exception with the given message.
    * 
    * @param message
    */
   public JenkinsClientException(String message) {
      super(message);
   }

   /**
    * Construct a new {@code JenkinsClientException} exception with the given {@link Throwable}.
    * 
    * @param cause {@link Throwable}
    */
   public JenkinsClientException(Throwable cause) {
      super(cause);
   }

   /**
    * Construct a new {@code JenkinsClientException} exception with the given message and {@link Throwable}.
    * 
    * @param message
    * @param cause {@link Throwable}
    */
   public JenkinsClientException(String message, Throwable cause) {
      super(message, cause);
   }

}
