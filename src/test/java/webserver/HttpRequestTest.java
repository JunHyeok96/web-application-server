package webserver;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class HttpRequestTest {

  private String testDirectory = "./src/test/resources/";

  @Test
  public void parseHeaderAndParamsForGet() throws IOException {
    InputStream in = new FileInputStream(testDirectory+"Http_GET.txt");
    HttpRequest request = new HttpRequest(in);

    assertEquals(HttpMethod.valueOf("GET"), request.getMethod());
    assertEquals("/user/create", request.getPath());
    assertEquals("keep-alive", request.getHeader("Connection"));
    assertEquals("javajigi", request.getParameter("userId"));
  }

  @Test
  public void parseHeaderAndParamsForPost() throws IOException {
    InputStream in = new FileInputStream(testDirectory+"Http_POST.txt");
    HttpRequest request = new HttpRequest(in);

    assertEquals(HttpMethod.valueOf("POST"), request.getMethod());
    assertEquals("/user/create", request.getPath());
    assertEquals("keep-alive", request.getHeader("Connection"));
    assertEquals("javajigi", request.getRequestBody("userId"));
  }
}
