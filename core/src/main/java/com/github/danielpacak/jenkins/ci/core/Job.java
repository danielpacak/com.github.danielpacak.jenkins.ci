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
package com.github.danielpacak.jenkins.ci.core;

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

   private Long nextBuildNumber;

   private Long lastBuildNumber;

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

   public Long getNextBuildNumber() {
      return nextBuildNumber;
   }

   public Job setNextBuildNumber(Long nextBuildNumber) {
      this.nextBuildNumber = nextBuildNumber;
      return this;
   }

   public Long getLastBuildNumber() {
      return lastBuildNumber;
   }

   public Job setLastBuildNumber(Long lastBuildNumber) {
	   this.lastBuildNumber = lastBuildNumber;
	   return this;
   }

}
