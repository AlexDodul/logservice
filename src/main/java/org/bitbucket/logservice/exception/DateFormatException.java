package org.bitbucket.logservice.exception;

import org.springframework.http.HttpStatus;

public class DateFormatException extends RuntimeException {

  private final String message;

  private final int statusCode;

  @Override
  public String getMessage() {
    return message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public DateFormatException() {
    super();
    this.message = "Неправильный формат даты";
    this.statusCode = HttpStatus.BAD_REQUEST.value();
  }

  public DateFormatException(String message) {
    super(message);
    this.message = message;
    this.statusCode = HttpStatus.BAD_REQUEST.value();
  }

  public DateFormatException(String message, int statusCode) {
    super(message);
    this.message = message;
    this.statusCode = statusCode;
  }
}
