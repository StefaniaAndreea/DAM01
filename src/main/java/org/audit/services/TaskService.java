package org.audit.services;

import org.audit.dto.TaskDTO;
import org.audit.dto.TaskStatus;
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
public class TaskService implements ITaskService {

    private static final Logger logger = Logger.getLogger(TaskService.class.getName());

    @Value("${backend.url.tasks}")
    private String backendUrl; // URL-ul pentru endpoint-urile backend-ului

    private final RestTemplate restTemplate;

    public TaskService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TaskDTO addTaskToProject(Integer projectId, TaskDTO task) {
        logger.info("Adding task to project ID: " + projectId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<TaskDTO> entity = new HttpEntity<>(task, headers);

        TaskDTO createdTask = restTemplate.exchange(
                backendUrl + "/projects/" + projectId,
                HttpMethod.POST,
                entity,
                TaskDTO.class
        ).getBody();

        logger.info("Created task: " + createdTask);
        return createdTask;
    }

    @Override
    public TaskDTO addTaskWithoutEmployee(Integer projectId, TaskDTO task) {
        logger.info("Adding task without employee to project ID: " + projectId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<TaskDTO> entity = new HttpEntity<>(task, headers);

        TaskDTO createdTask = restTemplate.exchange(
                backendUrl + "/projects/" + projectId + "/without-employee",
                HttpMethod.POST,
                entity,
                TaskDTO.class
        ).getBody();

        logger.info("Created task: " + createdTask);
        return createdTask;
    }

    @Override
    public void markTaskAsDone(Integer taskId) {
        logger.info("Marking task with ID: " + taskId + " as done");
        restTemplate.put(backendUrl + "/" + taskId + "/mark-as-done", null);
    }

    @Override
    public void assignEmployeeToTask(Integer taskId, Integer employeeId) {
        logger.info("Assigning employee ID: " + employeeId + " to task ID: " + taskId);
        restTemplate.put(backendUrl + "/" + taskId + "/assign-employee/" + employeeId, null);
    }

    @Override
    public TaskDTO updateTask(Integer taskId, TaskDTO task) {
        logger.info("Updating task with ID: " + taskId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<TaskDTO> entity = new HttpEntity<>(task, headers);

        TaskDTO updatedTask = restTemplate.exchange(
                backendUrl + "/" + taskId,
                HttpMethod.PUT,
                entity,
                TaskDTO.class
        ).getBody();

        logger.info("Updated task: " + updatedTask);
        return updatedTask;
    }

    @Override
    public void deleteTask(Integer taskId) {
        logger.info("Deleting task with ID: " + taskId);
        restTemplate.delete(backendUrl + "/" + taskId);
    }

    @Override
    public List<TaskDTO> getTasksByStatus(TaskStatus status) {
        logger.info("Fetching tasks with status: " + status);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        TaskDTO[] tasks = restTemplate.exchange(
                backendUrl + "/status/" + status,
                HttpMethod.GET,
                entity,
                TaskDTO[].class
        ).getBody();

        return List.of(tasks);
    }

    @Override
    public List<TaskDTO> getTasksByEmployee(Integer employeeId) {
        logger.info("Fetching tasks for employee ID: " + employeeId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        TaskDTO[] tasks = restTemplate.exchange(
                backendUrl + "/employee/" + employeeId,
                HttpMethod.GET,
                entity,
                TaskDTO[].class
        ).getBody();

        return List.of(tasks);
    }

    @Override
    public List<TaskDTO> getTasksByProject(Integer projectId) {
        logger.info("Fetching tasks for project ID: " + projectId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        TaskDTO[] tasks = restTemplate.exchange(
                backendUrl + "/project/" + projectId,
                HttpMethod.GET,
                entity,
                TaskDTO[].class
        ).getBody();

        return List.of(tasks);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        logger.info("Fetching all tasks");
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        TaskDTO[] tasks = restTemplate.exchange(
                backendUrl + "/all",
                HttpMethod.GET,
                entity,
                TaskDTO[].class
        ).getBody();

        return List.of(tasks);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
