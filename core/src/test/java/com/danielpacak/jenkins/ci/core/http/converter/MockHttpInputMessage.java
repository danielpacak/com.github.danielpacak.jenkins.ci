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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;

public class MockHttpInputMessage implements HttpInputMessage {

   private final String content;

   public MockHttpInputMessage(String content) {
      this.content = content;
   }

   @Override
   public HttpHeaders getHeaders() {
      return new HttpHeaders();
   }

   @Override
   public InputStream getBody() throws IOException {
      return new ByteArrayInputStream(content.getBytes());
   }

}
