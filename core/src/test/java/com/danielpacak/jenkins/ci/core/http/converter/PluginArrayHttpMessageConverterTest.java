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
package com.danielpacak.jenkins.ci.core.http.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.danielpacak.jenkins.ci.core.Plugin;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;

/**
 * Tests for {@link PluginArrayHttpMessageConverter}.
 */
public class PluginArrayHttpMessageConverterTest {

	private PluginArrayHttpMessageConverter converter;

	@Before
	public void beforeTest() {
		this.converter = new PluginArrayHttpMessageConverter();
	}

	@Test
	public void canRead() throws Exception {
		assertTrue(converter.canRead(Plugin[].class));
		assertFalse(converter.canRead(Plugin.class));
		assertFalse(converter.canRead(Object.class));
	}

	@Test
	public void canWrite() throws Exception {
		assertFalse(converter.canWrite(Plugin[].class));
		assertFalse(converter.canWrite(Plugin.class));
		assertFalse(converter.canWrite(Object.class));
	}

	@Test
	public void read() throws Exception {
		// @formatter:off
		HttpInputMessage inputMessage = new TestHttpInputMessage(""
			+	"<localPluginManager>"
			+		"<plugin>"
			+			"<active>true</active>"
			+			"<bundled>true</bundled>"
			+			"<deleted>false</deleted>"
			+			"<downgradable>false</downgradable>"
			+			"<enabled>true</enabled>"
			+			"<hasUpdate>false</hasUpdate>"
			+			"<longName>Jenkins Mailer Plugin</longName>"
			+			"<pinned>false</pinned>"
			+			"<shortName>mailer</shortName>"
			+			"<url>http://wiki.jenkins-ci.org/display/JENKINS/Mailer</url>"
			+			"<version>1.5</version>"
			+		"</plugin>"
			+		"<plugin>"
			+			"<active>true</active>"
			+			"<bundled>true</bundled>"
			+			"<deleted>false</deleted>"
			+			"<downgradable>false</downgradable>"
			+			"<enabled>true</enabled>"
			+			"<hasUpdate>false</hasUpdate>"
			+			"<longName>External Monitor Job Type Plugin</longName>"
			+			"<pinned>false</pinned>"
			+			"<shortName>external-monitor-job</shortName>"
			+			"<url>http://wiki.jenkins-ci.org/display/JENKINS/Monitoring+external+jobs</url>"
			+			"<version>1.1</version>"
			+		"</plugin>"
			+	"</localPluginManager>");
		// @formatter:on

		Plugin[] plugins = converter.read(Plugin[].class, inputMessage);

		assertEquals(2, plugins.length);
		assertTrue(plugins[0].getActive());
		assertTrue(plugins[0].getBundled());
		assertFalse(plugins[0].getDeleted());
		assertFalse(plugins[0].getDowngradable());
		assertTrue(plugins[0].getEnabled());
		assertFalse(plugins[0].getHasUpdate());
		assertEquals("Jenkins Mailer Plugin", plugins[0].getLongName());
		assertFalse(plugins[0].getPinned());
		assertEquals("mailer", plugins[0].getShortName());
		assertEquals("http://wiki.jenkins-ci.org/display/JENKINS/Mailer", plugins[0].getUrl());
		assertEquals("1.5", plugins[0].getVersion());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void write() throws Exception {
		converter.write(new Plugin[] {}, null, null);
	}

}
