import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.danielpacak.jenkins.ci.client.JenkinsClient;
import com.danielpacak.jenkins.ci.client.JenkinsXmlClient;

public class LearningTest {

	@Test
	public void go() throws Exception {

		JenkinsClient jenkinsApi = new JenkinsXmlClient("localhost", 8080);

		String jobName = "customComponent" + new Date().getTime();
		InputStream jobDefinition = getJobDefinitionXml();

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap
				.put("GitUrl",
						"https://danielpacak@github.com/danielpacak/osgi-enterprise-webconsole.git");
		paramsMap.put("RevId", "master");
		paramsMap.put("PomSource", "G:\\playwithjenkins\\pom_template.xml");
		paramsMap.put("GID", "com.amadeus.groovy");
		paramsMap.put("AID", "my-scripts");
		paramsMap.put("VID", "0.0.1");

		// Creating a new job based on XML definition (see
		// src/main/resources/config.xml)
		jenkinsApi.createJob(jobName, jobDefinition);

		// Scheduling a build for the job created above
/*		jenkinsApi.scheduleABuild(jobName, paramsMap);

		Long lastBuildId = jenkinsApi.getLastBuildId(jobName);
		System.out.println("lastBuildId: " + lastBuildId);

		System.out.println("status: "
				+ jenkinsApi.getBuildStatus(jobName, lastBuildId));*/
	}

	InputStream getJobDefinitionXml() {
		return getClass().getClassLoader().getResourceAsStream(
				"job/definition/free-style-job-definition.xml");
	}

}
