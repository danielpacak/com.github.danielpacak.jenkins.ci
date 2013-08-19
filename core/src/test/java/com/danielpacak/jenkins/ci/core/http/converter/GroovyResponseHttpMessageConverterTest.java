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

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.GroovyResponse;
import com.danielpacak.jenkins.ci.core.util.Streams;

/**
 * Tests for {@link GroovyResponseHttpMessageConverter}.
 */
public class GroovyResponseHttpMessageConverterTest {

   private GroovyResponseHttpMessageConverter converter;

   @Before
   public void beforeTest() {
      converter = new GroovyResponseHttpMessageConverter();
   }

   @Test
   public void canRead() throws Exception {
      assertTrue(converter.canRead(GroovyResponse.class));
      assertTrue(converter.canRead(new GroovyResponse() {
         @Override
         public InputStream getInputStream() throws IOException {
            return null;
         }
      }.getClass()));
      assertFalse(converter.canRead(Object.class));
   }

   @Test
   public void canWrite() throws Exception {
      assertFalse(converter.canWrite(GroovyResponse.class));
      assertFalse(converter.canWrite(Object.class));
   }

   @Test
   public void read() throws Exception {
      MockHttpInputMessage inputMessage = new MockHttpInputMessage("print 'hello'");
      GroovyResponse response = converter.read(GroovyResponse.class, inputMessage);
      assertEquals("print 'hello'", Streams.toString(response.getInputStream()));
   }

   @Test(expected = UnsupportedOperationException.class)
   public void write() throws Exception {
      converter.write(null, null, null);
   }

}
