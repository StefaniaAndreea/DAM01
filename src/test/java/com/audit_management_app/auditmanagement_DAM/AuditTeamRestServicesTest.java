package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.dto.AuditTeamDTO;
import com.audit_management_app.auditmanagement_DAM.dto.EmployeeDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuditTeamRestServicesTest {

    private static Integer teamId; // Variabilă globală și statică
    private static Logger logger = Logger.getLogger(AuditTeamRestServicesTest.class.getName());
    private static String BASE_URL = "http://localhost:8088/api/rest/app/teams";
    private final RestTemplate restTemplate = new RestTemplate();

    private void createEmployee(Integer id, String name, String role, boolean available) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(id);
        employeeDTO.setName(name);
        employeeDTO.setRole(role);
        employeeDTO.setSalary(5000.0f);
        employeeDTO.setAvailable(available);
        employeeDTO.setTeamId(null); // Nu este asociat cu nicio echipă

        try {
            restTemplate.exchange(
                    "http://localhost:8088/api/rest/app/employees",
                    HttpMethod.POST,
                    new HttpEntity<>(employeeDTO, headers),
                    EmployeeDTO.class
            );
        } catch (Exception e) {
            logger.warning("Error creating employee with ID " + id + ": " + e.getMessage());
        }
    }


    @Test
    @Order(1)
    public void testGetAllTeams() {
        logger.info("DEBUG: Testing GET all teams ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<AuditTeamDTO> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Teams list is null!");
        logger.info("Teams list retrieved: " + response.size() + " teams.");
    }




    @Test
    @Order(2)
    public void testGetTeamById() {
        logger.info("DEBUG: Testing GET team by ID ...");

        if (teamId == null) {
            teamId = 1; // ID-ul unei echipe existente
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        AuditTeamDTO response = restTemplate.exchange(
                BASE_URL + "/" + teamId,
                HttpMethod.GET,
                request,
                AuditTeamDTO.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve team by ID!");
        assertEquals(teamId, response.getTeamId(), "Team ID does not match!");
        logger.info("Team retrieved by ID: " + response);
    }

    @Test
    @Order(3)
    public void testDeleteTeam() {
        logger.info("DEBUG: Testing DELETE team ...");

        if (teamId == null) {
            teamId = 1; // ID-ul unei echipe existente
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        restTemplate.exchange(
                BASE_URL + "/" + teamId,
                HttpMethod.DELETE,
                request,
                Void.class
        );

        logger.info("Team with ID " + teamId + " deleted.");
    }

    @Test
    @Order(4)
    public void testGetTeamsWithoutProjects() {
        logger.info("DEBUG: Testing GET teams without projects ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<AuditTeamDTO> response = restTemplate.exchange(
                BASE_URL + "/without-projects",
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve teams without projects!");
        logger.info("Teams without projects retrieved: " + response.size() + " teams.");
    }
}

