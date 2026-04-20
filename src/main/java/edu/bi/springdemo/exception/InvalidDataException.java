package edu.bi.springdemo.exception;

public class InvalidDataException extends RuntimeException {
    private InvalidDataException(String message){
        super(message);
    }

    public static InvalidDataException create(String message){
        return new InvalidDataException(message);
    }
}