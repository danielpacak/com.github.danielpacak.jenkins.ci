package com.danielpacak.jenkins.ci.core;

import java.io.Serializable;

/**
 * Job model class.
 * 
 * @since 1.0.0
 */
public class Job implements Serializable {

	private static final long serialVersionUID = 6280410389222694298L;

	private String name;
	private String displayName;
	private String url;
	private Boolean buildable;
	private Boolean inQueue;
	private Integer nextBuildNumber;

	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @return this job
	 */
	public Job setName(String name) {
		this.name = name;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Job setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Job setUrl(String url) {
		this.url = url;
		return this;
	}

	public Boolean getBuildable() {
		return buildable;
	}

	public Job setBuildable(Boolean buildable) {
		this.buildable = buildable;
		return this;
	}

	public Boolean getInQueue() {
		return inQueue;
	}

	public Job setInQueue(Boolean inQueue) {
		this.inQueue = inQueue;
		return this;
	}

	public Integer getNextBuildNumber() {
		return nextBuildNumber;
	}

	public Job setNextBuildNumber(Integer nextBuildNumber) {
		this.nextBuildNumber = nextBuildNumber;
		return this;
	}

}
