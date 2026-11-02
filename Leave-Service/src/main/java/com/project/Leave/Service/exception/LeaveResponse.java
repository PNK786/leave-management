package com.project.Leave.Service.exception;

import java.time.LocalDate;

public class LeaveResponse
{
    private int Status;
    private String message;
    private LocalDate timeStamp;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDate timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LeaveResponse(int status, String message) {
        Status = status;
        this.message = message;
        this.timeStamp =LocalDate.now();
    }
}
