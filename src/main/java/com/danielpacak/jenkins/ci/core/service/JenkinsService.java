package com.danielpacak.jenkins.ci.core.service;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.danielpacak.jenkins.ci.core.Jenkins;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.util.Preconditions;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Jenkins class service.
 * 
 * @since 1.0.0
 */
public class JenkinsService {

	private JenkinsClient client;
	private HttpClient httpClient;

	public JenkinsService(JenkinsClient client) {
		this.client = Preconditions.checkArgumentNotNull(client,
				"Client cannot be null");
		this.httpClient = new HttpClient();
	}

	public Jenkins getJenkins() throws IOException {
		String url = "http://" + client.getHost() + ":" + client.getPort()
				+ "/api/xml";
		GetMethod get = new GetMethod(url);
		int responseCode = httpClient.executeMethod(get);
		if (responseCode == 200) {
			XmlResponse xmlResponse = new XmlResponse(get.getResponseBodyAsStream());
			Jenkins jenkins = new Jenkins();
			jenkins.setMode(Jenkins.MODE.valueOf(xmlResponse.evaluateAsString("//mode/text()")));
			jenkins.setNodeDescription(xmlResponse.evaluateAsString("//nodeDescription/text()"));
			jenkins.setNodeName(xmlResponse.evaluateAsString("//nodeName/text()"));
			jenkins.setNumExecutors(xmlResponse.evaluateAsInteger("//numExecutors/text()"));
			return jenkins;
		}

		throw new IllegalStateException();
	}

}
