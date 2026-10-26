package com.project.employeService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionalHandler
{

    @ExceptionHandler(EmployeeNotFound.class)
    public ResponseEntity<ErrorResponse> EmployeeNotFound(EmployeeNotFound ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmailExists.class)
    public ResponseEntity<ErrorResponse> EmployeeExists(EmailExists ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
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
