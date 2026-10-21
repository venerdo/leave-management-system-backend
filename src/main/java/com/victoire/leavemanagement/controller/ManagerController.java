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
            return new ResponseEntity<>("The manager with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(m.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Manager manager) {
        Optional<Manager> updated = managerService.updateManager(id, manager);
        if (updated.isEmpty()) {
            return new ResponseEntity<>("The manager with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
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
        return new ResponseEntity<>(managerService.getManagersByLeaveTypeName(name), HttpStatus.OK);
    }
}