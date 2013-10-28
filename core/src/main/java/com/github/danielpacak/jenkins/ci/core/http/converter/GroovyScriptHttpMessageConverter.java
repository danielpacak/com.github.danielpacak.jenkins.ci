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
package com.github.danielpacak.jenkins.ci.core.http.converter;

import java.io.IOException;

import com.github.danielpacak.jenkins.ci.core.GroovyScript;
import com.github.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.github.danielpacak.jenkins.ci.core.http.HttpOutputMessage;
import com.github.danielpacak.jenkins.ci.core.util.Streams;

public class GroovyScriptHttpMessageConverter implements HttpMessageConverter<GroovyScript> {

   @Override
   public boolean canRead(Class<?> clazz) {
      return false;
   }

   @Override
   public boolean canWrite(Class<?> clazz) {
      return GroovyScript.class.isAssignableFrom(clazz);
   }

   @Override
   public void write(GroovyScript t, String contentType, HttpOutputMessage outputMessage) throws IOException {
      String payload = "script=" + Streams.toString(t.getInputStream());
      Streams.copy(payload, outputMessage.getBody());
   }

   @Override
   public GroovyScript read(Class<? extends GroovyScript> clazz, HttpInputMessage inputMessage) throws IOException {
      throw new UnsupportedOperationException();
   }

}
