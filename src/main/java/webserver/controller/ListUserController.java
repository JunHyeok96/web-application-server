package webserver.controller;

import java.io.IOException;
import model.User;
import service.UserService;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class ListUserController extends AbstractController {

  private UserService userService = new UserService();

  @Override
  protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    userService.findUserList();
    if (httpRequest.isLogined()) {
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
}
