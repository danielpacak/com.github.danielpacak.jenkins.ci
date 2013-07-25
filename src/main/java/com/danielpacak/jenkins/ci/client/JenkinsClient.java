package com.danielpacak.jenkins.ci.client;

import java.io.InputStream;
import java.util.Map;

public interface JenkinsClient {

	void createJob(String jobName, InputStream jobDefinitionXml)
			throws Exception;

	void scheduleABuild(String jobName) throws Exception;

	void scheduleABuild(String jobName, Map<String, String> paramsMap)
			throws Exception;

	Long getLastBuildId(String jobName) throws Exception;

	public String getBuildStatus(String jobName, Long buildId) throws Exception;

}
