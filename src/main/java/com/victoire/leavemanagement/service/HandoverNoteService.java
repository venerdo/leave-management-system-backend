package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.HandoverNote;
import com.victoire.leavemanagement.model.LeaveRequest;
import com.victoire.leavemanagement.model.LeaveRequestStatus;
import com.victoire.leavemanagement.repository.HandoverNoteRepository;
import com.victoire.leavemanagement.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HandoverNoteService {

    private final HandoverNoteRepository handoverNoteRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public HandoverNoteService(HandoverNoteRepository handoverNoteRepository, LeaveRequestRepository leaveRequestRepository) {
        this.handoverNoteRepository = handoverNoteRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public String saveHandoverNote(UUID leaveRequestId, HandoverNote handoverNote) {
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(leaveRequestId);
        if (leaveRequestOpt.isEmpty()) {
            return "The leave request with that id does not exist";
        }
        
        LeaveRequest leaveRequest = leaveRequestOpt.get();
        if (leaveRequest.getStatus() != LeaveRequestStatus.APPROVED) {
            return "Handover notes are only for APPROVED leave requests";
        }

        handoverNote.setWrittenDate(LocalDate.now());
        handoverNote.setLeaveRequest(leaveRequest);
        
        handoverNoteRepository.save(handoverNote);
        return "Handover note saved successfully";
    }

    public List<HandoverNote> getNotesByEmployeeStaffNumber(String staffNumber) {
        return handoverNoteRepository.findByEmployeeStaffNumber(staffNumber);
    }

    public String deleteHandoverNote(UUID id) {
        if (!handoverNoteRepository.existsById(id)) {
            return "The handover note with that id does not exist";
        }
        handoverNoteRepository.deleteById(id);
        return "Handover note deleted successfully";
    }
}