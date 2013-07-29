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
