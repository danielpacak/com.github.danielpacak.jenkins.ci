package com.danielpacak.jenkins.ci.core.service;

import java.util.ArrayList;
import java.util.List;

import com.danielpacak.jenkins.ci.core.Plugin;
import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Maps an {@link XmlResponse} to a list of {@link Plugin} models.
 */
public class PluginListResponseMapper implements ResponseMapper<List<Plugin>> {

	@Override
	public List<Plugin> map(XmlResponse xml) {
		Integer count = xml.evaluateAsInteger("count(//plugin)");
		List<Plugin> plugins = new ArrayList<Plugin>(count);
		for (int i = 1; i <= count; i++) {
			// @formatter:off
			plugins.add(new Plugin()
				.setShortName(xml.evaluateAsString("//plugin[" + i + "]/shortName"))
				.setLongName(xml.evaluateAsString("//plugin[" + i + "]/longName"))
				.setVersion(xml.evaluateAsString("//plugin[" + i + "]/version"))
				.setUrl(xml.evaluateAsString("//plugin[" + i + "]/url"))
				.setEnabled(xml.evaluateAsBoolean("//plugin[" + i + "]/enabled"))
			);
			// @formatter:on
		}

		return plugins;
	}

}
