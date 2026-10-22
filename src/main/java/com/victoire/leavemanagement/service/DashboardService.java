package com.victoire.leavemanagement.service;

import com.victoire.leavemanagement.model.LeaveRequestStatus;
import com.victoire.leavemanagement.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final TeamService teamService;
    private final LeaveRequestRepository leaveRequestRepository;
    private final HandoverNoteRepository handoverNoteRepository;

    public DashboardService(
        EmployeeRepository employeeRepository,
        ManagerRepository managerRepository,
        TeamService teamService,
        LeaveRequestRepository leaveRequestRepository,
        HandoverNoteRepository handoverNoteRepository) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.teamService = teamService;
        this.leaveRequestRepository = leaveRequestRepository;
        this.handoverNoteRepository = handoverNoteRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();

        summary.put("employees", employeeRepository.count());
        summary.put("managers", managerRepository.count());

        summary.put("freeTeams", (long) teamService.getFreeTeams().size());

        summary.put("requestsToday", leaveRequestRepository.countByStartDate(LocalDate.now()));
        summary.put("submitted", leaveRequestRepository.countByStatus(LeaveRequestStatus.SUBMITTED));
        summary.put("handoverNotes", handoverNoteRepository.count());

        return summary;
    }
}