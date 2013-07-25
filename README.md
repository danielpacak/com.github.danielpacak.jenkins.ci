# Jenkins Java API (com.danielpacak.jenkins.ci.client)

This project is a Java library for communicating with the [Jenkins REST API](https://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API).

## Examples

### Authenticating
```java
// Basic authentication
JenkinsClient client = new JenkinsClient("localhost", 8080");
client.setCredentials("user", "passw0rd");
```
```java
// OAuth2 token authentication
JenkinsClient client = new JenkinsClient("localhost", 8080");
client.setOAuth2Token("SlAV32hkKG");
```

### Create a job
The following example creates a new job called `release.and.deploy.jenkins.ci.client` using an XML configuration
template stored as a class path resource file `job/template/free-style-project.xml`.
```java
JenkinsClient client = new JenkinsClient("localhost", 8080");
JobService jobService = new JobService(client);
Job job = jobService.createJob("release.and.deploy.jenkins.ci.client", new JobConfiguration("classpath:job/template/free-style-project.xml")); 
```
The `job/template/free-style-project.xml` configuration template can look as follows:
```xml
<project></project>
```
