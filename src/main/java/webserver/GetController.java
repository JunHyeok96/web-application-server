package webserver;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import model.User;
import util.IOUtils;

public class GetController {

  public byte[] route(String url, Map<String, String> paramMap) throws IOException {
    if (url.endsWith(".html")) {
      return getHtml(url);
    } else if (url.equals("/user/create")) {
      User user = User.paramsToUser(paramMap);
      return user.toString().getBytes();
    }
    return "Hello World".getBytes();
  }

  public byte[] getHtml(String url) throws IOException {
    if (url.equals("/index.html")) {
      return IOUtils.readFile(new File("./webapp/index.html"));
    } else if (url.equals("/user/form.html")) {
      return IOUtils.readFile(new File("./webapp/user/form.html"));
    }
    throw new RuntimeException("올바른 경로가 아닙니다.");
  }
}
