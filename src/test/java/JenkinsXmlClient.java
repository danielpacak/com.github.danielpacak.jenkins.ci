

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.codehaus.jackson.map.ObjectMapper;
@Deprecated
public class JenkinsXmlClient {

	private String host;
	private Integer port;
	private HttpClient httpClient;

	public JenkinsXmlClient(String host, Integer port) {
		this.host = host;
		this.port = port;
		this.httpClient = new HttpClient();
	}

	public void createJob(String jobName, InputStream jobDefinitionXml)
			throws Exception {
		PostMethod post = new PostMethod("http://" + host + ":" + port
				+ "/createItem?name=" + jobName);
		post.setRequestHeader("Content-Type", "application/xml");
		post.setRequestEntity(new InputStreamRequestEntity(jobDefinitionXml,
				"application/xml"));

		System.out.println(httpClient.executeMethod(post));
	}

	public void scheduleABuild(String jobName) throws Exception {
		GetMethod get = new GetMethod("http://" + host + ":" + port + "/job/"
				+ jobName + "/build?delay=0sec");
		int responseCode = httpClient.executeMethod(get);
		System.out.println("scheduleABuild.responseCode: " + responseCode);
	}

	public void scheduleABuild(String jobName, Map<String, String> paramsMap)
			throws Exception {
		JsonParams jsonParams = new JsonParams(paramsMap);
		String jsonString = jsonParams.toJson();

		PostMethod post = new PostMethod("http://" + host + ":" + port
				+ "/job/" + jobName + "/build?delay=0sec");
		post.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		String jsonEncoded = EncodingUtil.formUrlEncode(
				new NameValuePair[] { new NameValuePair("json", jsonString) },
				"UTF-8");
		post.setRequestEntity(new StringRequestEntity(jsonEncoded));

		System.out.println(httpClient.executeMethod(post));
	}

	public Long getLastBuildId(String jobName) throws Exception {
		System.out.println("Getting the last buid id for job " + jobName);
		GetMethod getMethod = new GetMethod("http://" + host + ":" + port
				+ "/job/" + jobName
				+ "/api/xml?xpath=//lastBuild/number");
		int responseCode = httpClient.executeMethod(getMethod);
		if (responseCode == 200) {
			String response = getMethod.getResponseBodyAsString();
			System.out.println("RRRRR: "+ response);
			Pattern p = Pattern.compile("<number>(.+?)</number>");
			Matcher m = p.matcher(response);
			m.find();
			return Long.valueOf(m.group(1));
		} else {
			return null;
		}
	}

	// http://localhost:8080/job/customComponent1337616720328/1/api/xml?xpath=//result
	public String getBuildStatus(String jobName, Long buildId) throws Exception {
		// status= `curl
		// http://localhost:8080/job/repoA/$buildId/api/xml?xpath=//result`
		GetMethod getMethod = new GetMethod("http://" + host + ":" + port
				+ "/job/" + jobName + "/" + buildId + "/api/xml?xpath=//result");
		System.out.println("Getting build status: " + getMethod.getURI());
		int responseCode = httpClient.executeMethod(getMethod);
		if (responseCode == 200) {
			return getMethod.getResponseBodyAsString();
		} else {
			return null;
		}
	}

	class JsonParams {
		Collection<JsonParam> parameter = new ArrayList<JsonParam>();

		public JsonParams() {
		}

		public JsonParams(Map<String, String> paramsMap) {
			for (Entry<String, String> entry : paramsMap.entrySet()) {
				parameter.add(new JsonParam(entry.getKey(), entry.getValue()));
			}

		}

		JsonParams(Collection<JsonParam> parameter) {
			this.parameter = parameter;
		}

		public Collection<JsonParam> getParameter() {
			return parameter;
		}

		void add(String name, String value) {
			this.parameter.add(new JsonParam(name, value));
		}

		public String toJson() {
			ObjectMapper om = new ObjectMapper();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				om.writeValue(out, this);
				return new String(out.toByteArray());
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot serialize this object to json string", e);
			}
		}

	}

	class JsonParam {
		String name;
		String value;

		public JsonParam(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
	}

}
