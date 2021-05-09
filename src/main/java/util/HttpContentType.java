package util;

public enum HttpContentType {
  HTML("text/html;charset=utf-8"), CSS("text/css");

  String contentType;

  HttpContentType(String contentType){
    this.contentType = contentType;
  }
}
