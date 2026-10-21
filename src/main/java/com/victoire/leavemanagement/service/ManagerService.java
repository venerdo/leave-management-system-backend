package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.Manager;
import com.victoire.leavemanagement.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
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