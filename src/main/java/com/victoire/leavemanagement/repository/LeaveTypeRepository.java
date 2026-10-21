package com.victoire.leavemanagement.repository;

import com.victoire.leavemanagement.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, UUID> {
    @Query("SELECT lt FROM LeaveType lt WHERE lt.managers IS EMPTY")
    List<LeaveType> findUnusedLeaveTypes();
}