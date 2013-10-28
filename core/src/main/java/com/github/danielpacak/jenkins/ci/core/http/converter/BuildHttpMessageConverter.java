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

import com.github.danielpacak.jenkins.ci.core.Build;
import com.github.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.github.danielpacak.jenkins.ci.core.http.HttpOutputMessage;
import com.github.danielpacak.jenkins.ci.core.util.XmlResponse;

public class BuildHttpMessageConverter implements HttpMessageConverter<Build> {

   @Override
   public boolean canRead(Class<?> clazz) {
      return Build.class.equals(clazz);
   }

   @Override
   public boolean canWrite(Class<?> clazz) {
      return false;
   }

   @Override
   public Build read(Class<? extends Build> clazz, HttpInputMessage inputMessage) throws IOException {
      XmlResponse xml = new XmlResponse(inputMessage.getBody());
      Build build = new Build();
      build.setNumber(xml.evaluateAsLong("/*/number"));
      build.setUrl(xml.evaluateAsString("/*/url"));
      build.setDuration(xml.evaluateAsLong("/*/duration"));

      Boolean building = xml.evaluateAsBoolean("/*/building");
      if (building) {
         build.setStatus(Build.Status.PENDING);
      } else {
         build.setStatus(Build.Status.valueOf(xml.evaluateAsString("/*/result")));
      }

      return build;
   }

   @Override
   public void write(Build t, String contentType, HttpOutputMessage outputMessage) throws IOException {
      throw new UnsupportedOperationException();
   }

}
