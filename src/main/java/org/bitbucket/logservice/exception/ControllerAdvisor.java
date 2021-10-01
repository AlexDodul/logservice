package org.bitbucket.logservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityExistException.class)
  public ResponseEntity<Object> handleEntityExistException(EntityExistException ex) {
    log.error(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @NotNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NotNull HttpHeaders headers,
      @NotNull HttpStatus status,
      @NotNull WebRequest request
  ) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(DateFormatException.class)
  public ResponseEntity<Object> handleDateFormatException(DateFormatException e) {
    return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
  }

  @ExceptionHandler(NoValuePresentException.class)
  public ResponseEntity<Object> handleNoValuePresentException(NoValuePresentException e) {
    return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
  }
}
