package com.victoire.leavemanagement.controller;

import com.victoire.leavemanagement.model.HandoverNote;
import com.victoire.leavemanagement.service.HandoverNoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/handover-notes")
public class HandoverNoteController {

    private final HandoverNoteService handoverNoteService;

    public HandoverNoteController(HandoverNoteService handoverNoteService) {
        this.handoverNoteService = handoverNoteService;
    }

    // E1
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID leaveRequestId, @RequestBody HandoverNote handoverNote) {
        String message = handoverNoteService.saveHandoverNote(leaveRequestId, handoverNote);

        if (message.equals("The leave request with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        if (message.equals("Handover notes are only for APPROVED leave requests")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // E2
    @GetMapping("/employee/{staffNumber}")
    public ResponseEntity<List<HandoverNote>> getByEmployee(@PathVariable String staffNumber) {
        return new ResponseEntity<>(handoverNoteService.getNotesByEmployeeStaffNumber(staffNumber), HttpStatus.OK);
    }

    // E3
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = handoverNoteService.deleteHandoverNote(id);
        if (message.equals("The handover note with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}