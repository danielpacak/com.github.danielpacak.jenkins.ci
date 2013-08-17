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

import java.io.IOException;

import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.danielpacak.jenkins.ci.core.http.HttpOutputMessage;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

public class JobHttpMessageConverter implements HttpMessageConverter<Job> {

   @Override
   public boolean canRead(Class<?> clazz) {
      return Job.class.equals(clazz);
   }

   @Override
   public boolean canWrite(Class<?> clazz) {
      return false;
   }

   @Override
   public Job read(Class<? extends Job> clazz, HttpInputMessage inputMessage) throws IOException {
      XmlResponse response = new XmlResponse(inputMessage.getBody());
      // @formatter:off
      return new Job()
         .setName(response.evaluateAsString("/*/name"))
         .setDisplayName(response.evaluateAsString("/*/displayName/text()"))
         .setUrl(response.evaluateAsString("/*/url"))
         .setBuildable(response.evaluateAsBoolean("/*/buildable"))
         .setInQueue(response.evaluateAsBoolean("/*/inQueue"))
         .setNextBuildNumber(response.evaluateAsLong("/*/nextBuildNumber"));
      // @formatter:on
   }

   @Override
   public void write(Job t, String contentType, HttpOutputMessage outputMessage) throws IOException {
      throw new UnsupportedOperationException();
   }

}
