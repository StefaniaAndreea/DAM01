package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAuditTeamRepository extends JpaRepository<AuditTeam, Integer> {
    @Query("SELECT t FROM AuditTeam t WHERE t.assignedProjects IS EMPTY")
    List<AuditTeam> findTeamsWithoutProjects();
}
