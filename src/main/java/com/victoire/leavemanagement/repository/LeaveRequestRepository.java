package com.victoire.leavemanagement.repository;

import com.victoire.leavemanagement.model.LeaveRequest;
import com.victoire.leavemanagement.model.LeaveRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {
    List<LeaveRequest> findByStatusOrderByStartDateAsc(LeaveRequestStatus status);
    List<LeaveRequest> findByStartDateBetweenOrderByStartDateAsc(java.time.LocalDate start, java.time.LocalDate end);
    boolean existsByManagerAndStartDateAndStatusNot(com.victoire.leavemanagement.model.Manager manager, java.time.LocalDate startDate, LeaveRequestStatus status);
}