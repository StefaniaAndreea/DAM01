package com.audit_management_app.auditmanagement_DAM;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.audit_management_app.auditmanagement_DAM.dto.ProjectDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class TestAppProjectServiceREST {
    private static Integer projectId; // Variabilă globală și statică
    private static Logger logger = Logger.getLogger(TestAppProjectServiceREST.class.getName());
    private static String BASE_URL = "http://localhost:8088/api/rest/app/project";
    private final RestTemplate restTemplate = new RestTemplate();


    @Test
    @Order(1)
    public void testGetAllProjects() {
        logger.info("DEBUG: Testing GET all projects ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<ProjectDTO> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Projects list is null!");
        logger.info("Projects list retrieved: " + response.size() + " projects.");
    }

    @Test
    @Order(2)
    public void testAddProject() {
        logger.info("DEBUG: Testing POST add project ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setClientId(1); // ID-ul unui client existent
        projectDTO.setTeamId(1);   // ID-ul unei echipe existente
        projectDTO.setStartDate(new Date());
        projectDTO.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // +1 zi
        projectDTO.setStatus("ONGOING");
        projectDTO.setProgress(50.0f);

        HttpEntity<ProjectDTO> request = new HttpEntity<>(projectDTO, headers);

        ProjectDTO response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                request,
                ProjectDTO.class
        ).getBody();

        assertNotNull(response, "Failed to add project!");
        assertNotNull(response.getProjectId(), "Project ID must not be null!");

        projectId = response.getProjectId(); // Setează `projectId` pentru testele ulterioare
        logger.info("Project added with ID: " + projectId);
    }
    @Test
    @Order(3)
    public void testUpdateProject() {
        logger.info("DEBUG: Testing PUT update project ...");

        // Setăm direct un projectId existent pentru acest test
        if (projectId == null) {
            projectId = 1; // ID-ul proiectului existent în baza de date
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(projectId);
        projectDTO.setName("Updated Test Project");
        projectDTO.setClientId(1); // Client valid
        projectDTO.setTeamId(1);   // Echipa validă
        projectDTO.setStartDate(new Date());
        projectDTO.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // +1 zi
        projectDTO.setStatus("COMPLETED");
        projectDTO.setProgress(90.0f);

        HttpEntity<ProjectDTO> request = new HttpEntity<>(projectDTO, headers);

        ProjectDTO response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.PUT,
                request,
                ProjectDTO.class
        ).getBody();

        assertNotNull(response, "Failed to update project!");
        assertEquals(projectId, response.getProjectId(), "Project ID does not match!");
        assertEquals("Updated Test Project", response.getName(), "Project name does not match!");
        assertEquals(90.0f, response.getProgress(), "Project progress does not match!");
        logger.info("Project updated successfully: " + response);
    }

    @Test
    @Order(4)
    public void testDeleteProject() {
        logger.info("DEBUG: Testing DELETE project ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        restTemplate.exchange(
                BASE_URL + "/" + projectId,
                HttpMethod.DELETE,
                request,
                Void.class
        );

        logger.info("Project with ID " + projectId + " deleted.");
    }

    @Test
    @Order(5)
    public void testArchiveProject() {
        logger.info("DEBUG: Testing PUT archive project ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        restTemplate.exchange(
                BASE_URL + "/" + projectId + "/archive",
                HttpMethod.PUT,
                request,
                Void.class
        );

        logger.info("Project with ID " + projectId + " archived.");
    }

    @Test
    @Order(6)
    public void testFindProjectsByStatus() {
        logger.info("DEBUG: Testing GET projects by status ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<ProjectDTO> response = restTemplate.exchange(
                BASE_URL + "/status/ONGOING",
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve projects by status!");
        logger.info("Projects with status ONGOING: " + response.size());
    }

    @Test
    @Order(7)
    public void testFindProjectsByClient() {
        logger.info("DEBUG: Testing GET projects by client ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<ProjectDTO> response = restTemplate.exchange(
                BASE_URL + "/client/Client Test",
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve projects by client!");
        logger.info("Projects for client 'Client Test': " + response.size());
    }
}
