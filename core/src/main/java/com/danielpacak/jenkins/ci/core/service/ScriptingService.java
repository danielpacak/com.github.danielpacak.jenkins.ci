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
package com.danielpacak.jenkins.ci.core.service;

import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_SCRIPT_TEXT;
import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import com.danielpacak.jenkins.ci.core.GroovyResponse;
import com.danielpacak.jenkins.ci.core.GroovyScript;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsClientException;

/**
 * Scripting service class.
 * 
 * @see <a href="https://wiki.jenkins-ci.org/display/JENKINS/Jenkins+Script+Console">Jenkins Script Console</a>
 * @since 1.0.0
 */
public class ScriptingService extends AbstractService {

   /**
    * Create a new scripting service for the {@link JenkinsClient#JenkinsClient() default} client.
    * 
    * @since 1.0.0
    */
   public ScriptingService() {
      super();
   }

   /**
    * Create a new scripting service for the given client.
    * 
    * @param client the client
    * @throws IllegalArgumentException if the client is {@code null}
    * @since 1.0.0
    */
   public ScriptingService(JenkinsClient client) {
      super(client);
   }

   /**
    * Run the given Groovy script.
    * 
    * @param script the script to be run
    * @return the Groovy response
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @throws IllegalArgumentException if the script is {@code null}
    * @since 1.0.0
    */
   public GroovyResponse runScript(GroovyScript script) {
      checkArgumentNotNull(script, "Script cannot be null");
      return client().postForObject(SEGMENT_SCRIPT_TEXT, script, GroovyResponse.class);
   }

}
