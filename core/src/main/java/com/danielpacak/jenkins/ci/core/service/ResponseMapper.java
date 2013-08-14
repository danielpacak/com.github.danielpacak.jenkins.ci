package com.danielpacak.jenkins.ci.core.service;

import com.danielpacak.jenkins.ci.core.util.XmlResponse;

/**
 * Strategy for mapping an {@link XmlResponse} to an instance of a model class.
 * 
 * @param <T>
 *            type of the model class
 */
public interface ResponseMapper<T> {

	T map(XmlResponse response);

}
