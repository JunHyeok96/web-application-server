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

public class RequestHandler extends Thread {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private Socket connection;

  private Controller controller = new Controller();

  public RequestHandler(Socket connectionSocket) {
    this.connection = connectionSocket;
  }

  public void run() {
    log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
        connection.getPort());

    try (InputStream in = connection.getInputStream(); OutputStream out = connection
        .getOutputStream()) {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
      String line = "";
      String url = "";
      int lineCount = 0;
      while (!(line = bufferedReader.readLine()).equals("")) {
        if (lineCount++ == 0) {
          url = line;
        }
        log.debug(line);
      }
      String requestPath = HttpRequestUtils.parseRequestPath(url);
      HttpRequestMethod method = HttpRequestUtils.parseMethod(url);
      Map<String, String> paramMap = HttpRequestUtils.parseParameter(url);
      byte[] body = controller.route(requestPath, paramMap, method);
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
