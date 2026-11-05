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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<LeaveResponseDTO> leaveRequest(@Valid @RequestBody LeaveRequestDTO request,@AuthenticationPrincipal Jwt jwt) {

        Long employeeId =  Long.valueOf(jwt.getClaimAsString("employeeId"));
        LeaveResponseDTO empResponse = leaveManagementService.applyLeave(request,employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(empResponse);
    }

    @GetMapping("/leaves/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<LeaveResponseDTO>> getMyLeaves(@AuthenticationPrincipal Jwt jwt) {
        Long employeeId = Long.valueOf(jwt.getClaimAsString("employeeId"));
        return ResponseEntity.ok(leaveManagementService.allLeaveRequests(employeeId));
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

    @GetMapping("/leaves/balance")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<LeaveBalanceDTO> getBalance(@AuthenticationPrincipal Jwt jwt) {
        Long employeeId = Long.valueOf(jwt.getClaimAsString("employeeId"));
        return ResponseEntity.ok(leaveManagementService.getBalance(employeeId));
    }

}
