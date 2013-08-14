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
			.setName(response.evaluateAsString("//name/text()"))
			.setDisplayName(response.evaluateAsString("//displayName/text()"))
			.setUrl(response.evaluateAsString("//url/text()"))
			.setBuildable(response.evaluateAsBoolean("//buildable/text()"))
			.setInQueue(response.evaluateAsBoolean("//inQueue/text()"))
			.setNextBuildNumber(response.evaluateAsLong("//nextBuildNumber"));
		// @formatter:on
	}

}
