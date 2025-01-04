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
}
