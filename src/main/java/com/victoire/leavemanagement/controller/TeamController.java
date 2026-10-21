package com.victoire.leavemanagement.controller;

import com.victoire.leavemanagement.model.Team;
import com.victoire.leavemanagement.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Team team) {
        String message = teamService.saveTeam(team);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Team>> getAll() {
        return new ResponseEntity<>(teamService.getAllTeams(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Team> team = teamService.getTeamById(id);
        if (team.isEmpty()) {
            return new ResponseEntity<>("The team with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(team.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Team team) {
        Optional<Team> updated = teamService.updateTeam(id, team);
        if (updated.isEmpty()) {
            return new ResponseEntity<>("The team with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = teamService.deleteTeam(id);
        if (message.equals("The team with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}