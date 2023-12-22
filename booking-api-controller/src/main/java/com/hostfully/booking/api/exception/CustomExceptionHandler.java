package com.hostfully.booking.api.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ExceptionResource> objectNotFoundException(ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResource(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResource> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationExceptionResource(
                        ex.getStatusCode().value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getErrorCount(),
                        ex.getAllErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .toList()));
    }

    @ExceptionHandler(ParameterValidationException.class)
    public ResponseEntity<ValidationExceptionResource> parameterValidationException(ParameterValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationExceptionResource(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        1,
                        Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessExceptionResource> businessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new BusinessExceptionResource(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage()));
    }
}
