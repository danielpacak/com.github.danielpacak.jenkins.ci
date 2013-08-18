package com.danielpacak.jenkins.ci.core;

import java.io.IOException;
import java.io.InputStream;

public interface GroovyResponse {

   InputStream getInputStream() throws IOException;

}
