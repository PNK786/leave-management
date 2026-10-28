package com.project.Leave.Service.DTO;

import com.project.Leave.Service.entity.LeaveType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class LeaveRequestDTO
{
    @NotNull(message = "empId is Required")
    @Column(name = "employee_id", nullable = false)
    private Long empId;
    @NotBlank(message = "Reason is Required")
    private String reason;
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date cannot be in the past")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

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
