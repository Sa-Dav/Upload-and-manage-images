package com.example.demo.exceptionHandling;

public class ResizeNotWorkingException extends RuntimeException {
    public ResizeNotWorkingException(String message) {
        super(message);
    }
}
