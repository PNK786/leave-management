package com.project.Leave.Service.DTO;

import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.entity.LeaveType;

public class LeaveResponseDTO
{
   private Long leaveId;
   private String reason;
   private Long Duration;
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
        return Duration;
    }

    public void setDuration(Long duration) {
        Duration = duration;
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

    public LeaveResponseDTO(Long leaveId, String reason, Long duration, LeaveType leaveType, LeaveStatus status) {
        this.leaveId = leaveId;
        this.reason = reason;
        Duration = duration;
        this.leaveType = leaveType;
        this.status = status;
    }
}
