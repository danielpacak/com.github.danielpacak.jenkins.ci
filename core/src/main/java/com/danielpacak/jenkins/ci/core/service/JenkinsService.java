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

import static com.danielpacak.jenkins.ci.core.client.JenkinsClient.SEGMENT_API_XML;

import java.io.IOException;

import com.danielpacak.jenkins.ci.core.Jenkins;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsResponse;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Jenkins class service.
 * 
 * @since 1.0.0
 */
public class JenkinsService extends AbstractService {

	public JenkinsService() {
		super();
	}

	public JenkinsService(JenkinsClient client) {
		super(client);
	}

	/**
	 * @return
	 * @throws IOException
	 * @since 1.0.0
	 */
	public Jenkins getJenkins() throws IOException {
		JenkinsResponse response = client.get(SEGMENT_API_XML);
		return response.getModel(new JenkinsConverter());
	}

	private class JenkinsConverter implements ResponseMapper<Jenkins> {
		@Override
		public Jenkins map(XmlResponse xmlResponse) {
			// @formatter:off
			return new Jenkins()
				.setMode(Jenkins.MODE.valueOf(xmlResponse.evaluateAsString("//mode/text()")))
				.setNodeDescription(xmlResponse.evaluateAsString("//nodeDescription/text()"))
				.setNodeName(xmlResponse.evaluateAsString("//nodeName/text()"))
				.setNumExecutors(xmlResponse.evaluateAsInteger("//numExecutors/text()"));
			// @formatter:on
		}
	}

}
