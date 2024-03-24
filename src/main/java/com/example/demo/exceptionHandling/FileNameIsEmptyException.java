package com.example.demo.exceptionHandling;

public class FileNameIsEmptyException extends RuntimeException {
    public FileNameIsEmptyException(String message) {
        super(message);
    }
}
