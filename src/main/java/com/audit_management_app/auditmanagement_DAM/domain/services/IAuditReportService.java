package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.AuditReport;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Vulnerability;
import com.audit_management_app.auditmanagement_DAM.dto.VulnerabilityDTO;

import java.util.Date;
import java.util.List;

public interface IAuditReportService {
    AuditReport addAuditReport(Integer projectId, String filePath, Integer authorId);
    AuditReport submitUpdateAuditReport(Integer reportId, String filePath, Integer authorId);
    List<AuditReport> getReportsByProject(Integer projectId);
    List<AuditReport> getReportsByAuthor(Integer authorId);
    void deleteAuditReport(Integer reportId);
    List<AuditReport> getReportsBySubmissionDateRange(Date startDate, Date endDate);
    AuditReport getReportById(Integer reportId);
}
