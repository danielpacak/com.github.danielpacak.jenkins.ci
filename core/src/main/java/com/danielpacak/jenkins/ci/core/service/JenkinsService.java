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

import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_API_XML;

import com.danielpacak.jenkins.ci.core.Jenkins;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsClientException;

/**
 * Jenkins class service.
 * 
 * @since 1.0.0
 */
public class JenkinsService extends AbstractService {

   /**
    * Create job service for the default client.
    * 
    * @since 1.0.0
    */
   public JenkinsService() {
      super();
   }

   /**
    * Create job service for the given client.
    * 
    * @param client
    * @since 1.0.0
    */
   public JenkinsService(JenkinsClient client) {
      super(client);
   }

   /**
    * @return
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @since 1.0.0
    */
   public Jenkins getJenkins() {
      return client.getForObject(SEGMENT_API_XML, Jenkins.class);
   }

}
