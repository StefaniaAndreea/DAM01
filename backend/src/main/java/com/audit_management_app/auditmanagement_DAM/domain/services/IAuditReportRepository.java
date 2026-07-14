package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.AuditReport;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IAuditReportRepository extends JpaRepository<AuditReport, Integer> {

    // Găsește rapoarte după autor
    List<AuditReport> findByAuthor(Employee author);

    // Găsește rapoarte asociate unui proiect
    @Query("SELECT r FROM AuditReport r WHERE r.project.projectId = :projectId")
    List<AuditReport> findByProjectId(@Param("projectId") int projectId);

    // Găsește rapoarte într-un interval de timp
    @Query("SELECT r FROM AuditReport r WHERE r.submissionDate BETWEEN :startDate AND :endDate")
    List<AuditReport> findBySubmissionDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}