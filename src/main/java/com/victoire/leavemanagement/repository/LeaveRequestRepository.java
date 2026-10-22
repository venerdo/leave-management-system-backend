package com.victoire.leavemanagement.repository;

import com.victoire.leavemanagement.model.LeaveRequest;
import com.victoire.leavemanagement.model.LeaveRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, UUID> {

    List<LeaveRequest> findByStatusOrderByStartDateAsc(
            LeaveRequestStatus status);

    List<LeaveRequest> findByStartDateBetweenOrderByStartDateAsc(
            LocalDate start,
            LocalDate end);

    boolean existsByManagerAndStartDateAndStatusNot(
            com.victoire.leavemanagement.model.Manager manager,
            LocalDate startDate,
            LeaveRequestStatus status);

    long countByStartDate(LocalDate startDate);

    long countByStatus(LeaveRequestStatus status);

    @Query("SELECT lr.status, COUNT(lr) " +
        "FROM LeaveRequest lr GROUP BY lr.status")
    List<Object[]> countLeaveRequestsByStatus();

    @Modifying
    @Query("UPDATE LeaveRequest lr " +
        "SET lr.status = 'REJECTED' " +
        "WHERE lr.manager.id = :managerId " +
        "AND lr.startDate = :date " +
        "AND lr.status != 'APPROVED'")
    int cancelLeaveRequestsForManagerOnDate(
            @Param("managerId") UUID managerId,
            @Param("date") LocalDate date);
}