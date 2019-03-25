package com.almis.awe.component;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class AweHttpServletRequestWrapper extends HttpServletRequestWrapper {
  private final String body;

  /**
   * Constructor
   * @param request Servlet request
   * @throws IOException
   */
  public AweHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
    // So that other request method behave just like before
    super(request);
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
      char[] charBuffer = new char[128];
      int bytesRead = -1;
      while ((bytesRead = reader.read(charBuffer)) > 0) {
        stringBuilder.append(charBuffer, 0, bytesRead);
      }
    } catch (IOException exc) {
      throw new IOException(exc);
    }
    // Store request pody content in 'body' variable
    body = stringBuilder.toString();
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
    return new ServletInputStream() {
      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException("Not implemented");
      }

      /**
       * Read stream
       * @return Input stream
       * @throws IOException
       */
      public int read() throws IOException {
        return byteArrayInputStream.read();
      }
    };
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

  /**
   * Use this method to read the request body N times
   * @return Body contents as string
   */
  public String getBody() {
    return this.body;
  }
}
