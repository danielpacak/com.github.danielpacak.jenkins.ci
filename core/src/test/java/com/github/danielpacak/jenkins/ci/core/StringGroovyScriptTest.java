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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.danielpacak.jenkins.ci.core.StringGroovyScript;
import com.github.danielpacak.jenkins.ci.core.util.Streams;

/**
 * Tests for {@link StringGroovyScript}.
 */
public class StringGroovyScriptTest {

   @Test(expected = IllegalArgumentException.class)
   public void init_WithNullScript_ThrowsException() {
      new StringGroovyScript(null);
   }

   @Test
   public void init() throws Exception {
      StringGroovyScript script = new StringGroovyScript("print 'Hello, Jenkins!'");
      assertEquals("print 'Hello, Jenkins!'", Streams.toString(script.getInputStream()));
   }

}
