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

	public void setDowngradable(Boolean downgradable) {
		this.downgradable = downgradable;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getHasUpdate() {
		return hasUpdate;
	}

	public void setHasUpdate(Boolean hasUpdate) {
		this.hasUpdate = hasUpdate;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public Boolean getPinned() {
		return pinned;
	}

	public void setPinned(Boolean pinned) {
		this.pinned = pinned;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
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

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static enum DynamicLoad {
		MAYBE
	}

}
