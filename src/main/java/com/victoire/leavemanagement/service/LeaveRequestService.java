package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.LeaveRequest;
import com.victoire.leavemanagement.model.LeaveRequestStatus;
import com.victoire.leavemanagement.repository.HandoverNoteRepository;
import com.victoire.leavemanagement.repository.LeaveRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final HandoverNoteRepository handoverNoteRepository;

    public LeaveRequestService(
            LeaveRequestRepository leaveRequestRepository,
            HandoverNoteRepository handoverNoteRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.handoverNoteRepository = handoverNoteRepository;
    }

    public String saveLeaveRequest(LeaveRequest leaveRequest) {
        if (leaveRequest.getStartDate()
                .isBefore(java.time.LocalDate.now())) {
            return "Leave request date cannot be in the past";
        }

        if (leaveRequestRepository.existsByManagerAndStartDateAndStatusNot(
                leaveRequest.getManager(),
                leaveRequest.getStartDate(),
                LeaveRequestStatus.REJECTED)) {
            return "Manager already has a leave request starting on that date";
        }

        // Force status to SUBMITTED regardless of what JSON says
        leaveRequest.setStatus(LeaveRequestStatus.SUBMITTED);

        leaveRequestRepository.save(leaveRequest);
        return "Leave request saved successfully";
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public Optional<LeaveRequest> getLeaveRequestById(UUID id) {
        return leaveRequestRepository.findById(id);
    }

    public List<LeaveRequest> getLeaveRequestsByStatus(
            LeaveRequestStatus status) {
        return leaveRequestRepository
                .findByStatusOrderByStartDateAsc(status);
    }

    public List<LeaveRequest> getLeaveRequestsBetweenDates(
            java.time.LocalDate start,
            java.time.LocalDate end) {
        return leaveRequestRepository
                .findByStartDateBetweenOrderByStartDateAsc(start, end);
    }

    // Bonus 2: Get paginated leave requests
    public Page<LeaveRequest> getLeaveRequestsPage(
            Pageable pageable) {
        return leaveRequestRepository.findAll(pageable);
    }

    // F1: Get leave request counts grouped by status
    public List<Object[]> getLeaveRequestStats() {
        return leaveRequestRepository.countLeaveRequestsByStatus();
    }

    // F2: Bulk cancel leave requests for an absent manager
    @Transactional
    public String cancelLeaveRequestsForManager(
            UUID managerId,
            java.time.LocalDate date) {

        int updatedCount =
                leaveRequestRepository
                        .cancelLeaveRequestsForManagerOnDate(
                                managerId, date);

        return updatedCount + " leave requests cancelled";
    }

    // D2: Update status only when the transition is valid
    public String updateLeaveRequestStatus(
            UUID id,
            LeaveRequestStatus newStatus) {

        Optional<LeaveRequest> existing =
                leaveRequestRepository.findById(id);

        if (existing.isEmpty()) {
            return "The leave request with that id does not exist";
        }

        LeaveRequest lr = existing.get();
        LeaveRequestStatus current = lr.getStatus();

        // Validate status transitions
        boolean valid = false;

        if (current == LeaveRequestStatus.SUBMITTED
                && (newStatus == LeaveRequestStatus.IN_REVIEW
                || newStatus == LeaveRequestStatus.REJECTED)) {
            valid = true;
        } else if (current == LeaveRequestStatus.IN_REVIEW
                && (newStatus == LeaveRequestStatus.APPROVED
                || newStatus == LeaveRequestStatus.REJECTED)) {
            valid = true;
        }

        if (!valid) {
            return "Cannot change status from "
                    + current + " to " + newStatus;
        }

        lr.setStatus(newStatus);
        leaveRequestRepository.save(lr);

        return "Leave request status updated successfully";
    }

    public Optional<LeaveRequest> updateLeaveRequest(
            UUID id,
            LeaveRequest leaveRequest) {

        Optional<LeaveRequest> existing =
                leaveRequestRepository.findById(id);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        LeaveRequest lr = existing.get();
        lr.setStartDate(leaveRequest.getStartDate());
        lr.setDays(leaveRequest.getDays());
        lr.setReason(leaveRequest.getReason());
        lr.setEmployee(leaveRequest.getEmployee());
        lr.setManager(leaveRequest.getManager());

        leaveRequestRepository.save(lr);
        return Optional.of(lr);
    }

    // E3: Prevent deletion when handover notes exist
    public String deleteLeaveRequest(UUID id) {
        Optional<LeaveRequest> leaveRequestOpt =
                leaveRequestRepository.findById(id);

        if (leaveRequestOpt.isEmpty()) {
            return "The leave request with that id does not exist";
        }

        if (handoverNoteRepository.existsByLeaveRequest(
                leaveRequestOpt.get())) {
            return "That leave request has handover notes, delete them first";
        }

        leaveRequestRepository.deleteById(id);
        return "Leave request deleted successfully";
    }
}