package webserver.controller;

import java.io.IOException;
import service.UserService;
import webserver.HttpHeader;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController extends AbstractController {

  private UserService userService = new UserService();

  @Override
  protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    boolean isSuccessLogin = userService
        .login(httpRequest.getRequestBody("userId"), httpRequest.getRequestBody(("password")));
    if (isSuccessLogin) {
      httpResponse.sendRedirect("/index.html");
      httpResponse.addHeader(HttpHeader.SET_COOKIE.getHeader(), "logined=true");
      httpResponse.processHeader();
      return;
    }
    httpResponse.sendRedirect("/user/login_failed.html");
    httpResponse.addHeader(HttpHeader.SET_COOKIE.getHeader(), "logined=false");
    httpResponse.processHeader();
  }
}

