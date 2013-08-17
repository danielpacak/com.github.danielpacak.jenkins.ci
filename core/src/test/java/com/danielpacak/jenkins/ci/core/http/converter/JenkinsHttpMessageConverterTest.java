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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Jenkins;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;

/**
 * Tests for {@link JenkinsHttpMessageConverter}.
 */
public class JenkinsHttpMessageConverterTest {

   private JenkinsHttpMessageConverter converter;

   @Before
   public void beforeTest() {
      converter = new JenkinsHttpMessageConverter();
   }

   @Test
   public void canRead() throws Exception {
      assertTrue(converter.canRead(Jenkins.class));
      assertFalse(converter.canRead(String.class));
   }

   @Test
   public void canWrite() throws Exception {
      assertFalse(converter.canWrite(Jenkins.class));
      assertFalse(converter.canWrite(String.class));
   }

   @Test
   public void read() throws Exception {
      // @formatter:off
		HttpInputMessage inputMessage = new MockHttpInputMessage(""
			+	"<hudson>"
			+		"<mode>NORMAL</mode>"
			+		"<nodeDescription>Node description</nodeDescription>"
			+		"<nodeName/>"
			+		"<numExecutors>2</numExecutors>"
			+		"<useSecurity>false</useSecurity>"
			+	"</hudson>"
		);
		// @formatter:on

      Jenkins jenkins = converter.read(Jenkins.class, inputMessage);

      assertEquals(Jenkins.MODE.NORMAL, jenkins.getMode());
      assertEquals("Node description", jenkins.getNodeDescription());
      assertNull(jenkins.getNodeName());
      assertEquals(new Integer(2), jenkins.getNumExecutors());
      assertFalse(jenkins.getUseSecurity());
   }

   @Test(expected = UnsupportedOperationException.class)
   public void write() throws Exception {
      converter.write(new Jenkins(), null, null);
   }

}
