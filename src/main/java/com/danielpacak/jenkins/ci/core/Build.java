package com.danielpacak.jenkins.ci.core;


/**
 * Build model class.
 * 
 * @since 1.0.0
 */
public class Build {

	private Long number;
	private Status status;

	public static enum Status {
		SUCCESS, PENDING, FAILED
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
