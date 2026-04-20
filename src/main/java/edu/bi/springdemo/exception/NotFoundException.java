package edu.bi.springdemo.exception;

public class NotFoundException extends RuntimeException {
  private NotFoundException(String message){
    super(message);
  }

  public static NotFoundException create(String message){
    return new NotFoundException(message);
  }
}
