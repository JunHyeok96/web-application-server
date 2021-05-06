package webserver;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

public class GetController {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  public byte[] route(String url, Map<String, String> paramMap) throws IOException {
    if (url.equals("/index.html")) {
      return IOUtils.readFile(new File("./webapp/index.html"));
    } else if(url.equals("/user/form.html")){
      return IOUtils.readFile(new File("./webapp/user/form.html"));
    } if (url.equals("/user/create")) {
      User user = User.paramsToUser(paramMap);
      log.debug(user.toString());
    }
    return "Hello World".getBytes();
  }

}
