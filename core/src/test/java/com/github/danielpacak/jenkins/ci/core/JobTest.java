package com.github.danielpacak.jenkins.ci.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JobTest {

	private Job job = new Job();

	@Test
	public void lastBuildNumber_byDefault_isNull() {
		assertNull(job.getLastBuildNumber());
	}

	@Test
	public void lastBuildNumber_afterSetLastBuildNumber_isEqualToValueSet() {
		job.setLastBuildNumber(42L);
		assertEquals(Long.valueOf(42L), job.getLastBuildNumber());
	}
}
