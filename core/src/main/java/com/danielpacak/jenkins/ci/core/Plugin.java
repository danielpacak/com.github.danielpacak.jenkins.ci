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
package com.danielpacak.jenkins.ci.core;

import java.io.Serializable;

/**
 * Plugin model class.
 * 
 * @since 1.0.0
 */
public class Plugin implements Serializable {

	private static final long serialVersionUID = 189768219282490790L;

	private Boolean active;
	private Boolean bundled;
	private Boolean deleted;
	private Boolean downgradable;
	private Boolean enabled;
	private Boolean hasUpdate;
	private String longName;
	private Boolean pinned;
	private String shortName;
	private DynamicLoad supportsDynamicLoad;
	private String url;
	private String version;

	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active
	 * @return this plugin
	 */
	public Plugin setActive(Boolean active) {
		this.active = active;
		return this;
	}

	public Boolean getBundled() {
		return bundled;
	}

	/**
	 * @param bundled
	 * @return this plugin
	 */
	public Plugin setBundled(Boolean bundled) {
		this.bundled = bundled;
		return this;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted
	 * @return this plugin
	 */
	public Plugin setDeleted(Boolean deleted) {
		this.deleted = deleted;
		return this;
	}

	public Boolean getDowngradable() {
		return downgradable;
	}

	public Plugin setDowngradable(Boolean downgradable) {
		this.downgradable = downgradable;
		return this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Plugin setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public Boolean getHasUpdate() {
		return hasUpdate;
	}

	public Plugin setHasUpdate(Boolean hasUpdate) {
		this.hasUpdate = hasUpdate;
		return this;
	}

	public String getLongName() {
		return longName;
	}

	public Plugin setLongName(String longName) {
		this.longName = longName;
		return this;
	}

	public Boolean getPinned() {
		return pinned;
	}

	public Plugin setPinned(Boolean pinned) {
		this.pinned = pinned;
		return this;
	}

	public String getShortName() {
		return shortName;
	}

	public Plugin setShortName(String shortName) {
		this.shortName = shortName;
		return this;
	}

	public DynamicLoad getSupportsDynamicLoad() {
		return supportsDynamicLoad;
	}

	public void setSupportsDynamicLoad(DynamicLoad supportsDynamicLoad) {
		this.supportsDynamicLoad = supportsDynamicLoad;
	}

	public String getUrl() {
		return url;
	}

	public Plugin setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public Plugin setVersion(String version) {
		this.version = version;
		return this;
	}

	public static enum DynamicLoad {
		MAYBE
	}

}
