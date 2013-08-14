package com.danielpacak.jenkins.ci.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link VariableSubstitutorJobConfiguration}.
 */
public class VariableSubstitutorJobConfigurationTest {

	@Test(expected = IllegalArgumentException.class)
	public void init_WhenJobConfigurationIsNull_ThrowsException() {
		new VariableSubstitutorJobConfiguration(null, new HashMap<String, Object>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void init_WhenValuesIsNull_ThrowsException() {
		new VariableSubstitutorJobConfiguration(new NotNullJobConfiguration(), null);
	}

	@Ignore
	@Test
	public void testGetInputStream() throws Exception {
		JobConfiguration decorated = null;
		Map<String, Object> values = new HashMap<String, Object>();
		JobConfiguration config = new VariableSubstitutorJobConfiguration(decorated, values);

		config.getInputStream();
	}

	class NotNullJobConfiguration implements JobConfiguration {
		@Override
		public InputStream getInputStream() {
			throw new IllegalStateException("I'm not supposed to be called");
		}
	}

}
