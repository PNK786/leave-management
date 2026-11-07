package com.project.employeService.controller;

import com.project.employeService.DTO.LoginResponseDTO;
import com.project.employeService.entity.Employees;
import com.project.employeService.entity.LoginRequest;
import com.project.employeService.repository.EmployeeRepository;
import com.project.employeService.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtService service;

    public AuthController(AuthenticationManager manager, JwtService service) {
        this.manager = manager;
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequest request) {

        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = service.generateToken(authentication.getName());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}