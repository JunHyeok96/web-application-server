package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpRequestUtils;
import util.HttpResponseUtils;
import util.IOUtils;

public class PostController {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
  private UserService userService = new UserService();

  public void route(Map<String, String> headerMap, BufferedReader br, OutputStream out) throws IOException {
    String url = headerMap.get("Request-Line");
    String requestPath = HttpRequestUtils.parseRequestPath(url);
    Map<String, String> paramMap = HttpRequestUtils.parseParameter(url);
    int contentLength = HttpRequestUtils.parseContentLength(headerMap.get("Content-Length"));
    String inputBody = IOUtils.readData(br, contentLength);
    Map<String, String> bodyMap = HttpRequestUtils.parseQueryString(inputBody);
    DataOutputStream dos = new DataOutputStream(out);
    byte[] body = "".getBytes();
    if (requestPath.equals("/user/create")) {
      User user = User.paramsToUser(bodyMap);
      userService.saveUser(user);
      log.debug("save : {}", user.toString());
      body = user.toString().getBytes();
      HttpResponseUtils.response302Header(dos, body.length, "/index.html");
    } else if (requestPath.equals("/user/login")) {
        boolean isSuccessLogin = userService.login(bodyMap.get("userId"), bodyMap.get("password"));
        if(isSuccessLogin){
          HttpResponseUtils.response302Header(dos, body.length, "/index.html");
          HttpResponseUtils.setCookie(dos, "logined", "true");
        }else{
          HttpResponseUtils.response302Header(dos, body.length, "/user/login_failed.html");
          HttpResponseUtils.setCookie(dos, "logined", "false");
        }
    }
  }
}
