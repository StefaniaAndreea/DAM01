package com.audit_management_app.auditmanagement_DAM.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuditReportDTO {
    @EqualsAndHashCode.Include
    private Integer reportId;

    private Integer projectId;   // ID-ul proiectului asociat
    private String filePath;     // Calea fișierului raportului
    private Date submissionDate; // Data trimiterii raportului
    private Integer authorId;    // ID-ul autorului raportului
    // Lista de vulnerabilități asociate raportului
    private List<VulnerabilityDTO> vulnerabilities;
}
