package com.danielpacak.jenkins.ci.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * Job configuration model class.
 * 
 * @since 1.0.0
 */
public interface JobConfiguration {

	InputStream getInputStream() throws IOException;

}
