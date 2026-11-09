package com.project.Leave.Service.service;

import com.project.Leave.Service.DTO.LeaveBalanceDTO;
import com.project.Leave.Service.DTO.LeaveRequestDTO;
import com.project.Leave.Service.DTO.LeaveResponseDTO;
import com.project.Leave.Service.entity.Leave;
import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.entity.LeaveType;
import com.project.Leave.Service.exception.LeaveRecordNotFoundException;
import com.project.Leave.Service.exception.LeaveResponseException;
import com.project.Leave.Service.repository.LeaveManagementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaveManagementService {

    private final LeaveManagementRepository leaveManagementRepository;

    public LeaveManagementService(LeaveManagementRepository leaveManagementRepository) {
        this.leaveManagementRepository = leaveManagementRepository;
    }

    public LeaveResponseDTO applyLeave(LeaveRequestDTO request,Long employeeId) {
        Leave stored = new Leave();
        stored.setEmployeeId(employeeId);
        stored.setReason(request.getReason());
        stored.setLeaveType(request.getLeaveType());
        stored.setStartDate(request.getStartDate());
        stored.setEndDate(request.getEndDate());
        stored.setStatus(LeaveStatus.PENDING);

        Leave leaveResponse = leaveManagementRepository.save(stored);
        LeaveResponseDTO response = new LeaveResponseDTO();
        response.setLeaveId(leaveResponse.getId());
        response.setEmployeeId(leaveResponse.getEmployeeId());
        response.setStartDate(leaveResponse.getStartDate());
        response.setEndDate(leaveResponse.getEndDate());
        response.setDuration(ChronoUnit.DAYS.between(stored.getStartDate(), stored.getEndDate()) + 1);
        response.setLeaveType(leaveResponse.getLeaveType());
        response.setReason(leaveResponse.getReason());
        response.setStatus(leaveResponse.getStatus());
        return response;
    }

    public List<LeaveResponseDTO> allLeaveRequests(Long employeeId) {
        List<Leave> leaves = leaveManagementRepository.findByEmployeeId(employeeId);
        if (leaves.isEmpty()) {
            throw new LeaveRecordNotFoundException("no Leave record found with this id" + employeeId);
        }
        return leaves.stream().map(leave -> toResponse(leave))
                .toList();
    }

    private LeaveResponseDTO toResponse(Leave leave) {
        LeaveResponseDTO dto = new LeaveResponseDTO();
        dto.setLeaveId(leave.getId());
        dto.setReason(leave.getReason());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setEmployeeId(leave.getEmployeeId());
        dto.setDuration(ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1);
        dto.setLeaveType(leave.getLeaveType());
        dto.setStatus(leave.getStatus());
        return dto;
    }

    public List<LeaveResponseDTO> pendingLeaveRequests(LeaveStatus status) {
        List<Leave> byLeaveStatus = leaveManagementRepository.findBystatus(status);
//        if (byLeaveStatus.isEmpty()) {
//            throw new LeaveRecordNotFoundException("no Leave record found with this status  " + status);
//        }
        return byLeaveStatus.stream().map(this::toResponse)
                .toList();
    }

    public LeaveResponseDTO approveLeave(Long id) {
        Leave leave = leaveManagementRepository.findById(id).
                orElseThrow(() -> new LeaveRecordNotFoundException("Leave request not found with id " + id));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new LeaveResponseException("only pending leaves can be Approved");
        }
        leave.setStatus(LeaveStatus.APPROVED);
        Leave rejected = leaveManagementRepository.save(leave);
        return toResponse(rejected);
    }

    public LeaveResponseDTO rejectLeave(Long id) {
        Leave leave = leaveManagementRepository.findById(id).
                orElseThrow(() -> new LeaveRecordNotFoundException("Leave request not found with id " + id));
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new LeaveResponseException("only pending leaves can be rejected");
        }
        leave.setStatus(LeaveStatus.REJECTED);
        Leave rejected = leaveManagementRepository.save(leave);
        return toResponse(rejected);
    }

    public int usedDays(Long employeeId, LeaveType leaveType, int year) {
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);

        return leaveManagementRepository.findByEmployeeIdAndLeaveTypeAndStatusIn(
                        employeeId, leaveType, List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED))
                .stream().mapToInt(n -> {
                    LocalDate from = n.getStartDate().isBefore(yearStart) ? yearStart : n.getStartDate();
                    LocalDate to = n.getEndDate().isAfter(yearEnd) ? yearEnd : n.getEndDate();

                    if (to.isBefore(from)) return 0;
                    return (int) ChronoUnit.DAYS.between(from, to) + 1;
                })
                .sum();
    }

    public LeaveBalanceDTO getBalance(Long employeeId) {
        int year = LocalDate.now().getYear();
        Map<LeaveType, Integer> remaining = new EnumMap<>(LeaveType.class);

        for (LeaveType type : LeaveType.values()) {
            remaining.put(type, type.getYearlyQuota() - usedDays(employeeId, type, year));
        }

        return new LeaveBalanceDTO(employeeId, year, remaining);
    }
}
