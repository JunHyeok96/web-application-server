package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.swing.text.Style;
import model.User;
import service.UserService;
import util.HttpRequestMethod;
import util.HttpRequestUtils;
import util.HttpResponseUtils;
import util.IOUtils;

public class GetController {

  private final UserService userService = new UserService();

  public void route(Map<String, String> headerMap, OutputStream out) throws IOException {
    String url = headerMap.get("Header");
    String requestPath = HttpRequestUtils.parseRequestPath(url);
    Map<String, String> paramMap = HttpRequestUtils.parseParameter(url);
    DataOutputStream dos = new DataOutputStream(out);
    byte[] body = "".getBytes();
    if (requestPath.endsWith(".html")) {
      body = getHtml(requestPath);
      HttpResponseUtils.response200Header(dos, body.length);
    } else if (requestPath.equals("/user/create")) {
      User user = User.paramsToUser(paramMap);
      body = user.toString().getBytes();
      HttpResponseUtils.response200Header(dos, body.length);
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
      } else {
        HttpResponseUtils.response302Header(dos, body.length, "/user/login.html");
      }
    }
    HttpResponseUtils.responseBody(dos, body);
  }

  public byte[] getHtml(String url) throws IOException {
    if (url.equals("/index.html")) {
      return IOUtils.readFile(new File("./webapp/index.html"));
    } else if (url.equals("/user/form.html")) {
      return IOUtils.readFile(new File("./webapp/user/form.html"));
    } else if (url.equals("/user/login.html")) {
      return IOUtils.readFile(new File("./webapp/user/login.html"));
    } else if (url.equals("/user/login_failed.html")) {
      return IOUtils.readFile(new File("./webapp/user/login_failed.html"));
    }
    throw new RuntimeException("올바른 경로가 아닙니다.");
  }
}
