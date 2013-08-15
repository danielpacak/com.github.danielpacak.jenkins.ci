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
package com.danielpacak.jenkins.ci.core.http.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.danielpacak.jenkins.ci.core.ClassPathJobConfiguration;
import com.danielpacak.jenkins.ci.core.JobConfiguration;
import com.danielpacak.jenkins.ci.core.http.HttpInputMessage;
import com.danielpacak.jenkins.ci.core.http.HttpOutputMessage;
import com.danielpacak.jenkins.ci.core.util.Streams;

public class JobConfigurationHttpMessageConverter implements HttpMessageConverter<JobConfiguration> {

	@Override
	public boolean canRead(Class<?> clazz) {
		return JobConfiguration.class.equals(clazz);
	}

	@Override
	public boolean canWrite(Class<?> clazz) {
		// FIXME This is hacky!
		return JobConfiguration.class.equals(clazz) || ClassPathJobConfiguration.class.equals(clazz);
	}

	@Override
	public JobConfiguration read(Class<? extends JobConfiguration> clazz, HttpInputMessage inputMessage)
			throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		Streams.copy(inputMessage.getBody(), output);
		return new JobConfiguration() {
			@Override
			public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(output.toByteArray());
			}
		};
	}

	@Override
	public void write(JobConfiguration t, String contentType, HttpOutputMessage outputMessage) throws IOException {
		outputMessage.getHeaders().setContentType("application/xml");
		Streams.copy(t.getInputStream(), outputMessage.getBody());
	}

}
