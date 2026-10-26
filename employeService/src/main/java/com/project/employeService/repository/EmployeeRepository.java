package com.project.employeService.repository;

import com.project.employeService.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employees, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<Employees> findByEmail(String email);
}
