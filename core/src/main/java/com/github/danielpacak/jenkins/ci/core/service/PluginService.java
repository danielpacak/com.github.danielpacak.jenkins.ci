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
package com.github.danielpacak.jenkins.ci.core.service;

import static java.util.Arrays.asList;

import java.util.List;

import com.github.danielpacak.jenkins.ci.core.Plugin;
import com.github.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.github.danielpacak.jenkins.ci.core.client.JenkinsClientException;

/**
 * Plugin service class.
 * 
 * @see <a href="https://wiki.jenkins-ci.org/display/JENKINS/Plugins">Jenkins Plugins</a>
 * @since 1.0.0
 */
public class PluginService extends AbstractService {

   /**
    * Create a new plugin service for the {@link JenkinsClient#JenkinsClient() default} client.
    * 
    * @since 1.0.0
    */
   public PluginService() {
      super();
   }

   /**
    * Create a new plugin service for the given client.
    * 
    * @param client the client
    * @throws IllegalArgumentException if the client is {@code null}
    * @since 1.0.0
    */
   public PluginService(JenkinsClient client) {
      super(client);
   }

   /**
    * Get all plugins.
    * 
    * @return list of plugins
    * @throws JenkinsClientException if an error occurred connecting to Jenkins
    * @since 1.0.0
    */
   public List<Plugin> getPlugins() {
      return asList(client().getForObject("/pluginManager/api/xml?depth=1", Plugin[].class));
   }

}
