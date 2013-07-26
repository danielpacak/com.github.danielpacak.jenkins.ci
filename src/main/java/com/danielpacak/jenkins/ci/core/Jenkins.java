package com.danielpacak.jenkins.ci.core;

/**
 * Jenkins model class.
 * 
 * @since 1.0.0
 */
public class Jenkins {

	private String nodeName;
	private String nodeDescription;
	private Integer numExecutors;
	private MODE mode;
	private Boolean useSecurity;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeDescription() {
		return nodeDescription;
	}

	public void setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
	}

	public Integer getNumExecutors() {
		return numExecutors;
	}

	public void setNumExecutors(Integer numExecutors) {
		this.numExecutors = numExecutors;
	}

	public MODE getMode() {
		return mode;
	}

	public void setMode(MODE mode) {
		this.mode = mode;
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
