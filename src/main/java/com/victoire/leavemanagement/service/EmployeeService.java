package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.Employee;
import com.victoire.leavemanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public String saveEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "Employee saved successfully";
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> updateEmployee(UUID id, Employee employee) {
        Optional<Employee> existing = employeeRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        Employee e = existing.get();
        e.setFirstName(employee.getFirstName());
        e.setLastName(employee.getLastName());
        e.setStaffNumber(employee.getStaffNumber());
        e.setEmail(employee.getEmail());
        e.setJobTitle(employee.getJobTitle());
        employeeRepository.save(e);
        return Optional.of(e);
    }

    public String deleteEmployee(UUID id) {
        if (!employeeRepository.existsById(id)) {
            return "The employee with that id does not exist";
        }
        employeeRepository.deleteById(id);
        return "Employee deleted successfully";
    }
}