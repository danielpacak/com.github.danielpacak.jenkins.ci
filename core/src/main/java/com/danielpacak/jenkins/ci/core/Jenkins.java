package com.danielpacak.jenkins.ci.core;

import java.io.Serializable;

/**
 * Jenkins model class.
 * 
 * @since 1.0.0
 */
public class Jenkins implements Serializable {

	private static final long serialVersionUID = 3830206808903450100L;

	private String nodeName;

	private String nodeDescription;

	private Integer numExecutors;

	private MODE mode;

	private Boolean useSecurity;

	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName
	 * @return this jenkins
	 */
	public Jenkins setNodeName(String nodeName) {
		this.nodeName = nodeName;
		return this;
	}

	public String getNodeDescription() {
		return nodeDescription;
	}

	/**
	 * @param nodeDescription
	 * @return this jenkins
	 */
	public Jenkins setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
		return this;
	}

	public Integer getNumExecutors() {
		return numExecutors;
	}

	/**
	 * @param numExecutors
	 * @return this jenkins
	 */
	public Jenkins setNumExecutors(Integer numExecutors) {
		this.numExecutors = numExecutors;
		return this;
	}

	public MODE getMode() {
		return mode;
	}

	/**
	 * @param mode
	 * @return this jenkins
	 */
	public Jenkins setMode(MODE mode) {
		this.mode = mode;
		return this;
	}

	public Boolean getUseSecurity() {
		return useSecurity;
	}

	public void setUseSecurity(Boolean useSecurity) {
		this.useSecurity = useSecurity;
	}

	public static enum MODE {
		NORMAL;
	}

}
