# Jenkins Java API (com.danielpacak.jenkins.ci.core)

[![Build Status](https://buildhive.cloudbees.com/job/danielpacak/job/jenkins-ci-client/badge/icon)](https://buildhive.cloudbees.com/job/danielpacak/job/jenkins-ci-client/)
This project is a Java library for communicating with the [Jenkins REST API](https://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API).

* [Examples](#examples)
 * [Creating a job and launching a build](#creating-a-job-and-launching-a-build)
 * [Getting all jobs](#getting-all-jobs)
 * [Getting a job with the given name](#getting-a-job-with-the-given-name)
 * [Getting the configuration of the given job](#getting-the-configuration-of-the-given-job)
 * [Deleting a job with the given name](#deleting-a-job-with-the-given-name)
 * [Executing the given Groovy script](#executing-the-given-groovy-script)
* [Packages](#packages)
* [Downloading](#downloading)

## Examples

### Authenticating
```java
// Basic authentication
JenkinsClient client = new JenkinsClient("localhost", 8080);
client.setCredentials("user", "passw0rd");
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
The `job/config/free-style.xml` configuration template may look as follows.
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
To launch a build call the `triggerBuild()` method of the `JobService` class.
```java
Long buildNumber = jobService.triggerBuild(job);
```
If your project is [parameterized](https://wiki.jenkins-ci.org/display/JENKINS/Parameterized+Build)
you would rather call the `triggerBuild()` method of the `JobService` class with an additional parameter
which is a map of parameters/values.
```java
Map<String, Object> parameters = mapOf(
   "FIRST_NAME", "Daniel",
   "LAST_NAME", "Pacak",
   "IS_SMART", true,
   "SECRET_PASSWORD", "passw0rd");
Long buildNumber = jobService.triggerBuild(job, parameters);
```
The configuration template for a parameterized project may look as follows.
```xml
<project>
   <actions />
   <description></description>
   <keepDependencies>false</keepDependencies>
   <properties>
      <hudson.model.ParametersDefinitionProperty>
         <parameterDefinitions>
            <hudson.model.StringParameterDefinition>
               <name>FIRST_NAME</name>
               <description></description>
               <defaultValue>Daniel</defaultValue>
            </hudson.model.StringParameterDefinition>
            <hudson.model.StringParameterDefinition>
               <name>LAST_NAME</name>
               <description></description>
               <defaultValue>Pacak</defaultValue>
            </hudson.model.StringParameterDefinition>
            <hudson.model.BooleanParameterDefinition>
               <name>IS_SMART</name>
               <description></description>
               <defaultValue>true</defaultValue>
            </hudson.model.BooleanParameterDefinition>
            <hudson.model.PasswordParameterDefinition>
               <name>SECRET_PASSWORD</name>
               <description></description>
               <defaultValue>D+ug18M/zLBNdF+SEEOSMA==</defaultValue>
            </hudson.model.PasswordParameterDefinition>
         </parameterDefinitions>
      </hudson.model.ParametersDefinitionProperty>
   </properties>
   <scm class="hudson.scm.NullSCM" />
   <canRoam>true</canRoam>
   <disabled>false</disabled>
   <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
   <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
   <triggers />
   <concurrentBuild>false</concurrentBuild>
   <builders>
      <hudson.tasks.BatchFile>
         <command>echo %FIRST_NAME% %LAST_NAME% %IS_SMART% %SECRET_PASSWORD% </command>
      </hudson.tasks.BatchFile>
   </builders>
   <publishers />
   <buildWrappers />
</project>
```

### Getting all jobs
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
for (Job job : jobService.getJobs()) {
   System.out.printf("%s%n", job.getName());
}
```

### Getting a job with the given name
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
Job vacuumMyRoom = jobService.getJob("vacuum.my.room");
```

### Getting the configuration of the given job
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
Job vacuumMyRoom = jobService.getJob("vacuum.my.room");
JobConfiguration vacuumMyRoomConfig = jobService.getJobConfiguration(vacuumMyRoom);
System.out.println(Streams.toString(vacuumMyRoomConfig.getInputStream());
```

### Deleting a job with the given name
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
JobService jobService = new JobService(client);
jobService.deleteJob("job.to.be.deleted");
```

### Executing the given Groovy script
```java
JenkinsClient client = new JenkinsClient("localhost", 8080);
ScriptingService scriptingService = new ScriptingService(client);
GroovyScript script = new StringGroovyScript("print 'Hello, Jenkins!'");
GroovyResponse response = scriptingService.runScript(script);
System.out.println(Streams.toString(response.getInputStream())); 
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
This package contains classes that invoke the API and return model classes representing resources
that were created, read, updated, or deleted. Service classes are defined for the resources they
interact with. For example, the `JobService` class interacts with the `Job` resource.

### Downloading
The library has not yet been deployed to the [maven central repository](http://repo1.maven.org/maven/),
so to use it, you need to fetch the source code, install it into your [local maven repository](http://maven.apache.org/guides/introduction/introduction-to-repositories.html),
and add the following snippet to the `<dependencies />` section of your project's `pom.xml` file.

```xml
<dependency>
	<groupId>com.danielpacak</groupId>
	<artifactId>jenkins.ci.core</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```
Alternatively, you can use the latest build deployed to my private [snapshots](http://repository-pacak-daniel.forge.cloudbees.com/snapshot)
repository. Just remember to reference this repository in your project's `pom.xml` file.
```xml
<repositories>
	<repository>
		<id>daniel.pacak.snapshots</id>
		<url>http://repository-pacak-daniel.forge.cloudbees.com/snapshot</url>
	</repository>
</repositories>
```