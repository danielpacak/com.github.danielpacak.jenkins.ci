package com.danielpacak.jenkins.ci.core.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsResponse;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Tests for {@link PluginService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class PluginServiceTest {

	@Mock
	private JenkinsClient client;
	private PluginService service;

	@Before
	public void before() throws Exception {
		service = new PluginService(client);
	}

	@Test
	public void testGetPlugins() throws Exception {
		XmlResponse xml = new XmlResponse("<localPluginManager><plugin></plugin></localPluginManager>");
		when(client.get("/pluginManager/api/xml?depth=1")).thenReturn(new JenkinsResponse(xml));
		service.getPlugins();
		verify(client).get("/pluginManager/api/xml?depth=1");
	}

}
