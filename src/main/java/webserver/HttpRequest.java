package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {

  private final InputStream in;
  private HttpMethod method;
  private String path;
  private Map<String, String> headerMap;
  private Map<String, String> parameterMap;
  private Map<String, String> requestBody;

  public HttpRequest(InputStream in) throws IOException {
    this.in = in;
    parseRequest();
  }

  public HttpMethod getMethod() {
    return this.method;
  }

  public String getPath() {
    return this.path;
  }

  public String getHeader(String header) {
    return this.headerMap.get(header);
  }

  public String getParameter(String parameter) {
    return this.parameterMap.get(parameter);
  }

  public String getRequestBody(String key) {
    return this.requestBody.get(key);
  }

  public boolean isLogined() {
    return Boolean.parseBoolean(
        HttpRequestUtils.parseCookies(getHeader(HttpHeader.COOKIE.getHeader())).get("logined"));
  }

  private void parseRequest() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(this.in));
    String line = br.readLine();
    this.method = HttpRequestUtils.parseMethod(line);
    this.path = HttpRequestUtils.parseRequestPath(line);
    this.parameterMap = HttpRequestUtils.parseParameter(line);
    this.headerMap = new HashMap<>();
    while (!(line = br.readLine()).equals("")) {
      String[] header = line.split(":");
      this.headerMap.put(header[0].trim(), header[1].trim());
    }
    if (method.isPost()) {
      parseRequestBody(br, Integer.parseInt(this.getHeader(HttpHeader.CONTENT_LENGTH.getHeader())));
    }
  }

  private void parseRequestBody(BufferedReader br, int contentLength) throws IOException {
    String body = IOUtils.readData(br, contentLength);
    System.out.println(body);
    this.requestBody = HttpRequestUtils.parseQueryString(body);
  }
}
