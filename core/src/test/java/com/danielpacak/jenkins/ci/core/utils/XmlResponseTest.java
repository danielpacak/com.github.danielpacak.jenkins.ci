package com.danielpacak.jenkins.ci.core.utils;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Tests for {@link XmlResponse}.
 */
public class XmlResponseTest {

	@Test
	public void testEvaluateAsString() throws Exception {
		XmlResponse xmlResponse = new XmlResponse("<result>SUCCESS</result>");
		assertEquals("SUCCESS", xmlResponse.evaluateAsString("//result/text()"));

		xmlResponse = new XmlResponse(
				"<parent><result>SUCCESS</result></parent>");
		assertEquals("SUCCESS", xmlResponse.evaluateAsString("//result/text()"));

		assertNull(new XmlResponse("<nodeName/>")
				.evaluateAsString("//nodeName/text()"));
	}

	@Test
	public void testEvaluateAsBoolean() throws Exception {
		assertEquals(Boolean.TRUE,
				new XmlResponse("<building>true</building>")
						.evaluateAsBoolean("//building/text()"));
		assertEquals(Boolean.FALSE,
				new XmlResponse("<building>false</building>")
						.evaluateAsBoolean("//building/text()"));

		assertEquals(Boolean.TRUE,
				new XmlResponse("<parent><building>TRUE</building></parent>")
						.evaluateAsBoolean("//building/text()"));
	}

}
