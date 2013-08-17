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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.danielpacak.jenkins.ci.core.Plugin;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

/**
 * Tests for {@link PluginService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class PluginServiceTest {

   @Mock
   private JenkinsClient client;

   private PluginService service;

   @Before
   public void beforeTest() throws Exception {
      service = new PluginService(client);
   }

   @Test
   public void getPlugins() throws Exception {
      Plugin[] somePlugins = new Plugin[] {};
      when(client.getForObject("/pluginManager/api/xml?depth=1", Plugin[].class)).thenReturn(somePlugins);
      assertEquals(Arrays.asList(somePlugins), service.getPlugins());
   }

}
