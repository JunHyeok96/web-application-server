package util;

public enum HttpStatus {
  OK("HTTP/1.1 200 OK \r\n"), FOUND("HTTP/1.1 302 Found \r\n");

  String status;

  HttpStatus(String status){
    this.status = status;
  }

  public String getStatus(){
    return this.status;
  }
}
