package com.project.Leave.Service.DTO;

import com.project.Leave.Service.entity.LeaveType;
import com.project.Leave.Service.validation.ValidDateRange;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidDateRange
public class LeaveRequestDTO {

    @NotBlank(message = "Reason is Required")
    private String reason;
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date cannot be in the past")
    private LocalDate endDate;
    @NotNull(message = "leaveType is required")
    private LeaveType leaveType;


    public LeaveRequestDTO() {
    }

    public LeaveRequestDTO(String reason, LocalDate startDate, LocalDate endDate, LeaveType leaveType) {
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }
}
