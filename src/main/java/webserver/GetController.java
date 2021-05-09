package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import model.User;
import service.UserService;
import util.HttpContentType;
import util.HttpRequestUtils;
import util.HttpResponseUtils;
import util.IOUtils;

public class GetController {

  private final UserService userService = new UserService();

  public void route(Map<String, String> headerMap, OutputStream out) throws IOException {
    String url = headerMap.get("Request-Line");
    String requestPath = HttpRequestUtils.parseRequestPath(url);
    Map<String, String> paramMap = HttpRequestUtils.parseParameter(url);
    DataOutputStream dos = new DataOutputStream(out);
    byte[] body = "".getBytes();
    if (requestPath.endsWith(".html")) {
      body = getStaticFile(requestPath);
      HttpResponseUtils.response200Header(dos, body.length);
      HttpResponseUtils.setContentType(dos, HttpContentType.HTML);
    } else if (requestPath.endsWith(".css")) {
      body = getStaticFile(requestPath);
      HttpResponseUtils.response200Header(dos, body.length);
      HttpResponseUtils.setContentType(dos, HttpContentType.CSS);
    } else if (requestPath.equals("/user/create")) {
      User user = User.paramsToUser(paramMap);
      body = user.toString().getBytes();
      HttpResponseUtils.response200Header(dos, body.length);
      HttpResponseUtils.setContentType(dos, HttpContentType.HTML);
    } else if (requestPath.equals("/user/list")) {
      boolean isLogined = Boolean
          .parseBoolean(HttpRequestUtils.parseCookies(headerMap.get("Cookie")).get("logined"));
      if (isLogined) {
        StringBuilder sb = new StringBuilder();
        for (User user : userService.findUserList()) {
          sb.append(user.getName());
          sb.append("\n");
        }
        body = sb.toString().getBytes();
        HttpResponseUtils.response200Header(dos, body.length);
        HttpResponseUtils.setContentType(dos, HttpContentType.HTML);
      } else {
        HttpResponseUtils.response302Header(dos, body.length, "/user/login.html");
        HttpResponseUtils.setContentType(dos, HttpContentType.HTML);
      }
    }
    HttpResponseUtils.responseBody(dos, body);
  }

  public byte[] getStaticFile(String url) throws IOException {
    File file = new File("./webapp" + url);
    if (file.exists()) {
      return IOUtils.readFile(file);
    }
    throw new RuntimeException("올바른 경로가 아닙니다.");
  }

}
