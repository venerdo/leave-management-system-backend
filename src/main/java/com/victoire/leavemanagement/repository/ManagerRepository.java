package com.victoire.leavemanagement.repository;

import com.victoire.leavemanagement.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {
    @Query("SELECT m FROM Manager m JOIN m.leaveTypes lt WHERE LOWER(lt.name) = LOWER(:name)")
    List<Manager> findManagersByLeaveTypeName(@Param("name") String name);
}