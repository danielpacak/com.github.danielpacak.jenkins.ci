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
package com.danielpacak.jenkins.ci.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.danielpacak.jenkins.ci.core.Job;
import com.danielpacak.jenkins.ci.core.client.JenkinsClient;

public abstract class AbstractJenkinsIntegrationTest {

   private static String host = "localhost";

   private static Integer port = 8080;

   private static String user = "dpacak";

   private static String password = "passw0rd";

   @BeforeClass
   public static void beforeClass() {
      System.out.printf("Starting Jenkins CI on %s:%s...%n", host, port);
   }

   @AfterClass
   public static void afterClass() {
      System.out.println("Stopping Jenkins CI...");
   }

   protected JenkinsClient getJenkinsClient() {
      return new JenkinsClient("http", host, port, "/jenkins-war-1.523").setCredentials(user, password);
   }

   /**
    * Return a new instance of the {@link Job} class with a random name.
    * 
    * @return new job with a random name
    * @see #newRandomName()
    */
   protected Job newJobWithRandomName() {
      return new Job().setName(newRandomName());
   }

   /**
    * Return a new random name. The name has the following pattern {@code junit-currentTimeMillis}.
    * 
    * @return random name
    */
   protected String newRandomName() {
      return "junit-" + System.currentTimeMillis();
   }

}
