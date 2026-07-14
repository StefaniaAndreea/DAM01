package org.audit.services;

import org.audit.dto.AuditTeamDTO;

import java.util.List;

public interface IAuditTeamService {
    AuditTeamDTO addTeam(AuditTeamDTO auditTeamDTO);
    void deleteTeam(Integer teamId);
    void addMemberToTeam(Integer teamId, Integer employeeId);
    AuditTeamDTO getTeamById(Integer teamId);
    List<AuditTeamDTO> getAllTeams();
    List<AuditTeamDTO> getTeamsWithoutProjects();

}
