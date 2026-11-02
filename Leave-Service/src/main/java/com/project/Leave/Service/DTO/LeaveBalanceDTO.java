package com.project.Leave.Service.DTO;

import com.project.Leave.Service.entity.LeaveType;

import java.util.Map;

public class LeaveBalanceDTO
{
    private Long empId;
    private int year;
    private Map<LeaveType, Integer> remaining;


    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Map<LeaveType, Integer> getRemaining() {
        return remaining;
    }

    public void setRemaining(Map<LeaveType, Integer> remaining) {
        this.remaining = remaining;
    }

    public LeaveBalanceDTO(Long empId, int year, Map<LeaveType, Integer> remaining) {
        this.empId = empId;
        this.year = year;
        this.remaining = remaining;
    }
}
