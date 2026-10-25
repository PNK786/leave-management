package com.project.employeService.Exception;

public class MethodArgumentNotValidException extends RuntimeException
{
   public MethodArgumentNotValidException(String message){
       super(message);
   }
}
