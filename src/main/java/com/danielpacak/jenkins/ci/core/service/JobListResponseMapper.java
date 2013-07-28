package com.danielpacak.jenkins.ci.core.service;

import java.util.ArrayList;
import java.util.List;

import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/** Maps an {@link XmlResponse} to a list of {@link Job} models. */
public class JobListResponseMapper implements ResponseMapper<List<Job>> {

	@Override
	public List<Job> map(XmlResponse response) {
		Integer count = response.evaluateAsInteger("count(//hudson/job)");
		List<Job> jobs = new ArrayList<Job>();
		for (int i = 1; i <= count; i++) {
			String prefix = "//hudson/job[" + i + "]";
			// @formatter:off
			jobs.add(new Job()
				.setName(response.evaluateAsString(prefix + "/name"))
				.setDisplayName(response.evaluateAsString(prefix + "/displayName"))
			);
			// @formatter:on
		}
		return jobs;
	}

}
