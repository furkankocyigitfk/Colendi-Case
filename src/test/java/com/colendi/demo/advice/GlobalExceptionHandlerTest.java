package com.colendi.demo.advice;

import com.colendi.demo.exception.MicroException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void handleValidationExceptions_givenMethodArgumentNotValidException_thenReturnStringResponseEntity() {
        FieldError fieldError = new FieldError("objectName", "field", "Default message");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<String> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("field: Default message", response.getBody());
    }

    @Test
    public void handleValidationExceptions_givenMicroException_thenReturnStringResponseEntity() {
        MicroException microException = new MicroException("Micro error message");
        ResponseEntity<String> response = exceptionHandler.handleValidationExceptions(microException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Micro error message", response.getBody());
    }
}

