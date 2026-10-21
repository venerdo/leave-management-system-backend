package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.LeaveType;
import com.victoire.leavemanagement.repository.LeaveTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public String saveLeaveType(LeaveType leaveType) {
        leaveTypeRepository.save(leaveType);
        return "Leave type saved successfully";
    }

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    public Optional<LeaveType> getLeaveTypeById(UUID id) {
        return leaveTypeRepository.findById(id);
    }

    public List<LeaveType> getUnusedLeaveTypes() {
        return leaveTypeRepository.findUnusedLeaveTypes();
    }

    public Optional<LeaveType> updateLeaveType(UUID id, LeaveType leaveType) {
        Optional<LeaveType> existing = leaveTypeRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        LeaveType lt = existing.get();
        lt.setName(leaveType.getName());
        leaveTypeRepository.save(lt);
        return Optional.of(lt);
    }

    public String deleteLeaveType(UUID id) {
        if (!leaveTypeRepository.existsById(id)) {
            return "The leave type with that id does not exist";
        }
        leaveTypeRepository.deleteById(id);
        return "Leave type deleted successfully";
    }
}