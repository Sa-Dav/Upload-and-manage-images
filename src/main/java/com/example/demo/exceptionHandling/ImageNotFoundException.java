package com.example.demo.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class ImageNotFoundException extends RuntimeException{
    public ImageNotFoundException(String message) {
        super(message);
    }
}
