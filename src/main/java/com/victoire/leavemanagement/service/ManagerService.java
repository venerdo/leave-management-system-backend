
package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.Manager;
import com.victoire.leavemanagement.model.LeaveType;
import com.victoire.leavemanagement.model.Team;
import com.victoire.leavemanagement.repository.ManagerRepository;
import com.victoire.leavemanagement.repository.LeaveTypeRepository;
import com.victoire.leavemanagement.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final TeamRepository teamRepository;

    public ManagerService(
            ManagerRepository managerRepository,
            LeaveTypeRepository leaveTypeRepository,
            TeamRepository teamRepository) {
        this.managerRepository = managerRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.teamRepository = teamRepository;
    }

    public String saveManager(Manager manager) {
        managerRepository.save(manager);
        return "Manager saved successfully";
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> getManagerById(UUID id) {
        return managerRepository.findById(id);
    }

    public List<Manager> getManagersByLeaveTypeName(String name) {
        return managerRepository.findManagersByLeaveTypeName(name);
    }

    public String addLeaveTypeToManager(UUID managerId, UUID leaveTypeId) {
        Optional<Manager> managerOpt = managerRepository.findById(managerId);

        if (managerOpt.isEmpty()) {
            return "The manager with that id does not exist";
        }

        Optional<LeaveType> leaveTypeOpt = leaveTypeRepository.findById(leaveTypeId);

        if (leaveTypeOpt.isEmpty()) {
            return "That leave type does not exist";
        }

        Manager manager = managerOpt.get();
        LeaveType leaveType = leaveTypeOpt.get();

        if (manager.getLeaveTypes().contains(leaveType)) {
            return "The manager already has that leave type";
        }

        manager.getLeaveTypes().add(leaveType);
        managerRepository.save(manager);

        return "Leave type added to the manager";
    }

    public String moveManagerToTeam(UUID managerId, int teamNumber) {
        Optional<Manager> managerOpt = managerRepository.findById(managerId);

        if (managerOpt.isEmpty()) {
            return "The manager with that id does not exist";
        }

        Optional<Team> teamOpt = teamRepository.findByTeamNumber(teamNumber);

        if (teamOpt.isEmpty()) {
            return "The team with that teamNumber does not exist";
        }

        Team newTeam = teamOpt.get();
        Manager manager = managerOpt.get();

        if (manager.getTeam() != null
                && manager.getTeam().getId().equals(newTeam.getId())) {
            return "Team changed successfully";
        }

        if (managerRepository.existsByTeam(newTeam)) {
            return "That team is taken by another manager";
        }

        manager.setTeam(newTeam);
        managerRepository.save(manager);

        return "Team changed successfully";
    }

    public Optional<Manager> updateManager(UUID id, Manager manager) {
        Optional<Manager> existing = managerRepository.findById(id);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Manager m = existing.get();
        m.setFirstName(manager.getFirstName());
        m.setLastName(manager.getLastName());
        m.setEmail(manager.getEmail());
        m.setTeam(manager.getTeam());

        managerRepository.save(m);
        return Optional.of(m);
    }

    public String deleteManager(UUID id) {
        if (!managerRepository.existsById(id)) {
            return "The manager with that id does not exist";
        }

        managerRepository.deleteById(id);
        return "Manager deleted successfully";
    }
}
