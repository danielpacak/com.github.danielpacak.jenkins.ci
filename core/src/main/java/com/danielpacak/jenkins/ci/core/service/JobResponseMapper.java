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
package com.danielpacak.jenkins.ci.core.service;

import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Maps an {@link XmlResponse} to an instance of the {@link Job} model.
 */
public class JobResponseMapper implements ResponseMapper<Job> {

	@Override
	public Job map(XmlResponse response) {
		// @formatter:off
		return new Job()
			.setName(response.evaluateAsString("/*/name/text()"))
			.setDisplayName(response.evaluateAsString("//displayName/text()"))
			.setUrl(response.evaluateAsString("//url/text()"))
			.setBuildable(response.evaluateAsBoolean("//buildable/text()"))
			.setInQueue(response.evaluateAsBoolean("//inQueue/text()"))
			.setNextBuildNumber(response.evaluateAsLong("//nextBuildNumber"));
		// @formatter:on
	}

}
