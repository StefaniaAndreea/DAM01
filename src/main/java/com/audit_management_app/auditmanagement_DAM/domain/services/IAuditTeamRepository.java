package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuditTeamRepository extends JpaRepository<AuditTeam, Integer> {
}
