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
package com.danielpacak.jenkins.ci.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.GroovyResponse;
import com.danielpacak.jenkins.ci.core.GroovyScript;
import com.danielpacak.jenkins.ci.core.StringGroovyScript;
import com.danielpacak.jenkins.ci.core.service.ScriptingService;
import com.danielpacak.jenkins.ci.core.util.Streams;

/**
 * Integration tests for {@link ScriptingService}.
 */
public class ScriptingServiceTest extends AbstractJenkinsIntegrationTest {

   private ScriptingService service;

   @Before
   public void beforeTest() throws Exception {
      this.service = new ScriptingService(getJenkinsClient());
   }

   @Test
   public void runScript() throws Exception {
      GroovyScript script = new StringGroovyScript("print 'Hello, Jenkins!'");

      GroovyResponse response = service.runScript(script);
      assertEquals("Hello, Jenkins!", Streams.toString(response.getInputStream()));
   }

}
