package com.project.Leave.Service.service;

import com.project.Leave.Service.DTO.LeaveRequestDTO;
import com.project.Leave.Service.entity.Leave;
import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.repository.LeaveManagementRepository;
import org.springframework.stereotype.Service;

@Service
public class LeaveManagementService {

    private final LeaveManagementRepository leaveManagementRepository;

    public LeaveManagementService(LeaveManagementRepository leaveManagementRepository) {
        this.leaveManagementRepository = leaveManagementRepository;
    }

    public Leave applyLeave(LeaveRequestDTO request) {

        Leave stored = new Leave();
        stored.setEmployeeId(request.getEmpId());
        stored.setReason(request.getReason());
        stored.setLeaveType(request.getLeaveType());
        stored.setStartDate(request.getStartDate());
        stored.setEndDate(request.getEndDate());
//        stored.setStatus(LeaveStatus.PENDING);
        return leaveManagementRepository.save(stored);
    }
}
