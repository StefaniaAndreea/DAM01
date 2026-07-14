package com.audit_management_app.auditmanagement_DAM.rest.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.AuditReport;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Vulnerability;
import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditReportService;
import com.audit_management_app.auditmanagement_DAM.dto.AuditReportDTO;
import com.audit_management_app.auditmanagement_DAM.dto.VulnerabilityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/app/reports")
public class AuditReportRestServices {
    @Autowired
    private IAuditReportService auditReportService;

    @PostMapping
    public AuditReportDTO addAuditReport(@RequestBody AuditReportDTO auditReportDTO) {
        // Apelăm metoda din service pentru a adăuga raportul
        AuditReport auditReport = auditReportService.addAuditReport(
                auditReportDTO.getProjectId(),
                auditReportDTO.getFilePath(),
                auditReportDTO.getAuthorId()
        );

        // Convertim entitatea în DTO și o returnăm
        return convertToDTO(auditReport);
    }

    @GetMapping("/project/{projectId}")
    public List<AuditReportDTO> getReportsByProject(@PathVariable Integer projectId) {
        return auditReportService.getReportsByProject(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/author/{authorId}")
    public List<AuditReportDTO> getReportsByAuthor(@PathVariable Integer authorId) {
        return auditReportService.getReportsByAuthor(authorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/date-range")
    public List<AuditReportDTO> getReportsBySubmissionDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return auditReportService.getReportsBySubmissionDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{reportId}")
    public String deleteAuditReport(@PathVariable Integer reportId) {
        auditReportService.deleteAuditReport(reportId);
        return "Audit report with ID " + reportId + " has been deleted.";
    }

    @PutMapping("/{reportId}/submit-update")
    public AuditReportDTO submitUpdateAuditReport(@PathVariable Integer reportId, @RequestBody AuditReportDTO reportDTO) {
        AuditReport updatedReport = auditReportService.submitUpdateAuditReport(
                reportId,
                reportDTO.getFilePath(),
                reportDTO.getAuthorId()
        );
        return convertToDTO(updatedReport);
    }

    @GetMapping("/{reportId}/vulnerabilities")
    public AuditReportDTO getReportWithVulnerabilities(@PathVariable Integer reportId) {
        // Obținem raportul de audit după ID
        AuditReport auditReport = auditReportService.getReportById(reportId);

        // Convertim raportul în DTO
        AuditReportDTO auditReportDTO = convertToDTO(auditReport);

        // Verificăm dacă raportul are vulnerabilități
        if (auditReport.getVulnerabilities() != null && !auditReport.getVulnerabilities().isEmpty()) {
            // Convertim vulnerabilitățile la DTO
            List<VulnerabilityDTO> vulnerabilities = auditReport.getVulnerabilities().stream()
                    .map(this::convertVulnerabilityToDTO)
                    .collect(Collectors.toList());
            auditReportDTO.setVulnerabilities(vulnerabilities);
        } else {
            // Dacă nu există vulnerabilități, setăm o listă goală
            auditReportDTO.setVulnerabilities(List.of());
        }

        return auditReportDTO;
    }

    @GetMapping("/{reportId}")
    public AuditReportDTO getReportById(@PathVariable Integer reportId) {
        // Apelăm serviciul pentru a obține raportul pe baza ID-ului
        AuditReport auditReport = auditReportService.getReportById(reportId);
        // Convertim raportul în DTO și returnăm
        return convertToDTO(auditReport);
    }


    private AuditReportDTO convertToDTO(AuditReport auditReport) {
        AuditReportDTO dto = new AuditReportDTO();
        dto.setReportId(auditReport.getReportId());
        dto.setFilePath(auditReport.getFilePath());
        dto.setSubmissionDate(auditReport.getSubmissionDate());
        dto.setAuthorId(auditReport.getAuthor().getEmployeeId());
        dto.setProjectId(auditReport.getProject().getProjectId());

        // Conversie vulnerabilități asociate în DTO
        dto.setVulnerabilities(
                auditReport.getVulnerabilities().stream()
                        .map(this::convertVulnerabilityToDTO) // Conversie folosind metoda dedicată
                        .collect(Collectors.toList())
        );

        return dto;
    }
    private VulnerabilityDTO convertVulnerabilityToDTO(Vulnerability vulnerability) {
        VulnerabilityDTO dto = new VulnerabilityDTO();
        dto.setVulnerabilityId(vulnerability.getVulnerabilityId());
        dto.setProjectId(vulnerability.getProject().getProjectId());
        dto.setDescription(vulnerability.getDescription());
        dto.setSeverity(vulnerability.getSeverity());
        dto.setStatus(vulnerability.getStatus());
        if (vulnerability.getReport() != null) {
            dto.setReportId(vulnerability.getReport().getReportId());
        }
        return dto;
    }
}
