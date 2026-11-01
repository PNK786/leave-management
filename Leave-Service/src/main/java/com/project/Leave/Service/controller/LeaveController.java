package com.project.Leave.Service.controller;

import com.project.Leave.Service.DTO.LeaveRequestDTO;
import com.project.Leave.Service.DTO.LeaveResponseDTO;
import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.service.LeaveManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.status(HttpStatus.FOUND).body(allLeaves);
    }

    @GetMapping("/leaves/pending/{status}")
    public ResponseEntity<List<LeaveResponseDTO>> accessPendingRequest(@PathVariable LeaveStatus status) {
        List<LeaveResponseDTO> pendingRequests = leaveManagementService.pendingLeaveRequests(status);
        return ResponseEntity.status(HttpStatus.FOUND).body(pendingRequests);
    }

    @PutMapping("/leaves/{id}/reject")
    public ResponseEntity<LeaveResponseDTO> approveLeaveRequest(@PathVariable Long id) {
        LeaveResponseDTO rejectRequest = leaveManagementService.rejectLeave(id);
        return ResponseEntity.status(HttpStatus.OK).body(rejectRequest);
    }

    @PutMapping("/leaves/{id}/approve")
    public ResponseEntity<LeaveResponseDTO> rejectLeaveRequest(@PathVariable Long id) {
        LeaveResponseDTO rejectRequest = leaveManagementService.approveLeave(id);
        return ResponseEntity.status(HttpStatus.OK).body(rejectRequest);
    }

}
