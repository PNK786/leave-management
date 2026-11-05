package com.project.employeService.service;

import com.project.employeService.entity.Employees;
import com.project.employeService.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public EmployeeUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Employees emp = employeeRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return User.withUsername(emp.getEmail())
                .password(emp.getPassword())
                .roles(emp.getRole().toString().toUpperCase())
                .build();
    }
}
