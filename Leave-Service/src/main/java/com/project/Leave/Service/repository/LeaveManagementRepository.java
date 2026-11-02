package com.project.Leave.Service.repository;

import com.project.Leave.Service.entity.Leave;
import com.project.Leave.Service.entity.LeaveStatus;
import com.project.Leave.Service.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LeaveManagementRepository extends JpaRepository<Leave,Long> {

    List<Leave> findByEmployeeId(Long employeeId);
    List<Leave> findBystatus(LeaveStatus status);

    List<Leave> findByEmployeeIdAndLeaveTypeAndStatusIn(
            Long employeeId, LeaveType leaveType, Collection<LeaveStatus> statuses);
}
