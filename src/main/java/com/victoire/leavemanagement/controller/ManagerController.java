
package com.victoire.leavemanagement.controller;

import com.victoire.leavemanagement.model.Manager;
import com.victoire.leavemanagement.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Manager manager) {
        String message = managerService.saveManager(manager);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Manager>> getAll() {
        return new ResponseEntity<>(managerService.getAllManagers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Manager> m = managerService.getManagerById(id);
        if (m.isEmpty()) {
            return new ResponseEntity<>(
                    "The manager with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(m.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody Manager manager) {
        Optional<Manager> updated = managerService.updateManager(id, manager);

        if (updated.isEmpty()) {
            return new ResponseEntity<>(
                    "The manager with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }

    // Add a leave type to a manager
    @PutMapping("/{managerId}/leave-types/{leaveTypeId}")
    public ResponseEntity<String> addLeaveType(
            @PathVariable UUID managerId,
            @PathVariable UUID leaveTypeId) {

        String message = managerService.addLeaveTypeToManager(managerId, leaveTypeId);

        if (message.equals("The manager with that id does not exist")
                || message.equals("That leave type does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals("The manager already has that leave type")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Change a manager's team
    @PatchMapping("/{managerId}/team")
    public ResponseEntity<String> changeTeam(
            @PathVariable UUID managerId,
            @RequestParam int teamNumber) {

        String message = managerService.moveManagerToTeam(managerId, teamNumber);

        if (message.equals("The manager with that id does not exist")
                || message.equals("The team with that teamNumber does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        if (message.equals("That team is taken by another manager")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = managerService.deleteManager(id);

        if (message.equals("The manager with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/by-leave-type")
    public ResponseEntity<List<Manager>> getByLeaveType(@RequestParam String name) {
        return new ResponseEntity<>(
                managerService.getManagersByLeaveTypeName(name),
                HttpStatus.OK);
    }
}
