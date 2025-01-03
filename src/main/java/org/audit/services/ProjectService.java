package org.audit.services;

import org.audit.dto.ProjectDTO;
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
public class ProjectService {

    private static final Logger logger = Logger.getLogger(ProjectService.class.getName());

    @Value("${backend.url.projects}")
    private String backendUrl; // URL-ul de bază pentru endpointurile backend-ului

    private final RestTemplate restTemplate;

    public ProjectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Obține toate proiectele
    public List<ProjectDTO> getAllProjects() {
        logger.info("Fetching all projects from backend: " + backendUrl);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ProjectDTO[] projects = restTemplate.exchange(
                backendUrl, // Endpoint-ul backend-ului
                HttpMethod.GET, // Metoda HTTP
                entity, // Antetele cererii
                ProjectDTO[].class // Tipul răspunsului
        ).getBody();

        return List.of(projects);
    }
    public ProjectDTO getProjectById(Integer projectId) {
        String url = backendUrl + "/" + projectId;
        logger.info("Fetching project with ID: " + projectId);
        ProjectDTO project = restTemplate.getForObject(url, ProjectDTO.class);
        logger.info("Fetched project: " + project);
        return project;
    }


    // Adaugă un proiect nou
    public ProjectDTO addProject(ProjectDTO project) {
        logger.info("Adding new project: " + project);
        HttpHeaders headers = generateHeaders();
        HttpEntity<ProjectDTO> entity = new HttpEntity<>(project, headers);

        ProjectDTO savedProject = restTemplate.exchange(
                backendUrl, // Endpoint-ul backend-ului
                HttpMethod.POST, // Metoda HTTP
                entity, // Cererea cu obiectul DTO și antetele
                ProjectDTO.class // Tipul răspunsului
        ).getBody();

        logger.info("Added project: " + savedProject);
        return savedProject;
    }

    // Actualizează un proiect
    public ProjectDTO updateProject(ProjectDTO project) {
        logger.info("Updating project: " + project);
        HttpHeaders headers = generateHeaders();
        HttpEntity<ProjectDTO> entity = new HttpEntity<>(project, headers);

        ProjectDTO updatedProject = restTemplate.exchange(
                backendUrl, // Endpoint-ul backend-ului
                HttpMethod.PUT, // Metoda HTTP
                entity, // Cererea cu obiectul DTO și antetele
                ProjectDTO.class // Tipul răspunsului
        ).getBody();

        logger.info("Updated project: " + updatedProject);
        return updatedProject;
    }

    // Șterge un proiect după ID
    public void deleteProject(Integer projectId) {
        logger.info("Deleting project with ID: " + projectId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                backendUrl + "/" + projectId, // Endpoint-ul backend-ului cu ID-ul proiectului
                HttpMethod.DELETE, // Metoda HTTP
                entity, // Antetele cererii
                Void.class // Tipul răspunsului
        );

        logger.info("Deleted project with ID: " + projectId);
    }

    // Găsește proiecte după status
    public List<ProjectDTO> findProjectsByStatus(String status) {
        logger.info("Fetching projects with status: " + status);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ProjectDTO[] projects = restTemplate.exchange(
                backendUrl + "/status/" + status, // Endpoint-ul backend-ului
                HttpMethod.GET, // Metoda HTTP
                entity, // Antetele cererii
                ProjectDTO[].class // Tipul răspunsului
        ).getBody();

        return List.of(projects);
    }

    // Găsește proiecte după client
    public List<ProjectDTO> findProjectsByClient(String clientName) {
        logger.info("Fetching projects for client: " + clientName);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ProjectDTO[] projects = restTemplate.exchange(
                backendUrl + "/client/" + clientName, // Endpoint-ul backend-ului
                HttpMethod.GET, // Metoda HTTP
                entity, // Antetele cererii
                ProjectDTO[].class // Tipul răspunsului
        ).getBody();

        return List.of(projects);
    }

    // Arhivează un proiect
    public void archiveProject(Integer projectId) {
        logger.info("Archiving project with ID: " + projectId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                backendUrl + "/" + projectId + "/archive", // Endpoint-ul backend-ului
                HttpMethod.PUT, // Metoda HTTP
                entity, // Antetele cererii
                Void.class // Tipul răspunsului
        );

        logger.info("Archived project with ID: " + projectId);
    }

    // Generarea headerelor pentru cereri
    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
