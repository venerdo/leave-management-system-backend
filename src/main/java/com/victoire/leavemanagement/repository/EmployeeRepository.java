package com.victoire.leavemanagement.repository;

import com.victoire.leavemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT e FROM LeaveRequest lr JOIN lr.employee e WHERE lr.manager.id = :managerId")
    List<Employee> findEmployeesByManagerId(@Param("managerId") UUID managerId);
}