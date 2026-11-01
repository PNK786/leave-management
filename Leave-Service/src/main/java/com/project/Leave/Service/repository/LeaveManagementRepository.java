package com.project.Leave.Service.repository;

import com.project.Leave.Service.entity.Leave;
import com.project.Leave.Service.entity.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveManagementRepository extends JpaRepository<Leave,Long> {

    List<Leave> findByEmployeeId(Long employeeId);
    List<Leave> findBystatus(LeaveStatus status);
}
