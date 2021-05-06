package webserver;

import java.io.IOException;
import java.util.Map;
import util.HttpRequestMethod;

public class Controller {

  private static final GetController getController = new GetController();

  public byte[] route(String url, Map<String, String> paramMap, HttpRequestMethod method) throws IOException {
    if(method == HttpRequestMethod.GET){
      return getController.route(url, paramMap);
    }
    throw new RuntimeException("지원하지 않는 http 메서드 입니다.");
  }


}
