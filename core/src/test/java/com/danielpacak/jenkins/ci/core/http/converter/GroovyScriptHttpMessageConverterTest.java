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
package com.danielpacak.jenkins.ci.core.http.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.GroovyScript;
import com.danielpacak.jenkins.ci.core.StringGroovyScript;

/**
 * Tests for {@link GroovyScriptHttpMessageConverter}.
 */
public class GroovyScriptHttpMessageConverterTest {

   private GroovyScriptHttpMessageConverter converter;

   @Before
   public void beforeTest() {
      converter = new GroovyScriptHttpMessageConverter();
   }

   @Test
   public void canRead() throws Exception {
      assertFalse(converter.canRead(GroovyScript.class));
      assertFalse(converter.canRead(Object.class));
   }

   @Test
   public void canWrite() throws Exception {
      assertTrue(converter.canWrite(GroovyScript.class));
      assertTrue(converter.canWrite(StringGroovyScript.class));
      assertFalse(converter.canWrite(Object.class));
   }

   @Test
   public void write() throws Exception {
      GroovyScript script = new StringGroovyScript("print 'Hello, Jenkins!'");
      MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
      converter.write(script, null, outputMessage);
      assertEquals("script=print 'Hello, Jenkins!'", outputMessage.getBodyAsString());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void read() throws Exception {
      converter.read(GroovyScript.class, null);
   }

}
