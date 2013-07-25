# Jenkins Java API (com.danielpacak.jenkins.ci.core)

This project is a Java library for communicating with the [Jenkins REST API](https://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API).

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

### Create a job
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

### Trigger a build
The following example triggers a build of a given job.
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
Job job = null; // I assume that you know how to create a job (see the previous examples)
jobService.triggerBuild(job);
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
