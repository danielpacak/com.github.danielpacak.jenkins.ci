/*
 * #%L
 * Jenkins Java API
 * %%
 * Copyright (C) 2013 Daniel Pacak
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
