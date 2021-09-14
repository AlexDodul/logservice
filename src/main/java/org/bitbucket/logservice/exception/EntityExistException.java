package org.bitbucket.logservice.exception;

public class EntityExistException extends RuntimeException {
  public EntityExistException(String message) {
    super(message);
  }
}
