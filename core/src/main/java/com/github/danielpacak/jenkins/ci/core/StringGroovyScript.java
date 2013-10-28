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
package com.github.danielpacak.jenkins.ci.core;

import static com.github.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of {@link GroovyScript} that stores the script as a string.
 * 
 * @since 1.0.0
 */
public class StringGroovyScript implements GroovyScript {

   private final String script;

   /**
    * Create a new Groovy script from the given string.
    * 
    * @param script the script string
    * @throws IllegalArgumentException if the script is {@code null}
    * @since 1.0.0
    */
   public StringGroovyScript(String script) {
      this.script = checkArgumentNotNull(script, "Script cannot be null");
   }

   @Override
   public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(script.getBytes());
   }

}
