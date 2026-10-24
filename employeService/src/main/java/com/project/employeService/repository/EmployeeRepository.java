package com.project.employeService.repository;

import com.project.employeService.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface EmployeeRepository extends JpaRepository<Employees, BigInteger> {
}
