package org.bitbucket.logservice.exception;

import org.springframework.http.HttpStatus;

public class NoValuePresentException extends RuntimeException {

  private final String message;

  private final int statusCode;

  @Override
  public String getMessage() {
    return message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public NoValuePresentException() {
    super();
    this.message = "No value present";
    this.statusCode = HttpStatus.NO_CONTENT.value();
  }

  public NoValuePresentException(String message) {
    super(message);
    this.message = message;
    this.statusCode = HttpStatus.NO_CONTENT.value();
  }

  public NoValuePresentException(String message, int statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }

  public NoValuePresentException(String message, HttpStatus statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode.value();
  }

}
