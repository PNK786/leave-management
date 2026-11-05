package com.project.employeService;

import com.project.employeService.entity.Employees;
import com.project.employeService.entity.Role;
import com.project.employeService.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EmployeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeServiceApplication.class, args);

    }

    CommandLineRunner seeAdmin(EmployeeRepository repo, PasswordEncoder encoder, @Value("${app.admin.password}")
    String adminPassword) {
        return args -> {
            if (!repo.existsByEmailIgnoreCase("manager@company.com")) {
                Employees admin = new Employees();
                admin.setName("MANAGER");
                admin.setEmail("manager@company.com");
                admin.setPassword(encoder.encode(adminPassword));
                admin.setRole(Role.valueOf("MANAGER"));
                repo.save(admin);
            }
        };
    }

}
