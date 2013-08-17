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
package com.danielpacak.jenkins.ci.core.util;

/**
 * Simple static methods to be called at the start of your own methods to verify correct arguments and state.
 * 
 * @since 1.0.0
 */
public final class Preconditions {

   private Preconditions() {

   }

   public static <T> T checkArgumentNotNull(T argument, String errorMessage) {
      if (argument == null) {
         throw new IllegalArgumentException(errorMessage);
      }
      return argument;
   }

   public static void checkArgument(boolean condition, String messageFormat, Object... args) {
      if (!condition) {
         throw new IllegalArgumentException(String.format(messageFormat, args));
      }
   }

}
