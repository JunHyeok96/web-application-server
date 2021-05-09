package webserver;

public enum HttpHeader {
  SET_COOKIE("Set-Cookie"),
  CONTENT_LENGTH("Content-Length"),
  COOKIE("Cookie"),
  CONTENT_TYPE("Content-Type"),
  LOCATION("Location");

  String header;

  HttpHeader(String header){
    this.header = header;
  }

  public String getHeader() {
    return this.header;
  }
}
