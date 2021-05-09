package webserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.CreateUserController;
import webserver.controller.ListUserController;
import webserver.controller.LoginController;

public class RequestHandler extends Thread {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private static Map<String, Controller> controllerMap = new HashMap<>();

  private Socket connection;


  public RequestHandler(Socket connectionSocket) {
    this.connection = connectionSocket;
  }

  public void run() {
    log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
        connection.getPort());
    try (InputStream in = connection.getInputStream(); OutputStream out = connection
        .getOutputStream()) {
      HttpRequest request = new HttpRequest(in);
      HttpResponse response = new HttpResponse(out);
      if (!controllerMap.containsKey(request.getPath())) {
        getDefaultPath(request.getPath(), response);
        return;
      }
      Controller controller = controllerMap.get(request.getPath());
      controller.service(request, response);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void getDefaultPath(String path, HttpResponse response) throws IOException {
    if (!isStaticFile(path)) {
      response.sendRedirect("/index.html");
      response.processHeader();
      return;
    }
    getStaticFile(path, response);
  }

  private boolean isStaticFile(String path) {
    File file = new File("./webapp" + path);
    return file.isFile();
  }

  private void getStaticFile(String path, HttpResponse response) throws IOException {
    if (path.endsWith(".html")) {
      response.addHeader(HttpHeader.CONTENT_TYPE.getHeader(), HttpContentType.HTML.getContentType());
    } else if (path.endsWith(".css")) {
      response.addHeader(HttpHeader.CONTENT_TYPE.getHeader(), HttpContentType.CSS.getContentType());
    }
    response.forward(path);
    response.processHeader();
  }


  public static void setUp() {
    controllerMap.put("/user/login", new LoginController());
    controllerMap.put("/user/create", new CreateUserController());
    controllerMap.put("/user/list", new ListUserController());
  }
}
