package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestMethod;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private Socket connection;

  private static final GetController getController = new GetController();
  private static final PostController postController = new PostController();

  public RequestHandler(Socket connectionSocket) {
    this.connection = connectionSocket;
  }

  public void run() {
    log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
        connection.getPort());

    try (InputStream in = connection.getInputStream(); OutputStream out = connection
        .getOutputStream()) {
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      Map<String, String> headerMap = IOUtils.readHeader(br);
      String url = headerMap.get("Header");
      String requestPath = HttpRequestUtils.parseRequestPath(url);
      HttpRequestMethod method = HttpRequestUtils.parseMethod(url);
      Map<String, String> paramMap = HttpRequestUtils.parseParameter(url);
      log.debug(headerMap.toString());
      byte[] body = null;
      if(method == HttpRequestMethod.GET){
        body = getController.route(requestPath, paramMap);
      }else if(method == HttpRequestMethod.POST){
        int contentLength = HttpRequestUtils.parseContentLength(headerMap.get("Content-Length"));
        String inputBody = IOUtils.readData(br, contentLength);
        Map<String, String> bodyMap = HttpRequestUtils.parseQueryString(inputBody);
        body = postController.route(requestPath, paramMap, bodyMap);
      }
      DataOutputStream dos = new DataOutputStream(out);
      response200Header(dos, body.length);
      responseBody(dos, body);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }


  private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
    try {
      dos.writeBytes("HTTP/1.1 200 OK \r\n");
      dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
      dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
      dos.writeBytes("\r\n");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void responseBody(DataOutputStream dos, byte[] body) {
    try {
      dos.write(body, 0, body.length);
      dos.flush();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
