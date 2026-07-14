package com.audit_management_app.auditmanagement_DAM.rest.services;

import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamService;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.dto.AuditTeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/app/teams")
public class AuditTeamRestServices {
    @Autowired
    private IAuditTeamService auditTeamService;

    @PostMapping
    public AuditTeamDTO addTeam(@RequestBody AuditTeamDTO auditTeamDTO) {
        AuditTeam createdTeam = auditTeamService.addTeam(auditTeamDTO.getTeamName(), auditTeamDTO.getMemberIds());
        return convertToDTO(createdTeam);
    }

    //POST http://localhost:8088/api/rest/app/teams
//    {
//        "teamName": "Team A",
//            "memberIds": [1, 2, 3] // Lista ID-urilor angajaților
//    }

    @DeleteMapping("/{teamId}")
    public String deleteTeam(@PathVariable Integer teamId) {
        auditTeamService.deleteTeam(teamId);
        return "Team with ID " + teamId + " has been deleted.";
    }
// DELETE http://localhost:8088/api/rest/app/teams/{teamId}

    @PutMapping("/{teamId}/add-member/{employeeId}")
    public String addMemberToTeam(@PathVariable Integer teamId, @PathVariable Integer employeeId) {
        auditTeamService.addMemberToTeam(teamId, employeeId);
        return "Employee with ID " + employeeId + " has been added to team with ID " + teamId + ".";
    }

    // PUT http://localhost:8088/api/rest/app/teams/{teamId}/add-member/{employeeId}

//    @PutMapping("/{teamId}/remove-member/{employeeId}")
//    public String removeMemberFromTeam(@PathVariable Integer teamId, @PathVariable Integer employeeId) {
//        auditTeamService.removeMemberFromTeam(teamId, employeeId);
//        return "Employee with ID " + employeeId + " has been removed from team with ID " + teamId + ".";
//    }

    @GetMapping("/{teamId}")
    public AuditTeamDTO getTeamById(@PathVariable Integer teamId) {
        AuditTeam team = auditTeamService.getTeamById(teamId);
        return convertToDTO(team);
    }
// GET http://localhost:8088/api/rest/app/teams/2
    @GetMapping
    public List<AuditTeamDTO> getAllTeams() {
        return auditTeamService.getAllTeams().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
// GET http://localhost:8088/api/rest/app/teams
    @GetMapping("/without-projects")
    public List<AuditTeamDTO> getTeamsWithoutProjects() {
        List<AuditTeam> teamsWithoutProjects = auditTeamService.getTeamsWithoutProjects();
        return teamsWithoutProjects.stream()
                .map(this::convertToDTO)
                .toList();
    }
//GET http://localhost:8088/api/rest/app/teams/without-projects
    // Helper methods for conversion
    private AuditTeamDTO convertToDTO(AuditTeam team) {
        AuditTeamDTO dto = new AuditTeamDTO();
        dto.setTeamId(team.getTeamId());
        dto.setTeamName(team.getTeamName());
        dto.setMemberIds(team.getMembers().stream()
                .map(member -> member.getEmployeeId())
                .collect(Collectors.toList()));
        return dto;
    }
}
