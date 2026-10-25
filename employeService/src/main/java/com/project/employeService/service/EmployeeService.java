package com.project.employeService.service;

import com.project.employeService.DTO.EmployeeRequestDTO;
import com.project.employeService.DTO.EmployeeResponseDTO;
import com.project.employeService.Exception.EmailExists;
import com.project.employeService.Exception.EmployeeNotFound;
import com.project.employeService.entity.Employees;
import com.project.employeService.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {

        Employees emp = new Employees();
        emp.setName(request.getName());
        if(employeeRepository.existsByEmailIgnoreCase(request.getEmail())){
            throw new EmailExists("Email Id already exists ");
        }else {
            emp.setEmail(request.getEmail());
        }
        emp.setPassword(passwordEncoder.encode(request.getPassword()));
        emp.setRole("EMPLOYEE");

        Employees savedEmp = employeeRepository.save(emp);

        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setId(savedEmp.getId());
        response.setName(savedEmp.getName());
        response.setEmail(savedEmp.getEmail());
        response.setRole(savedEmp.getRole());
        return response;
    }

    public EmployeeResponseDTO getEmpById(Long id) {
        Employees found = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFound("Employee not found with Employee Id" + id));

        EmployeeResponseDTO foundEmp = new EmployeeResponseDTO();
        foundEmp.setId(found.getId());
        foundEmp.setName(found.getName());
        foundEmp.setEmail(found.getEmail());
        foundEmp.setRole(found.getRole());
        return foundEmp;
    }

}
