package com.project.Leave.Service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex){
        Map<String,String> errors=new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err->errors.putIfAbsent(err.getField(),err.getDefaultMessage()));

        ValidationErrorResponse body= new ValidationErrorResponse(HttpStatus.BAD_REQUEST
                .value(),"Validation failed",errors);
        return ResponseEntity.badRequest().body(body);
    }
}
