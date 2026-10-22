package com.victoire.leavemanagement.repository;

import com.victoire.leavemanagement.model.HandoverNote;
import com.victoire.leavemanagement.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HandoverNoteRepository extends JpaRepository<HandoverNote, UUID> {
    
    boolean existsByLeaveRequest(LeaveRequest leaveRequest);

    @Query("SELECT h FROM HandoverNote h WHERE h.leaveRequest.employee.staffNumber = :staffNumber ORDER BY h.writtenDate DESC")
    List<HandoverNote> findByEmployeeStaffNumber(@Param("staffNumber") String staffNumber);
}