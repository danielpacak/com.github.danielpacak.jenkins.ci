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

	public XmlResponse(String content) throws Exception {
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
