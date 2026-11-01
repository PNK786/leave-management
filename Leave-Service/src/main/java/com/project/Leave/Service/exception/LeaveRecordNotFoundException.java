package com.project.Leave.Service.exception;

public class LeaveRecordNotFoundException  extends RuntimeException{

    public LeaveRecordNotFoundException(String message){
        super(message);
    }
}
