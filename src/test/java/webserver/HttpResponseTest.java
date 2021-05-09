package webserver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.junit.Test;

public class HttpResponseTest {

  private String testDirectory = "./src/test/resources/";

  @Test
  public void responseForward() throws Exception {
    HttpResponse response = new HttpResponse(createOutputStream("Http_response.txt"));
    response.forward("/index.html");
    response.processHeader();
  }

  @Test
  public void responseRedirect() throws Exception {
    HttpResponse response = new HttpResponse(createOutputStream("index.html"));
    response.sendRedirect("/index.html");
    response.processHeader();
  }

  @Test
  public void responseCookie() throws Exception {
    HttpResponse response = new HttpResponse(createOutputStream("index.html"));
    response.sendRedirect("/index.html");
    response.addHeader(HttpHeader.SET_COOKIE.getHeader(), "logined=true");
    response.processHeader();
  }

  private OutputStream createOutputStream(String filename) throws FileNotFoundException {
    return new FileOutputStream(testDirectory + filename);
  }
}
