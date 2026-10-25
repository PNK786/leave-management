package com.project.employeService.Exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponse
{
    private int status;
    private String message;
    private Map<String,String> errors;
    private LocalDateTime timeStamp;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public ValidationErrorResponse(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timeStamp =LocalDateTime.now();
    }
}
