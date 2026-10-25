package com.project.employeService.Exception;

public class EmailExists  extends  RuntimeException{

    public EmailExists(String message){
        super(message);
    }
}
