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
package com.danielpacak.jenkins.ci.core.http;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HttpHeaders extends HashMap<String, List<String>> {

	private static final long serialVersionUID = 4258600664539135240L;

	public void add(String headerName, String headerValue) {
		if (!containsKey(headerName)) {
			put(headerName, new LinkedList<String>());
		}
		get(headerName).add(headerValue);
	}

	public void setContentType(String mediaType) {
		add("Content-Type", mediaType);
	}

	public void setUserAgent(String userAgent) {
		add("User-Agent", userAgent);
	}

	public String getContentType() {
		if (containsKey("Content-Type")) {
			if (!get("Content-Type").isEmpty()) {
				return get("Content-Type").iterator().next();
			}
		}
		return null;
	}

	public void setAuthorization(String authorization) {
		add("Authorization", authorization);
	}

	public Long getContentLength() {
		if (containsKey("Content-Length")) {
			if (!get("Content-Length").isEmpty()) {
				return Long.valueOf(get("Content-Length").iterator().next());
			}
		}
		return new Long(-1);
	}

}
