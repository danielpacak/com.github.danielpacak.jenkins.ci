package com.danielpacak.jenkins.ci.test;

import java.util.List;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Plugin;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.danielpacak.jenkins.ci.core.service.PluginService;

/**
 * Integration tests for {@link PluginService}.
 */
public class PluginServiceIntegrationTest extends AbstractJenkinsIntegrationTest {

	@Test
	public void testGetPlugins() throws Exception {
		JenkinsClient client = new JenkinsClient("localhost", 8080);
		PluginService service = new PluginService(client);
		List<Plugin> plugins = service.getPlugins();
		for (Plugin plugin : plugins) {
			print(plugin);
		}
	}

	private void print(Plugin plugin) {
		System.out.println("Plugin.shortName: " + plugin.getShortName());
		System.out.println("Plugin.longName: " + plugin.getLongName());
		System.out.println("Plugin.url: " + plugin.getUrl());
		System.out.println("Plugin.version: " + plugin.getVersion());
		System.out.println("Plugin.enabled: " + plugin.getEnabled());

	}

}
