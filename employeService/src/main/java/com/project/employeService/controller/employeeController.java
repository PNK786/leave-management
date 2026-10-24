package com.project.employeService.controller;

import com.project.employeService.DTO.EmployeeRequestDTO;
import com.project.employeService.DTO.EmployeeResponseDTO;
import com.project.employeService.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api")
public class employeeController {

    private EmployeeService employeeService;

    public employeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponseDTO> postEmployee(@RequestBody EmployeeRequestDTO request){
       EmployeeResponseDTO response= employeeService.CreateEmployee(request);
       return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/emp/{id}")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable BigInteger id){
      EmployeeResponseDTO byId=new  EmployeeResponseDTO();
        byId = employeeService.getEmpById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(byId);
    }

}
