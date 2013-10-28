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
package com.github.danielpacak.jenkins.ci.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.danielpacak.jenkins.ci.core.Plugin;
import com.github.danielpacak.jenkins.ci.core.service.PluginService;

/**
 * Integration tests for {@link PluginService}.
 */
public class PluginServiceIntegrationTest extends AbstractJenkinsIntegrationTest {

   private PluginService service;

   @Before
   public void beforeTest() {
      service = new PluginService(getJenkinsClient());
   }

   @Test
   public void getPlugins() throws Exception {
      List<Plugin> plugins = service.getPlugins();
      for (Plugin plugin : plugins) {
         print(plugin);
      }
   }

   private void print(Plugin plugin) {
      System.out.println("Plugin.shortName: " + plugin.getShortName());
      System.out.println("Plugin.longName: " + plugin.getLongName());
      System.out.println("Plugin.url: " + plugin.getUrl());
      System.out.println("Plugin.version: " + plugin.getVersion());
      System.out.println("Plugin.enabled: " + plugin.getEnabled());

   }

}
