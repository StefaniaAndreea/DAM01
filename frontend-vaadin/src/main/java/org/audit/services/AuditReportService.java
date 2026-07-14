package org.audit.services;

import org.audit.dto.AuditReportDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Service
public class AuditReportService implements IAuditReportService {

    private static final Logger logger = Logger.getLogger(AuditReportService.class.getName());

    @Value("${backend.url.reports}")
    private String backendUrl; // URL-ul pentru endpoint-urile backend-ului

    private final RestTemplate restTemplate;

    public AuditReportService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AuditReportDTO addReport(AuditReportDTO report) {
        logger.info("Adding new audit report");
        HttpHeaders headers = generateHeaders();
        HttpEntity<AuditReportDTO> entity = new HttpEntity<>(report, headers);

        AuditReportDTO savedReport = restTemplate.exchange(
                backendUrl,
                HttpMethod.POST,
                entity,
                AuditReportDTO.class
        ).getBody();

        logger.info("Saved report: " + savedReport);
        return savedReport;
    }

    @Override
    public AuditReportDTO updateReport(Integer reportId, AuditReportDTO report) {
        logger.info("Updating audit report with ID: " + reportId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<AuditReportDTO> entity = new HttpEntity<>(report, headers);

        AuditReportDTO updatedReport = restTemplate.exchange(
                backendUrl + "/" + reportId,
                HttpMethod.PUT,
                entity,
                AuditReportDTO.class
        ).getBody();

        logger.info("Updated report: " + updatedReport);
        return updatedReport;
    }

    @Override
    public void deleteReport(Integer reportId) {
        logger.info("Deleting audit report with ID: " + reportId);
        restTemplate.delete(backendUrl + "/" + reportId);
    }

    @Override
    public AuditReportDTO getReportById(Integer reportId) {
        logger.info("Fetching audit report with ID: " + reportId);
        return restTemplate.getForObject(
                backendUrl + "/" + reportId,
                AuditReportDTO.class
        );
    }

    @Override
    public List<AuditReportDTO> getReportsByProjectId(Integer projectId) {
        logger.info("Fetching audit reports for project ID: " + projectId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        AuditReportDTO[] reports = restTemplate.exchange(
                backendUrl + "/project/" + projectId,
                HttpMethod.GET,
                entity,
                AuditReportDTO[].class
        ).getBody();

        return List.of(reports);
    }

    @Override
    public List<AuditReportDTO> getAllReports() {
        logger.info("Fetching all audit reports");
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        AuditReportDTO[] reports = restTemplate.exchange(
                backendUrl + "/all",
                HttpMethod.GET,
                entity,
                AuditReportDTO[].class
        ).getBody();

        return List.of(reports);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
