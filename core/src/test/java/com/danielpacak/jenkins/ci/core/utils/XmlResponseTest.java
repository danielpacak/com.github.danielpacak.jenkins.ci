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
package com.danielpacak.jenkins.ci.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Tests for {@link XmlResponse}.
 */
public class XmlResponseTest {

   @Test
   public void testEvaluateAsString() throws Exception {
      XmlResponse xmlResponse = new XmlResponse("<result>SUCCESS</result>");
      assertEquals("SUCCESS", xmlResponse.evaluateAsString("//result/text()"));

      xmlResponse = new XmlResponse("<parent><result>SUCCESS</result></parent>");
      assertEquals("SUCCESS", xmlResponse.evaluateAsString("//result/text()"));

      assertNull(new XmlResponse("<nodeName/>").evaluateAsString("//nodeName/text()"));
   }

   @Test
   public void testEvaluateAsBoolean() throws Exception {
      assertEquals(Boolean.TRUE, new XmlResponse("<building>true</building>").evaluateAsBoolean("//building/text()"));
      assertEquals(Boolean.FALSE, new XmlResponse("<building>false</building>").evaluateAsBoolean("//building/text()"));

      assertEquals(Boolean.TRUE,
            new XmlResponse("<parent><building>TRUE</building></parent>").evaluateAsBoolean("//building/text()"));
   }

}
