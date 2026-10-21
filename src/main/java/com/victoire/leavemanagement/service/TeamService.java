package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.Team;
import com.victoire.leavemanagement.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public String saveTeam(Team team) {
        teamRepository.save(team);
        return "Team saved successfully";
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(UUID id) {
        return teamRepository.findById(id);
    }

    public Optional<Team> updateTeam(UUID id, Team team) {
        Optional<Team> existing = teamRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        Team t = existing.get();
        t.setTeamNumber(team.getTeamNumber());
        t.setName(team.getName());
        teamRepository.save(t);
        return Optional.of(t);
    }

    public String deleteTeam(UUID id) {
        if (!teamRepository.existsById(id)) {
            return "The team with that id does not exist";
        }
        teamRepository.deleteById(id);
        return "Team deleted successfully";
    }
}