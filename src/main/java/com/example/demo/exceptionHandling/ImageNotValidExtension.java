package com.example.demo.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class ImageNotValidExtension extends RuntimeException {

    public ImageNotValidExtension(String message) {
        super(message);
    }
}
