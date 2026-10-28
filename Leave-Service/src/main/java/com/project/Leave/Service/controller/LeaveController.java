package com.project.Leave.Service.controller;

import com.project.Leave.Service.DTO.LeaveRequestDTO;
import com.project.Leave.Service.DTO.LeaveResponseDTO;
import com.project.Leave.Service.service.LeaveManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LeaveController
{

    private final LeaveManagementService leaveManagementService;

    public LeaveController(LeaveManagementService leaveManagementService) {
        this.leaveManagementService = leaveManagementService;
    }

    @PostMapping("/leaves")
    public ResponseEntity<LeaveResponseDTO> leaveRequest(@Valid  @RequestBody LeaveRequestDTO request){
           LeaveResponseDTO empResponse= leaveManagementService.applyLeave(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(empResponse);
    }
}
