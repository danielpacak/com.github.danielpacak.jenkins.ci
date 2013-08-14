package com.danielpacak.jenkins.ci.core.util;

/**
 * Simple static methods to be called at the start of your own methods to verify
 * correct arguments and state.
 * 
 * @since 1.0.0
 */
public final class Preconditions {

	private Preconditions() {

	}

	public static <T> T checkArgumentNotNull(T argument, String errorMessage) {
		if (argument == null) {
			throw new IllegalArgumentException(errorMessage);
		}
		return argument;
	}

	public static void checkState(boolean expression, String errorMessage) {
		if (!expression) {
			throw new IllegalStateException(errorMessage);
		}
	}

}
