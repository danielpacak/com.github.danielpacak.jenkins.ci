package com.danielpacak.jenkins.ci.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Jenkins;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

public class JenkinsServiceIntegrationTest {

	@Test
	public void testGetJenkins() throws Exception {
		JenkinsClient client = new JenkinsClient("localhost", 8080);
		JenkinsService service = new JenkinsService(client);
		Jenkins jenkins = service.getJenkins();
		assertEquals(Jenkins.MODE.NORMAL, jenkins.getMode());
		assertEquals(new Integer(2), jenkins.getNumExecutors());
		assertEquals("the master Jenkins node", jenkins.getNodeDescription());
		assertNull(jenkins.getNodeName());
	}

}
