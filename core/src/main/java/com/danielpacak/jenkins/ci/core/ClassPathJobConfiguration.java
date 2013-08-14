package com.danielpacak.jenkins.ci.core;

import java.io.InputStream;

import com.danielpacak.jenkins.ci.core.util.Preconditions;

/**
 * Job configuration stored as a class path resource.
 * 
 * @since 1.0.0
 */
public class ClassPathJobConfiguration implements JobConfiguration {

	private final String path;

	public ClassPathJobConfiguration(String path) {
		this.path = Preconditions.checkArgumentNotNull(path,
				"Path cannot be null");
	}

	@Override
	public InputStream getInputStream() {
		return getClass().getResourceAsStream("/" + path);
	}

}
