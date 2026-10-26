package com.project.employeService.controller;

import com.project.employeService.DTO.EmployeeRequestDTO;
import com.project.employeService.DTO.EmployeeResponseDTO;
import com.project.employeService.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO request){
       EmployeeResponseDTO response= employeeService.createEmployee(request);
       return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/emp/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getEmpById(id));
    }

}
