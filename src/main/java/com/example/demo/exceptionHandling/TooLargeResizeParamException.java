package com.example.demo.exceptionHandling;

public class TooLargeResizeParamException extends RuntimeException {
    public TooLargeResizeParamException(String message) {
        super(message);
    }
}
