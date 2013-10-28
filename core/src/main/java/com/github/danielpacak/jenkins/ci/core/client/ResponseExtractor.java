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

import java.io.IOException;

import com.github.danielpacak.jenkins.ci.core.http.client.ClientHttpResponse;

/**
 * Generic callback interface used by {@link JenkinsClient}'s retrieval methods. Implementations of this interface
 * perform the actual work of extracting data from a {@link ClientHttpResponse}, but don't need to worry about exception
 * handling or closing resources.
 */
public interface ResponseExtractor<T> {

   /**
    * Extract data from the given {@code ClientHttpResponse}.
    * 
    * @param response response
    * @return the extracted data
    * @throws IOException in case of I/O errors
    */
   T extract(ClientHttpResponse response) throws IOException;

}
