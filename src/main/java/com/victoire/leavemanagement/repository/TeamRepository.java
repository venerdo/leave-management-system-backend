package com.victoire.leavemanagement.repository;

import com.victoire.leavemanagement.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    @Query("SELECT t FROM Team t WHERE NOT EXISTS (SELECT m FROM Manager m WHERE m.team = t) ORDER BY t.teamNumber ASC")
    List<Team> findFreeTeams();

    Optional<Team> findByTeamNumber(int teamNumber);
}