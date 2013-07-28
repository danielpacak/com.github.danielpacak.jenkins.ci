package com.danielpacak.jenkins.ci.core.client;

import com.danielpacak.jenkins.ci.core.service.ResponseMapper;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

public class JenkinsResponse {

	private XmlResponse content;

	// nullable
	public JenkinsResponse(XmlResponse content) {
		this.content = content;
	}

	public <T> T getModel(ResponseMapper<T> converter) {
		return converter.map(content);
	}

}
