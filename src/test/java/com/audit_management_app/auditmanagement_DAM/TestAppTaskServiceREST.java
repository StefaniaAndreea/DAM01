package com.audit_management_app.auditmanagement_DAM;
import com.audit_management_app.auditmanagement_DAM.dto.ProjectDTO;
import com.audit_management_app.auditmanagement_DAM.dto.TaskDTO;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task.TaskStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

import static com.audit_management_app.auditmanagement_DAM.domain.projects.Task.TaskStatus.PENDING;
import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAppTaskServiceREST {
    private static Integer taskId; // Variabilă globală și statică
    private static Logger logger = Logger.getLogger(TestAppTaskServiceREST.class.getName());
    private static String BASE_URL = "http://localhost:8088/api/rest/app/tasks";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(0)
    public void setupProject() {
        logger.info("DEBUG: Setting up a test project...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setClientId(1); // ID-ul unui client existent
        projectDTO.setTeamId(1);   // ID-ul unei echipe existente
        projectDTO.setStatus("ONGOING");

        HttpEntity<ProjectDTO> request = new HttpEntity<>(projectDTO, headers);

        ProjectDTO response = restTemplate.exchange(
                "http://localhost:8088/api/rest/app/project",
                HttpMethod.POST,
                request,
                ProjectDTO.class
        ).getBody();

        assertNotNull(response, "Failed to create test project!");
        logger.info("Test project created with ID: " + response.getProjectId());
    }
    @Test
    @Order(1)
    public void testAddTaskToProject() {
        logger.info("DEBUG: Testing POST add task to project ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription("Task Description");
        taskDTO.setStatus(TaskStatus.PENDING);
        taskDTO.setAssignedToId(1);

        HttpEntity<TaskDTO> request = new HttpEntity<>(taskDTO, headers);

        TaskDTO response = restTemplate.exchange(
                BASE_URL + "/projects/1",
                HttpMethod.POST,
                request,
                TaskDTO.class
        ).getBody();

        assertNotNull(response, "Failed to add task!");
        assertNotNull(response.getTaskId(), "Task ID must not be null!");

        taskId = response.getTaskId(); // Setează `taskId` pentru testele ulterioare
        logger.info("Task added with ID: " + taskId);
    }

    @Test
    @Order(2)
    public void testAddTaskWithoutEmployee() {
        logger.info("DEBUG: Testing POST add task without employee ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription("Review project documentation");
        taskDTO.setStatus(TaskStatus.IN_PROGRESS);

        HttpEntity<TaskDTO> request = new HttpEntity<>(taskDTO, headers);

        TaskDTO response = restTemplate.exchange(
                BASE_URL + "/projects/1/without-employee",
                HttpMethod.POST,
                request,
                TaskDTO.class
        ).getBody();

        assertNotNull(response, "Failed to add task without employee!");
        assertNotNull(response.getTaskId(), "Task ID must not be null!");

        taskId = response.getTaskId();
        logger.info("Task without employee added with ID: " + taskId);
    }

    @Test
    @Order(3)
    public void testMarkTaskAsDone() {
        logger.info("DEBUG: Testing PUT mark task as done ...");

        // Verificăm dacă taskId este setat, altfel folosim un ID fix pentru test
        if (taskId == null) {
            taskId = 1; // ID-ul unui task existent
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);

        String response = restTemplate.exchange(
                BASE_URL + "/" + taskId + "/mark-as-done",
                HttpMethod.PUT,
                request,
                String.class
        ).getBody();

        assertNotNull(response, "Failed to mark task as done!");
        assertTrue(response.contains("marked as COMPLETED"));
        logger.info("Task with ID " + taskId + " marked as COMPLETED.");
    }

    @Test
    @Order(4)
    public void testAssignEmployeeToTask() {
        logger.info("DEBUG: Testing PUT assign employee to task ...");

        if (taskId == null) {
            taskId = 1; // ID-ul unui task existent
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);

        String response = restTemplate.exchange(
                BASE_URL + "/" + taskId + "/assign-employee/1",
                HttpMethod.PUT,
                request,
                String.class
        ).getBody();

        assertNotNull(response, "Failed to assign employee to task!");
        assertTrue(response.contains("assigned to task"));
        logger.info("Employee assigned to task with ID: " + taskId);
    }
//    @Test
//    @Order(5)
//    public void testUpdateTask() {
//        logger.info("DEBUG: Testing PUT update task ...");
//
//        // Setăm ID-ul task-ului pe care vrem să-l actualizăm
//        Integer specificTaskId = 1;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
//        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//
//        // DTO doar cu câmpurile necesare
//        TaskDTO taskDTO = new TaskDTO();
//        taskDTO.setTaskId(specificTaskId);
//        taskDTO.setDescription("Updated specific task description");
//
//        HttpEntity<TaskDTO> request = new HttpEntity<>(taskDTO, headers);
//
//        // Trimitere cerere PUT
//        TaskDTO response = restTemplate.exchange(
//                BASE_URL + "/" + specificTaskId,
//                HttpMethod.PUT,
//                request,
//                TaskDTO.class
//        ).getBody();
//
//        // Validare răspuns
//        assertNotNull(response, "Failed to update task!");
//        assertEquals(specificTaskId, response.getTaskId(), "Task ID does not match!");
//        assertEquals("Updated specific task description", response.getDescription(), "Task description does not match!");
//
//        logger.info("Task updated successfully: " + response);
//    }

    @Test
    @Order(6)
    public void testFindTasksByStatus() {
        logger.info("DEBUG: Testing GET tasks by status ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<TaskDTO> response = restTemplate.exchange(
                BASE_URL + "/status/PENDING",
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve tasks by status!");
        logger.info("Tasks with status PENDING: " + response.size());
    }

    @Test
    @Order(7)
    public void testFindTasksByEmployee() {
        logger.info("DEBUG: Testing GET tasks by employee ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<TaskDTO> response = restTemplate.exchange(
                BASE_URL + "/employee/1",
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve tasks by employee!");
        logger.info("Tasks for employee ID 1: " + response.size());
    }

    @Test
    @Order(8)
    public void testFindTasksByProject() {
        logger.info("DEBUG: Testing GET tasks by project ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<TaskDTO> response = restTemplate.exchange(
                BASE_URL + "/project/1",
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve tasks by project!");
        logger.info("Tasks for project ID 1: " + response.size());
    }

    @Test
    @Order(9)
    public void testFindTasksByEmployeeAndStatus() {
        logger.info("DEBUG: Testing GET tasks by employee and status ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<TaskDTO> response = restTemplate.exchange(
                BASE_URL + "/employee/1/status/PENDING",
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve tasks by employee and status!");
        logger.info("Tasks for employee ID 1 with status PENDING: " + response.size());
    }

    @Test
    @Order(10)
    public void testDeleteTask() {
        logger.info("DEBUG: Testing DELETE task ...");

        // Verificăm dacă taskId este setat, altfel folosim un ID fix pentru test
        if (taskId == null) {
            taskId = 1; // ID-ul unui task existent
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        restTemplate.exchange(
                BASE_URL + "/" + taskId,
                HttpMethod.DELETE,
                request,
                Void.class
        );

        logger.info("Task with ID " + taskId + " deleted.");
    }
}
