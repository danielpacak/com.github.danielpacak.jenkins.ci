package com.danielpacak.jenkins.ci.core.service;

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
		JenkinsResponse response = client.get("/api/xml");
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
