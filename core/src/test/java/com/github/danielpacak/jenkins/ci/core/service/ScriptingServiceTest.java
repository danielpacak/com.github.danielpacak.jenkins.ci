package com.github.danielpacak.jenkins.ci.core.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.danielpacak.jenkins.ci.core.GroovyResponse;
import com.github.danielpacak.jenkins.ci.core.GroovyScript;
import com.github.danielpacak.jenkins.ci.core.StringGroovyScript;
import com.github.danielpacak.jenkins.ci.core.client.JenkinsClient;
import com.github.danielpacak.jenkins.ci.core.service.ScriptingService;

/**
 * Tests for {@link ScriptingService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ScriptingServiceTest {

   @Mock
   private JenkinsClient client;

   @Mock
   private GroovyResponse response;

   private ScriptingService service;

   @Before
   public void beforeTest() {
      this.service = new ScriptingService(client);
   }

   @Test(expected = IllegalArgumentException.class)
   public void runScript_WithNullScript_ThrowsException() throws Exception {
      service.runScript(null);
   }

   @Test
   public void runScript() throws Exception {
      GroovyScript script = new StringGroovyScript("print 'hello'");
      when(client.postForObject("/scriptText", script, GroovyResponse.class)).thenReturn(response);
      assertEquals(response, service.runScript(script));
   }

}
