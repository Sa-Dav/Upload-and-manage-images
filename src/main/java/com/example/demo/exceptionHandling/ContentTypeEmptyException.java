package com.example.demo.exceptionHandling;

public class ContentTypeEmptyException extends RuntimeException {
    public ContentTypeEmptyException(String message) {
        super(message);
    }
}
