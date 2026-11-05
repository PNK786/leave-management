package com.project.Leave.Service.controller;

import com.project.Leave.Service.DTO.LeaveBalanceDTO;
import com.project.Leave.Service.DTO.LeaveRequestDTO;
import com.project.Leave.Service.DTO.LeaveResponseDTO;
import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.service.LeaveManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaveController {

    private final LeaveManagementService leaveManagementService;

    public LeaveController(LeaveManagementService leaveManagementService) {
        this.leaveManagementService = leaveManagementService;
    }

    @PostMapping("/leaves")
    public ResponseEntity<LeaveResponseDTO> leaveRequest(@Valid @RequestBody LeaveRequestDTO request) {
        LeaveResponseDTO empResponse = leaveManagementService.applyLeave(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(empResponse);
    }

    @GetMapping("/leaves/my/{empId}")
    public ResponseEntity<List<LeaveResponseDTO>> getAllLeaveRequestByEmpId(@PathVariable Long empId) {
        List<LeaveResponseDTO> allLeaves = leaveManagementService.allLeaveRequests(empId);
        return ResponseEntity.ok(allLeaves);
    }

    @GetMapping("/leaves/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<LeaveResponseDTO>> accessPendingRequest() {
        List<LeaveResponseDTO> pendingRequests = leaveManagementService.pendingLeaveRequests(LeaveStatus.PENDING);
        return ResponseEntity.ok(pendingRequests);
    }

    @PutMapping("/leaves/{id}/reject")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<LeaveResponseDTO> rejectLeaveRequest(@PathVariable Long id) {
        LeaveResponseDTO rejectRequest = leaveManagementService.rejectLeave(id);
        return ResponseEntity.status(HttpStatus.OK).body(rejectRequest);
    }

    @PutMapping("/leaves/{id}/approve")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<LeaveResponseDTO> approveLeaveRequest(@PathVariable Long id) {
        LeaveResponseDTO rejectRequest = leaveManagementService.approveLeave(id);
        return ResponseEntity.status(HttpStatus.OK).body(rejectRequest);
    }

    @GetMapping("/leaves/balance/{empId}")
    public ResponseEntity<LeaveBalanceDTO> getBalance(@PathVariable Long empId) {

        LeaveBalanceDTO balanceLeaves = leaveManagementService.getBalance(empId);
        return ResponseEntity.ok(balanceLeaves);
    }

}
