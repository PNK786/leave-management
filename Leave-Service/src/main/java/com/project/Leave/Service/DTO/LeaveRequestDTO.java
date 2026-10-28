package com.project.Leave.Service.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.Leave.Service.entity.LeaveType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class LeaveRequestDTO
{
    @NotNull(message = "empId is Required")
    @Positive(message = "Employee id must be positive")
    private Long empId;
    @NotBlank(message = "Reason is Required")
    private String reason;
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date cannot be in the past")
    private LocalDate endDate;
    @NotNull(message = "leaveType is required")
    private LeaveType leaveType;

    @JsonIgnore
    @AssertTrue(message = "End date must be on or after start date")
    public boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) return true; // @NotNull reports the missing one
        return !endDate.isBefore(startDate);
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
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

    public LeaveRequestDTO() {
    }

    public LeaveRequestDTO(Long empId, String reason, LocalDate startDate, LocalDate endDate, LeaveType leaveType) {
        this.empId = empId;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
    }
}
