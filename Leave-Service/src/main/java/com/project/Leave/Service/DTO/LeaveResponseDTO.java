package com.project.Leave.Service.DTO;

import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.entity.LeaveType;

import java.time.LocalDate;

public class    LeaveResponseDTO
{
   private Long leaveId;
   private Long employeeId;
   private String reason;
   private LocalDate startDate;
   private LocalDate endDate;
   private Long duration;
   private LeaveType leaveType;
   private LeaveStatus status;

    public Long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Long leaveId) {
        this.leaveId = leaveId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        duration = duration;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public LeaveResponseDTO() {
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LeaveResponseDTO(Long leaveId, Long employeeId, String reason, LocalDate startDate, LocalDate endDate, Long duration, LeaveType leaveType, LeaveStatus status) {
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        duration = duration;
        this.leaveType = leaveType;
        this.status = status;
    }
}
