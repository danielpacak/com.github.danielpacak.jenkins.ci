package com.danielpacak.jenkins.ci.core;

import static com.danielpacak.jenkins.ci.core.util.Preconditions.checkArgumentNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 
 * @since 1.0.0
 *
 */
public class VariableSubstitutorJobConfiguration implements JobConfiguration {
	
	private final JobConfiguration decorated;
	private final Map<String, Object> values;
	
	public VariableSubstitutorJobConfiguration(JobConfiguration decorated, Map<String, Object> values) {
		this.decorated = checkArgumentNotNull(decorated, "JobConfiguration cannot be null");
		this.values = checkArgumentNotNull(values, "Values cannot be null");
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return decorated.getInputStream();
	}

}
