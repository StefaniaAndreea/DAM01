package org.audit.services;

import org.audit.dto.AuditTeamDTO;
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
public class AuditTeamService implements IAuditTeamService {

    private static final Logger logger = Logger.getLogger(AuditTeamService.class.getName());

    @Value("${backend.url.teams}")
    private String backendUrl; // URL-ul de bază pentru endpointurile backend-ului

    private final RestTemplate restTemplate;

    public AuditTeamService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AuditTeamDTO addTeam(AuditTeamDTO auditTeamDTO) {
        logger.info("Adding new team: " + auditTeamDTO);
        HttpHeaders headers = generateHeaders();
        HttpEntity<AuditTeamDTO> entity = new HttpEntity<>(auditTeamDTO, headers);

        AuditTeamDTO savedTeam = restTemplate.exchange(
                backendUrl, // Endpoint-ul backend-ului
                HttpMethod.POST,
                entity,
                AuditTeamDTO.class
        ).getBody();

        logger.info("Added team: " + savedTeam);
        return savedTeam;
    }

    @Override
    public void deleteTeam(Integer teamId) {
        logger.info("Deleting team with ID: " + teamId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                backendUrl + "/" + teamId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        logger.info("Deleted team with ID: " + teamId);
    }

    @Override
    public void addMemberToTeam(Integer teamId, Integer employeeId) {
        logger.info("Adding member with ID: " + employeeId + " to team with ID: " + teamId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                backendUrl + "/" + teamId + "/add-member/" + employeeId,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        logger.info("Added member to team");
    }

    @Override
    public AuditTeamDTO getTeamById(Integer teamId) {
        logger.info("Fetching team with ID: " + teamId);
        AuditTeamDTO team = restTemplate.getForObject(
                backendUrl + "/" + teamId,
                AuditTeamDTO.class
        );

        logger.info("Fetched team: " + team);
        return team;
    }

    @Override
    public List<AuditTeamDTO> getAllTeams() {
        logger.info("Fetching all teams from backend");
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        AuditTeamDTO[] teams = restTemplate.exchange(
                backendUrl,
                HttpMethod.GET,
                entity,
                AuditTeamDTO[].class
        ).getBody();

        return List.of(teams);
    }

    @Override
    public List<AuditTeamDTO> getTeamsWithoutProjects() {
        logger.info("Fetching teams without projects");
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        AuditTeamDTO[] teams = restTemplate.exchange(
                backendUrl + "/without-projects",
                HttpMethod.GET,
                entity,
                AuditTeamDTO[].class
        ).getBody();

        return List.of(teams);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
