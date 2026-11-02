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

        ex.getBindingResult().getFieldErrors()
                .forEach(err->errors.putIfAbsent(err.getObjectName(),err.getDefaultMessage()));
        ValidationErrorResponse body= new ValidationErrorResponse(HttpStatus.BAD_REQUEST
                .value(),"Validation failed",errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(LeaveRecordNotFoundException.class)
    public ResponseEntity<LeaveNotFound> leaveNotFound(LeaveRecordNotFoundException ex){
        LeaveNotFound error = new LeaveNotFound(HttpStatus.NOT_FOUND.value(),ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(LeaveResponseException.class)
    public ResponseEntity<LeaveResponse> leaveStatusRejected(LeaveResponseException ex){
         LeaveResponse response= new LeaveResponse(HttpStatus.FORBIDDEN.value(),ex.getMessage());
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
