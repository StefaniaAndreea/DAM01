package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.AuditReport;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Vulnerability;
import com.audit_management_app.auditmanagement_DAM.domain.services.*;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import com.audit_management_app.auditmanagement_DAM.dto.VulnerabilityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AuditReportServiceImpl implements IAuditReportService {
    @Autowired
    private IAuditReportRepository auditReportRepository;

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private IVulnerabilityRepository vulnerabilityRepository;

    @Override
    public AuditReport addAuditReport(Integer projectId, String filePath, Integer authorId) {
        // Verificăm dacă proiectul există
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));

        // Verificăm dacă autorul există
        Employee author = employeeRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + authorId + " does not exist."));

        // Creăm raportul
        AuditReport report = new AuditReport();
        report.setProject(project);
        report.setFilePath(filePath);
        report.setAuthor(author);
        report.setSubmissionDate(new java.util.Date());

        // Salvăm raportul
        return auditReportRepository.save(report);
    }


    @Override
    @Transactional(readOnly = true)
    public List<AuditReport> getReportsByProject(Integer projectId) {
        // Verificăm dacă proiectul există
        projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));

        // Returnăm rapoartele asociate proiectului
        return auditReportRepository.findByProjectId(projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditReport> getReportsByAuthor(Integer authorId) {
        // Verificăm dacă autorul există
        employeeRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + authorId + " does not exist."));

        // Returnăm rapoartele create de autor
        Employee author = employeeRepository.getById(authorId);
        return auditReportRepository.findByAuthor(author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditReport> getReportsBySubmissionDateRange(Date startDate, Date endDate) {
        // Returnăm rapoartele din intervalul specificat
        return auditReportRepository.findBySubmissionDateBetween(startDate, endDate);
    }

    @Override
    @Transactional
    public void deleteAuditReport(Integer reportId) {
        // Verificăm dacă raportul există
        AuditReport report = auditReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Audit report with ID " + reportId + " does not exist."));

        // Actualizăm vulnerabilitățile asociate
        List<Vulnerability> vulnerabilities = report.getVulnerabilities();
        for (Vulnerability vulnerability : vulnerabilities) {
            vulnerability.setStatus(Vulnerability.Status.RESOLVED);
            vulnerability.setReport(null); // Eliminăm asocierea cu raportul
            vulnerabilityRepository.save(vulnerability);
        }

        // Eliminăm raportul din lista proiectului
        Project project = report.getProject();
        project.getReports().remove(report);
        projectRepository.save(project);

        // Ștergem raportul
        auditReportRepository.delete(report);
    }

    @Override
    @Transactional
    public AuditReport submitUpdateAuditReport(Integer reportId, String filePath, Integer authorId) {
        // Verificăm dacă raportul există
        AuditReport existingReport = auditReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Audit report with ID " + reportId + " does not exist."));

        // Verificăm dacă autorul există
        Employee author = employeeRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + authorId + " does not exist."));

        // Actualizăm raportul
        existingReport.setFilePath(filePath);
        existingReport.setSubmissionDate(new Date());
        existingReport.setAuthor(author);

        // Salvăm modificările
        return auditReportRepository.save(existingReport);
    }
    @Override
    @Transactional(readOnly = true)
    public AuditReport getReportById(Integer reportId) {
        return auditReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Audit report with ID " + reportId + " does not exist."));
    }


}
