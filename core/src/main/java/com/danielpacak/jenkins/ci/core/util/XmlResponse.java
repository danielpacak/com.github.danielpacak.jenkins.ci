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
package com.danielpacak.jenkins.ci.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlResponse {

	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private XPathFactory xPathFactory;
	private XPath xPath;

	private Document document;

	public XmlResponse(String content) throws IOException {
		this(new ByteArrayInputStream(content.getBytes()));
	}

	public XmlResponse(InputStream content) throws IOException {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			xPathFactory = XPathFactory.newInstance();
			xPath = xPathFactory.newXPath();

			document = builder.parse(content);
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		} catch (SAXException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Evaluate the XPath expression in the context of this XML response.
	 * 
	 * @param expression
	 *            expression to be evaluated
	 * @return the result of evaluating the expression
	 * @throws Exception
	 */
	public String evaluateAsString(String expression) {
		Preconditions.checkArgumentNotNull(expression, "Expression cannot be null");
		try {
			XPathExpression expr = xPath.compile(expression);
			String rawResult = (String) expr.evaluate(document, XPathConstants.STRING);
			return (rawResult != null && !"".equals(rawResult)) ? rawResult : null;
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Boolean evaluateAsBoolean(String expression) {
		String rawResult = evaluateAsString(expression);
		return rawResult != null ? Boolean.valueOf(rawResult) : null;
	}

	public Integer evaluateAsInteger(String expression) {
		String rawResult = evaluateAsString(expression);
		return rawResult != null ? Integer.valueOf(rawResult) : null;
	}

	public Long evaluateAsLong(String expression) {
		String rawResult = evaluateAsString(expression);
		return rawResult != null ? Long.valueOf(rawResult) : null;
	}

}
