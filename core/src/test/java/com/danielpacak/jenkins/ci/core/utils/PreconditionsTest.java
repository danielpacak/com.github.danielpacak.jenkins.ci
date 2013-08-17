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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.danielpacak.jenkins.ci.core.util.Preconditions;

/**
 * Tests for {@link Preconditions}.
 */
public class PreconditionsTest {

   @Rule
   public ExpectedException thrown = ExpectedException.none();

   @Test
   public void checkArgumentNotNull() throws Exception {
      Preconditions.checkArgumentNotNull(new Object(), "Object cannot be null");
      thrown.expect(IllegalArgumentException.class);
      thrown.expectMessage("Object cannot be null");
      Preconditions.checkArgumentNotNull(null, "Object cannot be null");
   }

   @Test
   public void checkArgument() throws Exception {
      Preconditions.checkArgument(true, "This condition has to evaluate to true");
      thrown.expect(IllegalArgumentException.class);
      thrown.expectMessage("This condition has to evaluate to true");
      Preconditions.checkArgument(false, "This condition has to evaluate to %s", "true");
   }

}
