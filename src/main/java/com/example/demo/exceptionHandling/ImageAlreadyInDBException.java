package com.example.demo.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class ImageAlreadyInDBException extends RuntimeException {
    public ImageAlreadyInDBException(String message){
        super(message);
    };

}
