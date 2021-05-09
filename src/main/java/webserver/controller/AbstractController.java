package webserver.controller;

import java.io.IOException;
import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class AbstractController implements Controller{

  @Override
  public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    HttpMethod method = httpRequest.getMethod();
    if(method == HttpMethod.GET){
      doGet(httpRequest, httpResponse);
    }else if(method == HttpMethod.POST){
      doPost(httpRequest, httpResponse);
    }
  }

  protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    throw new IOException("지원하지 않는 http method입니다.");
  }

  protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
    throw new IOException("지원하지 않는 http method입니다.");
  }
}
