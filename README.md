# Jenkins Java API (com.danielpacak.jenkins.ci.core)

This project is a Java library for communicating with the [Jenkins REST API](https://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API).

## TOC

* [Examples](#Examples)
 * [Creating a job](#creating-a-job)
 * [Triggering a build](#triggering-a-build)
 * [Triggering a build and waiting for its completion](#triggering-a-build-and-waiting-for-its-completion)
 * [Deleting a job](#deleting-a-job)
* [Packages](#packages)
* [Using Jenkins Java API in Your Build](#using-jenkins-java-api-in-your-build)

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

### Creating a job
The following example creates a new job called `release.and.deploy.jenkins.ci.client` using an XML configuration
template stored as a class path resource file `job/template/free-style-project.xml`.
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
JobConfiguration config = new ClassPathJobConfiguration("job/template/free-style-project.xml");
Job job = jobService.createJob("release.and.deploy.jenkins.ci.client", config); 
```
The `job/template/free-style-project.xml` configuration template may look as follows:
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

### Triggering a build
The following example triggers a build of a given job.
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
Job job = null; // I assume that you know how to create a job (see the previous examples)
jobService.triggerBuild(job);
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

### Using the `VariableSubstitutorJobConfiguration` class
Once you start creating more useful Jenkins jobs, you'll find the `VariableSubstitutorJobConfiguration`
class handy.
```java
Map<String, Object> values = new HashMap<>();
values.put("cloneUrl", "https://github.com/user/repo.git");
JobConfiguration config = new VariableSubstitutorJobConfiguration(
    new ClassPathJobConfiguration("job/template/maven3-project.xml"), values
);
```
The class substitutes all the variables within the job configuration returned by the decorated
`JobConfiguration` (the `ClassPathJobConfiguration` class in our case).

### 

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

## Using Jenkins Java API in Your Build
Developers may use several different kinds of build system to compile/package their code.
The library can be used with any of them. For any code snippet below, please substitute the version
given with the version of Jenkins Java API you wish to use.

### Maven
The library is not yet deployed to the maven central repository, so to use it, you need to fetch the
source code, install it into your local maven repository (`~/.m2/repository`) and add the following
snippet to the `<dependencies />` section of the project's `pom.xml` file.

```xml
<dependency>
	<groupId>com.danielpacak.jenkins.ci</groupId>
	<artifactId>com.danielpacak.jenkins.ci.core</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```
