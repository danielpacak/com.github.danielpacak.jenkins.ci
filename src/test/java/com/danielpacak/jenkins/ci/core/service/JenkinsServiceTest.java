package com.danielpacak.jenkins.ci.core.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Jenkins;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

/**
 * Tests for {@link JenkinsService}.
 */
public class JenkinsServiceTest {

	@Test
	public void testGetJenkins() throws Exception {
		JenkinsClient client = new JenkinsClient("localhost", 8080);
		JenkinsService jenkinsService = new JenkinsService(client);

		Jenkins jenkins = jenkinsService.getJenkins();

		assertEquals("the master Jenkins node", jenkins.getNodeDescription());
		assertEquals(Jenkins.MODE.NORMAL, jenkins.getMode());
		assertEquals(new Integer(2), jenkins.getNumExecutors());
		assertNull(jenkins.getNodeName());
	}

}
