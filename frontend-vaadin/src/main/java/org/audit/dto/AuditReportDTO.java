package org.audit.dto;

import java.util.Date;
import java.util.List;

public class AuditReportDTO {
    private Integer reportId;         // ID-ul raportului
    private Integer projectId;        // ID-ul proiectului asociat
    private String filePath;          // Calea fișierului raportului
    private Date submissionDate;      // Data trimiterii raportului
    private Integer authorId;         // ID-ul autorului raportului
    private List<VulnerabilityDTO> vulnerabilities; // Lista vulnerabilităților asociate raportului

    public AuditReportDTO() {
    }

    public AuditReportDTO(Integer reportId, Integer projectId, String filePath, Date submissionDate, Integer authorId) {
        this.reportId = reportId;
        this.projectId = projectId;
        this.filePath = filePath;
        this.submissionDate = submissionDate;
        this.authorId = authorId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public List<VulnerabilityDTO> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<VulnerabilityDTO> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }
}
