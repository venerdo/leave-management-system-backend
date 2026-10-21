package com.victoire.leavemanagement.repository;

import com.victoire.leavemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);
}