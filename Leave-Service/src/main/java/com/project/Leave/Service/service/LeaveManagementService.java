package com.project.Leave.Service.service;

import com.project.Leave.Service.DTO.LeaveRequestDTO;
import com.project.Leave.Service.DTO.LeaveResponseDTO;
import com.project.Leave.Service.entity.Leave;
import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.repository.LeaveManagementRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class LeaveManagementService {

    private final LeaveManagementRepository leaveManagementRepository;

    public LeaveManagementService(LeaveManagementRepository leaveManagementRepository) {
        this.leaveManagementRepository = leaveManagementRepository;
    }

    public LeaveResponseDTO applyLeave(LeaveRequestDTO request) {

        Leave stored = new Leave();
        stored.setEmployeeId(request.getEmpId());
        stored.setReason(request.getReason());
        stored.setLeaveType(request.getLeaveType());
        stored.setStartDate(request.getStartDate());
        stored.setEndDate(request.getEndDate());
        stored.setStatus(LeaveStatus.PENDING);

        Leave leaveResponse =leaveManagementRepository.save(stored);
        LeaveResponseDTO response= new LeaveResponseDTO();

        response.setLeaveId(leaveResponse.getId());
        response.setDuration(ChronoUnit.DAYS.between(stored.getStartDate(),stored.getEndDate())+1);
        response.setLeaveType(leaveResponse.getLeaveType());
        response.setReason(leaveResponse.getReason());
        response.setStatus(leaveResponse.getStatus());

        return response;
    }
}
