package com.project.Leave.Service.exception;

public class LeaveStatusConflictException extends RuntimeException
{
    public LeaveStatusConflictException(String message) {
        super(message);
    }
}
