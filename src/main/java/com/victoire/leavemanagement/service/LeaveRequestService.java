package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.LeaveRequest;
import com.victoire.leavemanagement.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public String saveLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.save(leaveRequest);
        return "Leave request saved successfully";
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public Optional<LeaveRequest> getLeaveRequestById(UUID id) {
        return leaveRequestRepository.findById(id);
    }

    public Optional<LeaveRequest> updateLeaveRequest(UUID id, LeaveRequest leaveRequest) {
        Optional<LeaveRequest> existing = leaveRequestRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        LeaveRequest lr = existing.get();
        lr.setStartDate(leaveRequest.getStartDate());
        lr.setStatus(leaveRequest.getStatus());
        lr.setDays(leaveRequest.getDays());
        lr.setReason(leaveRequest.getReason());
        lr.setEmployee(leaveRequest.getEmployee());
        lr.setManager(leaveRequest.getManager());
        leaveRequestRepository.save(lr);
        return Optional.of(lr);
    }

    public String deleteLeaveRequest(UUID id) {
        if (!leaveRequestRepository.existsById(id)) {
            return "The leave request with that id does not exist";
        }
        leaveRequestRepository.deleteById(id);
        return "Leave request deleted successfully";
    }
}