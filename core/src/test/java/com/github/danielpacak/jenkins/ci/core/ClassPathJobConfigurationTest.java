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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.github.danielpacak.jenkins.ci.core.util.Streams;

/**
 * Tests for {@link ClassPathJobConfiguration}.
 */
public class ClassPathJobConfigurationTest {

   @Rule
   public ExpectedException thrown = ExpectedException.none();

   @Test(expected = IllegalArgumentException.class)
   public void init_WithNullPath_ThrowsException() throws Exception {
      new ClassPathJobConfiguration(null);
   }

   @Test
   public void init_WithNonExistingResource_ThrowsException() throws Exception {
      thrown.expect(IllegalArgumentException.class);
      thrown.expectMessage("Class path resource [non/existing/job/config.xml] does not exist");
      new ClassPathJobConfiguration("non/existing/job/config.xml");
   }

   @Test
   public void getInputStream() throws Exception {
      ClassPathJobConfiguration jobConfiguration = new ClassPathJobConfiguration("job/config/empty.xml");
      assertEquals("<project></project>", Streams.toString(jobConfiguration.getInputStream()));
   }

}
