package com.danielpacak.jenkins.ci.core.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.danielpacak.jenkins.ci.core.Plugin;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.client.JenkinsResponse;

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
		//when(client.get("/pluginManager/api/xml?depth=1")).thenReturn(new JenkinsResponse(
			//	"<localPluginManager></localPluginManager>"));
		List<Plugin> plugins = service.getPlugins();
		verify(client).get("/pluginManager/api/xml?depth=1");
	}

}
