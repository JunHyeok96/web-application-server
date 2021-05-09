package webserver.controller;

import java.io.IOException;
import model.User;
import service.UserService;
import webserver.HttpHeader;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class ListUserController extends AbstractController {

  private UserService userService = new UserService();

  @Override
  protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    userService.findUserList();
    if (isLogined(httpRequest)) {
      StringBuilder sb = new StringBuilder();
      for (User user : userService.findUserList()) {
        sb.append(user.getName());
        sb.append("\n");
      }
      httpResponse.responseBody(sb.toString().getBytes());
    } else {
      httpResponse.sendRedirect("/user/login.html");
    }
    httpResponse.processHeader();
  }

  private boolean isLogined(HttpRequest httpRequest) {
    String cookie = HttpRequestUtils
        .parseCookies(httpRequest.getHeader(HttpHeader.COOKIE.getHeader())).get("logined");
    return Boolean.parseBoolean(cookie);
  }
}
