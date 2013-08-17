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
 * Jenkins model class.
 * 
 * @since 1.0.0
 */
public class Jenkins implements Serializable {

   private static final long serialVersionUID = 3830206808903450100L;

   private String nodeName;

   private String nodeDescription;

   private Integer numExecutors;

   private MODE mode;

   private Boolean useSecurity;

   public String getNodeName() {
      return nodeName;
   }

   /**
    * @param nodeName
    * @return this jenkins
    */
   public Jenkins setNodeName(String nodeName) {
      this.nodeName = nodeName;
      return this;
   }

   public String getNodeDescription() {
      return nodeDescription;
   }

   /**
    * @param nodeDescription
    * @return this jenkins
    */
   public Jenkins setNodeDescription(String nodeDescription) {
      this.nodeDescription = nodeDescription;
      return this;
   }

   public Integer getNumExecutors() {
      return numExecutors;
   }

   /**
    * @param numExecutors
    * @return this jenkins
    */
   public Jenkins setNumExecutors(Integer numExecutors) {
      this.numExecutors = numExecutors;
      return this;
   }

   public MODE getMode() {
      return mode;
   }

   /**
    * @param mode
    * @return this jenkins
    */
   public Jenkins setMode(MODE mode) {
      this.mode = mode;
      return this;
   }

   public Boolean getUseSecurity() {
      return useSecurity;
   }

   public Jenkins setUseSecurity(Boolean useSecurity) {
      this.useSecurity = useSecurity;
      return this;
   }

   public static enum MODE {
      NORMAL;
   }

}
