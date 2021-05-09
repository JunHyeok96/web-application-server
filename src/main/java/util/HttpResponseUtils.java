package util;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class HttpResponseUtils {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private HttpResponseUtils() {
  }

  public static void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
    try {
      dos.writeBytes("HTTP/1.1 200 OK \r\n");
      dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public static void setContentType(DataOutputStream dos, HttpContentType type) {
    try {
      dos.writeBytes("Content-Type: " + type.contentType + "\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public static void response302Header(DataOutputStream dos, int lengthOfBodyContent,
      String route) {
    try {
      dos.writeBytes("HTTP/1.1 302 Found \r\n");
      dos.writeBytes("Location: " + route + " \r\n");
      dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public static void setCookie(DataOutputStream dos, String key, String value) {
    try {
      dos.writeBytes("Set-Cookie: " + key + "=" + value + "\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  public static void responseBody(DataOutputStream dos, byte[] body) {
    try {
      dos.writeBytes("\r\n");
      dos.write(body, 0, body.length);
      dos.flush();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
