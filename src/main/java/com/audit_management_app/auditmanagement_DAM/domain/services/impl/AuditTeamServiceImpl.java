package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.services.*;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuditTeamServiceImpl implements IAuditTeamService {

    private static final Logger logger = LoggerFactory.getLogger(AuditTeamServiceImpl.class);
    @Autowired
    private IAuditTeamRepository auditTeamRepository;

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IProjectRepository projectRepository;

    @Override
    public AuditTeam addTeam(String teamName, List<Integer> memberIds) {
        logger.info("Creating a new team with name: {}", teamName);

        AuditTeam team = new AuditTeam();
        team.setTeamName(teamName);

        List<Employee> members = new ArrayList<>();
        for (Integer memberId : memberIds) {
            Employee employee = employeeRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + memberId + " not found."));
            if (employee.getTeam() != null) {
                throw new IllegalArgumentException("Employee with ID " + memberId + " is already part of another team.");
            }
            members.add(employee);
            employee.setTeam(team);
        }
        team.setMembers(members);

        AuditTeam savedTeam = auditTeamRepository.save(team);
        logger.info("Team created successfully with ID: {}", savedTeam.getTeamId());
        return savedTeam;
    }

    @Override
    public AuditTeam updateTeam(Integer teamId, String newName, List<Integer> memberIds) {
        logger.info("Updating team with ID: {}", teamId);

        AuditTeam team = auditTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " not found."));
        team.setTeamName(newName);
        logger.info("Updated team name to: {}", newName);

        List<Employee> updatedMembers = new ArrayList<>();
        for (Integer memberId : memberIds) {
            Employee employee = employeeRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + memberId + " not found."));
            if (employee.getTeam() != null && !employee.getTeam().equals(team)) {
                throw new IllegalArgumentException("Employee with ID " + memberId + " is already part of another team.");
            }
            updatedMembers.add(employee);
            employee.setTeam(team);
        }

        for (Employee member : team.getMembers()) {
            if (!updatedMembers.contains(member)) {
                logger.info("Removing employee with ID: {} from team.", member.getEmployeeId());
                member.setTeam(null);
            }
        }

        team.setMembers(updatedMembers);

        AuditTeam updatedTeam = auditTeamRepository.save(team);
        logger.info("Team updated successfully with ID: {}", updatedTeam.getTeamId());
        return updatedTeam;
    }

    @Override
    public void deleteTeam(Integer teamId) {
        AuditTeam team = auditTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " does not exist."));

        // Setăm `team` la null pentru toate proiectele asociate
        team.getAssignedProjects().forEach(project -> {
            project.setTeam(null);
            projectRepository.save(project); // Salvăm fiecare proiect
        });

        // Setăm `team` la null pentru toți membrii echipei
        team.getMembers().forEach(member -> {
            member.setTeam(null);
            member.setAvailable(true); // Angajații devin disponibili
            employeeRepository.save(member); // Salvăm fiecare membru
        });

        // Ștergem echipa
        auditTeamRepository.delete(team);
    }


    @Override
    public AuditTeam getTeamById(Integer teamId) {
        logger.info("Fetching team with ID: {}", teamId);
        return auditTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " not found."));
    }

    @Override
    public List<AuditTeam> getAllTeams() {
        logger.info("Fetching all teams.");
        return auditTeamRepository.findAll();
    }

    @Override
    public void addMemberToTeam(Integer teamId, Integer employeeId) {
        logger.info("Adding employee with ID: {} to team with ID: {}", employeeId, teamId);

        AuditTeam team = auditTeamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " not found."));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + employeeId + " not found."));

        if (employee.getTeam() != null) {
            throw new IllegalArgumentException("Employee with ID " + employeeId + " is already part of another team.");
        }

        employee.setTeam(team);
        team.getMembers().add(employee);
        auditTeamRepository.save(team);

        logger.info("Employee with ID: {} added to team with ID: {}", employeeId, teamId);
    }

//    @Override
//    @Transactional
//    public void removeMemberFromTeam(Integer teamId, Integer employeeId) {
//        // Găsim echipa
//        AuditTeam team = auditTeamRepository.findById(teamId)
//                .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " not found."));
//
//        // Găsim angajatul
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + employeeId + " not found."));
//
//        // Verificăm dacă angajatul aparține echipei
//        if (!team.getMembers().contains(employee)) {
//            throw new IllegalArgumentException("Employee with ID " + employeeId + " is not a member of team with ID " + teamId + ".");
//        }
//
//        // Actualizăm task-urile asociate angajatului înainte de a-l elimina din echipă
//        List<Task> assignedTasks = taskRepository.findByAssignedTo(employee);
//        for (Task task : assignedTasks) {
//            task.setAssignedTo(null); // Eliminăm asocierea angajatului din task-uri
//            taskRepository.save(task); // Salvăm task-ul
//        }
//
//        // Scoatem angajatul din echipă și actualizăm echipa
//        team.getMembers().remove(employee);
//        auditTeamRepository.save(team);
//
//        // Marcăm angajatul ca disponibil și eliminăm asocierea cu echipa
//        employee.setAvailable(true);
//        employee.setTeam(null); // Resetăm echipa
//        employeeRepository.save(employee);
//
//        logger.info("Employee with ID {} has been removed from team ID {}. Their tasks are now unassigned and employee is marked as available.", employeeId, teamId);
//    }
//


    @Override
    @Transactional(readOnly = true)
    public List<AuditTeam> getTeamsWithoutProjects() {
        return auditTeamRepository.findTeamsWithoutProjects();
    }

}
