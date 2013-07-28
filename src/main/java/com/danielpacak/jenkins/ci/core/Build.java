package com.danielpacak.jenkins.ci.core;

import java.io.Serializable;

/**
 * Build model class.
 * 
 * @since 1.0.0
 */
public class Build implements Serializable {

	private static final long serialVersionUID = 5391287361325427962L;

	private Long number;

	private Status status;

	public static enum Status {
		SUCCESS, PENDING, FAILED
	}

	public Long getNumber() {
		return number;
	}

	/**
	 * @param number
	 * @return this build
	 */
	public Build setNumber(Long number) {
		this.number = number;
		return this;
	}

	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 * @return this build
	 */
	public Build setStatus(Status status) {
		this.status = status;
		return this;
	}

}
