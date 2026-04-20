package edu.bi.springdemo.exception;

public class UserDoesNotExistException extends RuntimeException {
    private UserDoesNotExistException(String message) {
        super(message);
    }

  public static UserDoesNotExistException create(String message){
    return new UserDoesNotExistException(message);
  }
}
