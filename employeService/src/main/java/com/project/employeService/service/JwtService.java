package com.project.employeService.service;

import com.project.employeService.entity.Employees;
import com.project.employeService.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;
    private final EmployeeRepository employeeRepository;

    public JwtService(JwtEncoder jwtEncoder, EmployeeRepository employeeRepository) {
        this.jwtEncoder = jwtEncoder;
        this.employeeRepository = employeeRepository;
    }

    public String generateToken(String email) {
        Employees employee = employeeRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        Instant now = Instant.now();


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("employee-service")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(employee.getEmail())
                .claim("employeeId",employee.getId())
                .claim("roles",
                        List.of(employee.getRole().name())).build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();
    }
}
