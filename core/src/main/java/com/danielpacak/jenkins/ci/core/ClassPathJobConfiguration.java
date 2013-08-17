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
package com.danielpacak.jenkins.ci.core;

import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgument;
import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.InputStream;

/**
 * Job configuration stored as a class path resource.
 * 
 * @since 1.0.0
 */
public class ClassPathJobConfiguration implements JobConfiguration {

   private final String path;

   public ClassPathJobConfiguration(String path) {
      this.path = checkArgumentNotNull(path, "Path cannot be null");
      checkArgument(getClass().getResource("/" + path) != null, "Class path resource [%s] does not exist", path);
   }

   @Override
   public InputStream getInputStream() {
      return getClass().getResourceAsStream("/" + path);
   }

}
