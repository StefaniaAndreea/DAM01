package org.audit.services;

import org.audit.dto.AuditReportDTO;

import java.util.List;

public interface IAuditReportService {
    AuditReportDTO addReport(AuditReportDTO report);
    AuditReportDTO updateReport(Integer reportId, AuditReportDTO report);
    void deleteReport(Integer reportId);
    AuditReportDTO getReportById(Integer reportId);
    List<AuditReportDTO> getReportsByProjectId(Integer projectId);
    List<AuditReportDTO> getAllReports();
}
