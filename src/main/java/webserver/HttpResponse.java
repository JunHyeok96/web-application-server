package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import util.HttpStatus;
import util.IOUtils;

public class HttpResponse {

  private final Map<String, String> headerMap = new HashMap<>();
  private final DataOutputStream dataOutputStream;
  private byte[] responseBody = "".getBytes();
  private HttpStatus status = HttpStatus.OK;

  public HttpResponse(OutputStream outputStream) {
    this.dataOutputStream = new DataOutputStream(outputStream);
  }

  public void addHeader(String name, String value) {
    this.headerMap.put(name, value);
  }

  public String getHeader(String name){
    return this.headerMap.get(name);
  }

  public void forward(String path) throws IOException {
    this.responseBody = getStaticFile(path);
  }

  public void sendRedirect(String url) {
    this.status = HttpStatus.FOUND;
    this.headerMap.put(HttpHeader.LOCATION.getHeader(), url);
  }

  public void responseBody(byte[] body) {
    this.responseBody = body;
  }

  public byte[] getResponseBody() {
    return responseBody;
  }

  public void responseStatus(HttpStatus status) {
    this.status = status;
  }

  public void processHeader() throws IOException {
    dataOutputStream.writeBytes(this.status.getStatus());
    addHeader(HttpHeader.CONTENT_LENGTH.getHeader(), Integer.toString(this.responseBody.length));
    for (String key : this.headerMap.keySet()) {
      dataOutputStream.writeBytes(key + ": " + this.headerMap.get(key) + "\r\n");
    }
    dataOutputStream.writeBytes("\r\n");
    dataOutputStream.write(this.responseBody, 0, this.responseBody.length);
    dataOutputStream.flush();
  }

  private byte[] getStaticFile(String url) throws IOException {
    return IOUtils.readFile(new File("./webapp" + url));
  }
}
