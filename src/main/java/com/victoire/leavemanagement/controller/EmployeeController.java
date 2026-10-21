package com.victoire.leavemanagement.controller;

import com.victoire.leavemanagement.model.Employee;
import com.victoire.leavemanagement.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Employee employee) {
        String message = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAll() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Employee> emp = employeeService.getEmployeeById(id);
        if (emp.isEmpty()) {
            return new ResponseEntity<>("The employee with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(emp.get(), HttpStatus.OK);
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<List<Employee>> getByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(employeeService.getEmployeesByLastName(lastName), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Employee employee) {
        Optional<Employee> updated = employeeService.updateEmployee(id, employee);
        if (updated.isEmpty()) {
            return new ResponseEntity<>("The employee with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = employeeService.deleteEmployee(id);
        if (message.equals("The employee with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}