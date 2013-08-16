# Jenkins Java API (com.danielpacak.jenkins.ci.core)

[![Build Status](https://buildhive.cloudbees.com/job/danielpacak/job/jenkins-ci-client/badge/icon)](https://buildhive.cloudbees.com/job/danielpacak/job/jenkins-ci-client/)
This project is a Java library for communicating with the [Jenkins REST API](https://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API).

* [Examples](#examples)
 * [Creating a job and launching a build](#creating-a-job-and-launching-a-build)
 * [Triggering a build](#triggering-a-build)
 * [Triggering a build and waiting for its completion](#triggering-a-build-and-waiting-for-its-completion)
 * [Deleting a job](#deleting-a-job)
* [Packages](#packages)
* [Downloading](#downloading)

## Examples

### Authenticating
```java
// Basic authentication
JenkinsClient client = new JenkinsClient("localhost", 8080);
client.setCredentials("user", "passw0rd");
```
```java
// OAuth2 token authentication
JenkinsClient client = new JenkinsClient("localhost", 8080);
client.setOAuth2Token("SlAV32hkKG");
```

### Creating a job and launching a build
The following example creates a new job called `vacuum.my.room` using an XML configuration
template stored as a class path resource file `job/config/free-style.xml`.
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
JobConfiguration jobConfig = new ClassPathJobConfiguration("job/config/free-style.xml");
Job job = jobService.createJob("vacuum.my.room", jobConfig); 
```
The `job/config/free-style.xml` configuration template may look as follows:
```xml
<?xml version='1.0' encoding='UTF-8'?>
<project>
	<keepDependencies>false</keepDependencies>
	<properties />
	<scm class="hudson.scm.NullSCM" />
	<canRoam>false</canRoam>
	<disabled>false</disabled>
	<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
	<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
	<triggers />
	<concurrentBuild>false</concurrentBuild>
	<builders />
	<publishers />
	<buildWrappers />
</project>
```
To launch a build call the `triggerBuild()` method of the `JobService` class:
```java
Long buildNumber = jobService.triggerBuild(job);
```
If your project is [parameterized](https://wiki.jenkins-ci.org/display/JENKINS/Parameterized+Build)
you would rather call the `triggerBuild()` method of the `JobService` class with an additional parameter
which is a map of parameters/values:
```java
Map<String, String> parameters = mapOf("FIRST_NAME", "Daniel", "LAST_NAME", "Pacak");
Long buildNumber = jobService.triggerBuild(job, parameters);
```

### Triggering a build and waiting for its completion
The following example triggers a build of a given job and blocks until the build has completed.
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
Job job = null; // I assume that you know how to create a job (see the previous examples)
Build build = jobService.triggerBuildAndWait(job);
System.out.printf("Build status: %s%n", build.getStatus());
```
It's also possible to limit the time of waiting for the completion of the build by specifying
the timeout. In the example below the method will throw the `InterruptedException` exception
if the build hasn't completed in less than 30 seconds.
```java
try {
  Build build = jobService.triggerBuildAndWait(job, 30, TimeUnit.SECONDS);
} catch (InterruptedException e) {
  System.err.println("Sorry guys but I cannot wait so long...");
}
```
### Deleting a job
The following example deletes a job with a given name.
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
jobService.deleteJob("job-to-be-deleted");
```

## Packages
The library is composed of 3 main packages.

### Core (com.danielpacak.jenkins.ci.core)
This package contains all the model classes representing the resources available through the API such as
jobs, builds, and configurations. The model classes contain getters and setters for all the elements
present in the Jenkins REST API XML response.

### Client (com.danielpacak.jenkins.ci.core.client)
This package contains classes which communicate with the Jenkins REST API over HTTP(S). The client
package is also responsible for converting XML responses to appropriate Java model classes as well as
generating request exceptions based on HTTP status codes.

### Service (com.danielpacak.jenkins.ci.core.service)
This package contains the classes that invoke the API and return model classes representing resources
that were created, read, updated, or deleted. Service classes are defined for the resources they
interact with such as `JobService`.

### Downloading
The library is not yet deployed to the maven central repository, so to use it, you need to fetch the
source code, install it into your local maven cache (usually `~/.m2/repository`) and add the following
snippet to the `<dependencies />` section of the project's `pom.xml` file.

```xml
<dependency>
	<groupId>com.danielpacak</groupId>
	<artifactId>jenkins.ci.core</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```
Alternatively you can use the latest build deployed to my private snapshots repository hosted on
[CloudBees](http://repository-pacak-daniel.forge.cloudbees.com/snapshot). Remember to add remote
repository details in the project's `pom.xml` file.
```
<repositories>
	<repository>
		<id>daniel.pacak.snapshots</id>
		<url>http://repository-pacak-daniel.forge.cloudbees.com/snapshot</url>
	</repository>
</repositories>
``` 