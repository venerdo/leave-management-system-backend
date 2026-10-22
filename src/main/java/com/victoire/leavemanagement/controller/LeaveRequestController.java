package com.victoire.leavemanagement.controller;

import com.victoire.leavemanagement.model.LeaveRequest;
import com.victoire.leavemanagement.model.LeaveRequestStatus;
import com.victoire.leavemanagement.service.LeaveRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestBody LeaveRequest leaveRequest) {

        String message =
                leaveRequestService.saveLeaveRequest(leaveRequest);

        if (message.equals(
                "Leave request date cannot be in the past")) {
            return new ResponseEntity<>(
                    message, HttpStatus.BAD_REQUEST);
        }

        if (message.equals(
                "Manager already has a leave request starting on that date")) {
            return new ResponseEntity<>(
                    message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(
                message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LeaveRequest>> getAll() {
        return new ResponseEntity<>(
                leaveRequestService.getAllLeaveRequests(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {

        Optional<LeaveRequest> lr =
                leaveRequestService.getLeaveRequestById(id);

        if (lr.isEmpty()) {
            return new ResponseEntity<>(
                    "The leave request with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(lr.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody LeaveRequest leaveRequest) {

        Optional<LeaveRequest> updated =
                leaveRequestService.updateLeaveRequest(
                        id, leaveRequest);

        if (updated.isEmpty()) {
            return new ResponseEntity<>(
                    "The leave request with that id does not exist",
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable UUID id,
            @RequestParam LeaveRequestStatus status) {

        String message =
                leaveRequestService.updateLeaveRequestStatus(
                        id, status);

        if (message.equals(
                "The leave request with that id does not exist")) {
            return new ResponseEntity<>(
                    message, HttpStatus.NOT_FOUND);
        }

        if (message.startsWith("Cannot change status")) {
            return new ResponseEntity<>(
                    message, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        String message =
                leaveRequestService.deleteLeaveRequest(id);

        if (message.equals(
                "The leave request with that id does not exist")) {
            return new ResponseEntity<>(
                    message, HttpStatus.NOT_FOUND);
        }

        if (message.equals(
                "That leave request has handover notes, delete them first")) {
            return new ResponseEntity<>(
                    message, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<LeaveRequest>> getByStatus(
            @RequestParam LeaveRequestStatus status) {

        return new ResponseEntity<>(
                leaveRequestService.getLeaveRequestsByStatus(status),
                HttpStatus.OK);
    }

    @GetMapping("/between")
    public ResponseEntity<List<LeaveRequest>> getBetween(
            @RequestParam String start,
            @RequestParam String end) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return new ResponseEntity<>(
                leaveRequestService.getLeaveRequestsBetweenDates(
                        startDate, endDate),
                HttpStatus.OK);
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStats() {

        return new ResponseEntity<>(
                leaveRequestService.getLeaveRequestStats(),
                HttpStatus.OK);
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(
            @RequestParam UUID managerId,
            @RequestParam String date) {

        LocalDate parsedDate = LocalDate.parse(date);

        String message =
                leaveRequestService.cancelLeaveRequestsForManager(
                        managerId, parsedDate);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<LeaveRequest>> getPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort) {

        String[] sortParams = sort.split(",");

        Sort.Direction direction =
                sortParams[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortParams[0])
        );

        return new ResponseEntity<>(
                leaveRequestService.getLeaveRequestsPage(pageable),
                HttpStatus.OK);
    }
}