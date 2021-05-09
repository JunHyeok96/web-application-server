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
      String url = headerMap.get("Request-Line");
      HttpRequestMethod method = HttpRequestUtils.parseMethod(url);
      if (method == HttpRequestMethod.GET) {
        getController.route(headerMap, out);
      } else if (method == HttpRequestMethod.POST) {
        postController.route(headerMap, br, out);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

}
