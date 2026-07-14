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
public class ProjectService implements IProjectService {

    private static final Logger logger = Logger.getLogger(ProjectService.class.getName());

    @Value("${backend.url.projects}")
    private String backendUrl;

    private final RestTemplate restTemplate;

    public ProjectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        logger.info("Fetching all projects from backend: " + backendUrl);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ProjectDTO[] projects = restTemplate.exchange(
                backendUrl,
                HttpMethod.GET,
                entity,
                ProjectDTO[].class
        ).getBody();

        return List.of(projects);
    }

    @Override
    public ProjectDTO getProjectById(Integer projectId) {
        String url = backendUrl + "/" + projectId;
        logger.info("Fetching project with ID: " + projectId);
        ProjectDTO project = restTemplate.getForObject(url, ProjectDTO.class);
        logger.info("Fetched project: " + project);
        return project;
    }

    @Override
    public ProjectDTO addProject(ProjectDTO project) {
        logger.info("Adding new project: " + project);
        HttpHeaders headers = generateHeaders();
        HttpEntity<ProjectDTO> entity = new HttpEntity<>(project, headers);

        ProjectDTO savedProject = restTemplate.exchange(
                backendUrl,
                HttpMethod.POST,
                entity,
                ProjectDTO.class
        ).getBody();

        logger.info("Added project: " + savedProject);
        return savedProject;
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO project) {
        logger.info("Updating project: " + project);
        HttpHeaders headers = generateHeaders();
        HttpEntity<ProjectDTO> entity = new HttpEntity<>(project, headers);

        ProjectDTO updatedProject = restTemplate.exchange(
                backendUrl,
                HttpMethod.PUT,
                entity,
                ProjectDTO.class
        ).getBody();

        logger.info("Updated project: " + updatedProject);
        return updatedProject;
    }

    @Override
    public void deleteProject(Integer projectId) {
        logger.info("Deleting project with ID: " + projectId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                backendUrl + "/" + projectId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        logger.info("Deleted project with ID: " + projectId);
    }

    @Override
    public List<ProjectDTO> findProjectsByStatus(String status) {
        logger.info("Fetching projects with status: " + status);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ProjectDTO[] projects = restTemplate.exchange(
                backendUrl + "/status/" + status,
                HttpMethod.GET,
                entity,
                ProjectDTO[].class
        ).getBody();

        return List.of(projects);
    }

    @Override
    public List<ProjectDTO> findProjectsByClient(String clientName) {
        logger.info("Fetching projects for client: " + clientName);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ProjectDTO[] projects = restTemplate.exchange(
                backendUrl + "/client/" + clientName,
                HttpMethod.GET,
                entity,
                ProjectDTO[].class
        ).getBody();

        return List.of(projects);
    }

    @Override
    public void archiveProject(Integer projectId) {
        logger.info("Archiving project with ID: " + projectId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                backendUrl + "/" + projectId + "/archive",
                HttpMethod.PUT,
                entity,
                Void.class
        );

        logger.info("Archived project with ID: " + projectId);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
