package webserver;

import java.io.IOException;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostController {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  public byte[] route(String url, Map<String, String> paramMap, Map<String, String> bodyMap) throws IOException {
    if (url.equals("/user/create")) {
      User user = User.paramsToUser(bodyMap);
      log.debug(user.toString());
    }
    return "Hello World".getBytes();
  }

}
