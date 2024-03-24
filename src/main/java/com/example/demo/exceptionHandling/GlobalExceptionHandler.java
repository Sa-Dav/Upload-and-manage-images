package com.example.demo.exceptionHandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> handleValidationException(MethodArgumentNotValidException exception) {
        List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        log.debug(exception.getMessage());
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ZeroInputFileException.class)
    public ResponseEntity<List<ValidationError>> handleZeroInputFileException() {
        ValidationError validationError = new ValidationError("InputData", "Zero input file");
        log.error("Zero input file");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyDatabaseException.class)
    public ResponseEntity<List<ValidationError>> handleEmptyDatabaseException() {
        ValidationError validationError = new ValidationError("EmptyDatabase", "Database is empty!");
        log.error("Database is empty!");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }
}