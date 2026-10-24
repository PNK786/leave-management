package com.project.employeService.service;

import com.project.employeService.DTO.EmployeeRequestDTO;
import com.project.employeService.DTO.EmployeeResponseDTO;
import com.project.employeService.Exception.EmployeeNotFound;
import com.project.employeService.entity.Employees;
import com.project.employeService.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeResponseDTO CreateEmployee(EmployeeRequestDTO request) {

        Employees emp = new Employees();

        emp.setName(request.getName());
        emp.setEmail(request.getEmail());
        emp.setPassword(request.getPassword());
        emp.setRole(request.getRole());

        Employees savedEmp = employeeRepository.save(emp);

        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setId(savedEmp.getId());
        response.setName(savedEmp.getName());
        response.setEmail(savedEmp.getEmail());
        response.setRole(savedEmp.getRole());
        return response;
    }

    public EmployeeResponseDTO getEmpById(BigInteger id) {
        Employees found = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFound("Employee not found with Employee Id" + id));

        EmployeeResponseDTO foundEmp = new EmployeeResponseDTO();
        foundEmp.setId(found.getId());
        foundEmp.setName(found.getName());
        foundEmp.setEmail(found.getEmail());
        foundEmp.setRole(found.getRole());
        return foundEmp;
    }

}
