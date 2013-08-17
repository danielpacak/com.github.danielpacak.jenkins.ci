package com.danielpacak.jenkins.ci.core.http.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.danielpacak.jenkins.ci.core.client.JenkinsClientException;
import com.danielpacak.jenkins.ci.core.http.HttpHeaders;
import com.danielpacak.jenkins.ci.core.http.HttpStatus;

/**
 * Tests for {@link DefaultResponseErrorHandler}.
 */
public class DefaultResponseErrorHandlerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private DefaultResponseErrorHandler errorHandler;

	@Before
	public void beforeTest() {
		this.errorHandler = new DefaultResponseErrorHandler();
	}

	@Test
	public void hasError() throws Exception {
		assertTrue(errorHandler.hasError(new MockClientHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR)));
		assertTrue(errorHandler.hasError(new MockClientHttpResponse(HttpStatus.BAD_REQUEST)));
		assertFalse(errorHandler.hasError(new MockClientHttpResponse(HttpStatus.ACCEPTED)));
	}

	@Test
	public void handleError() throws Exception {
		thrown.expect(JenkinsClientException.class);
		errorHandler.handleError(new MockClientHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	class MockClientHttpResponse implements ClientHttpResponse {
		HttpStatus status;

		MockClientHttpResponse(HttpStatus status) {
			this.status = status;
		}

		@Override
		public HttpHeaders getHeaders() {
			return new HttpHeaders();
		}

		@Override
		public int getRawStatusCode() throws IOException {
			return status.value();
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return status;
		}

		@Override
		public String getStatusText() throws IOException {
			return null;
		}

		@Override
		public InputStream getBody() throws IOException {
			return null;
		}

		@Override
		public void close() {
		}
	}

}
