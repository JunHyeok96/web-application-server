package webserver.controller;

import java.io.IOException;
import model.User;
import service.UserService;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class CreateUserController extends AbstractController {

  private UserService userService = new UserService();

  @Override
  protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    User user = new User(httpRequest.getRequestBody("userId"),
        httpRequest.getRequestBody("password"),
        httpRequest.getRequestBody("name"),
        httpRequest.getRequestBody("email"));
    userService.saveUser(user);
    httpResponse.sendRedirect("/index.html");
    httpResponse.processHeader();
  }
}
