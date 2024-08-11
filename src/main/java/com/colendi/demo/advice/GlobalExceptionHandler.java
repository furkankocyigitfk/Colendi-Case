package com.colendi.demo.advice;

import com.colendi.demo.exception.MicroException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .findFirst()
                .orElse("Validation error");
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(MicroException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MicroException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
