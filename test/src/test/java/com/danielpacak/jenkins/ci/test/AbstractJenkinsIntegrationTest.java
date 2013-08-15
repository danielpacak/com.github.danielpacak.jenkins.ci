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
package com.danielpacak.jenkins.ci.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.danielpacak.jenkins.ci.core.Job;

public abstract class AbstractJenkinsIntegrationTest {

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Starting Jenkins CI...");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("Stopping Jenkins CI...");
	}

	/**
	 * Return a new instance of the {@link Job} class with a random name. The name has the following patter
	 * <code>junit-{currentTimeMillis}</code>.
	 * 
	 * @return job
	 */
	protected Job newJobWithRandomName() {
		return new Job().setName("junit-" + System.currentTimeMillis());
	}

}
