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

import com.github.danielpacak.jenkins.ci.core.Job;
import com.github.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.github.danielpacak.jenkins.ci.core.http.HttpOutputMessage;
import com.github.danielpacak.jenkins.ci.core.util.XmlResponse;

public class JobArrayHttpMessageConverter implements HttpMessageConverter<Job[]> {

   @Override
   public boolean canRead(Class<?> clazz) {
      return Job[].class.equals(clazz);
   }

   @Override
   public boolean canWrite(Class<?> clazz) {
      return false;
   }

   @Override
   public Job[] read(Class<? extends Job[]> clazz, HttpInputMessage inputMessage) throws IOException {
      XmlResponse xml = new XmlResponse(inputMessage.getBody());
      Integer count = xml.evaluateAsInteger("count(/hudson/job)");
      Job[] jobs = new Job[count];
      for (int i = 0; i < count; i++) {
         String prefix = "/hudson/job[" + (i + 1) + "]";
         // @formatter:off
         jobs[i] = new Job()
            .setName(xml.evaluateAsString(prefix + "/name"))
            .setDisplayName(xml.evaluateAsString(prefix + "/displayName"))
            .setUrl(xml.evaluateAsString(prefix + "/url"))
            .setBuildable(xml.evaluateAsBoolean(prefix + "/buildable"))
            .setInQueue(xml.evaluateAsBoolean(prefix + "/inQueue"))
            .setNextBuildNumber(xml.evaluateAsLong(prefix + "/nextBuildNumber"));
         // @formatter:on
      }
      return jobs;
   }

   @Override
   public void write(Job[] t, String contentType, HttpOutputMessage outputMessage) throws IOException {
      throw new UnsupportedOperationException();
   }

}
