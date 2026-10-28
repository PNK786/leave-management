package com.project.Leave.Service.repository;

import com.project.Leave.Service.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveManagementRepository extends JpaRepository<Leave,Long> {
}
