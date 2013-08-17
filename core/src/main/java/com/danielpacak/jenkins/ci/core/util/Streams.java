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
package com.danielpacak.jenkins.ci.core.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public final class Streams {

   private static final int EOF = -1;

   /**
    * The default buffer size {@value} to use for {@link #copyLarge(InputStream, OutputStream)}.
    */
   private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

   private Streams() {
   }

   /**
    * Get the content of an {@link InputStream} as a {@link String} using the default character encoding of the
    * platform.
    * <p>
    * This method buffers the input internally, so there is no need to use {@link BufferedInputStream}.
    * 
    * @param input the {@link InputStream} to read from
    * @return content string
    */
   public static String toString(InputStream input) throws IOException {
      StringWriter output = new StringWriter();

      copy(new InputStreamReader(input), output);
      return output.toString();
   }

   public static byte[] toByteArray(InputStream input) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy(input, output);
      return output.toByteArray();
   }

   public static int copy(Reader input, Writer output) throws IOException {
      long count = copyLarge(input, output);
      if (count > Integer.MAX_VALUE) {
         return -1;
      }
      return (int) count;
   }

   public static long copyLarge(Reader input, Writer output) throws IOException {
      return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
   }

   public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
      long count = 0;
      int n = 0;
      while (EOF != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
         count += n;
      }
      return count;
   }

   public static int copy(InputStream input, OutputStream output) throws IOException {
      long count = copyLarge(input, output);
      if (count > Integer.MAX_VALUE) {
         return -1;
      }
      return (int) count;

   }

   public static long copyLarge(InputStream input, OutputStream output) throws IOException {
      return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
   }

   public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
      long count = 0;
      int n = 0;
      while (EOF != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
         count += n;
      }
      return count;
   }

   public static int copy(byte[] input, OutputStream output) throws IOException {
      return copy(new ByteArrayInputStream(input), output);
   }

   public static int copy(String input, OutputStream output) throws IOException {
      return copy(input.getBytes(), output);
   }

}
