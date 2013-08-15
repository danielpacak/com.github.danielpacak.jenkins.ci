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

import com.danielpacak.jenkins.ci.core.Build;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Maps an {@link XmlResponse} to an instance of {@link Build}.
 */
public class BuildResponseMapper implements ResponseMapper<Build> {

	@Override
	public Build map(XmlResponse xmlResponse) {
		Build build = new Build();
		build.setNumber(xmlResponse.evaluateAsLong("//number"));

		Boolean building = xmlResponse.evaluateAsBoolean("//building");
		if (building) {
			build.setStatus(Build.Status.PENDING);
		} else {
			build.setStatus(Build.Status.valueOf(xmlResponse.evaluateAsString("//result")));
		}

		return build;
	}

}
