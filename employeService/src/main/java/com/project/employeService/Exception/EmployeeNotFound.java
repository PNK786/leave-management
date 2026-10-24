package com.project.employeService.Exception;

public class EmployeeNotFound extends RuntimeException
{
    public EmployeeNotFound(String message) {
        super(message);
    }
}
