package com.victoire.leavemanagement.controller;

import com.victoire.leavemanagement.model.LeaveType;
import com.victoire.leavemanagement.service.LeaveTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/leave-types")
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    public LeaveTypeController(LeaveTypeService leaveTypeService) {
        this.leaveTypeService = leaveTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody LeaveType leaveType) {
        String message = leaveTypeService.saveLeaveType(leaveType);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LeaveType>> getAll() {
        return new ResponseEntity<>(leaveTypeService.getAllLeaveTypes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<LeaveType> lt = leaveTypeService.getLeaveTypeById(id);
        if (lt.isEmpty()) {
            return new ResponseEntity<>("The leave type with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lt.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody LeaveType leaveType) {
        Optional<LeaveType> updated = leaveTypeService.updateLeaveType(id, leaveType);
        if (updated.isEmpty()) {
            return new ResponseEntity<>("The leave type with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }

    @GetMapping("/unused")
    public ResponseEntity<List<LeaveType>> getUnused() {
        return new ResponseEntity<>(leaveTypeService.getUnusedLeaveTypes(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = leaveTypeService.deleteLeaveType(id);
        if (message.equals("The leave type with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}