package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.services.ITaskService;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectRepository;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TaskServiceTest {

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IAuditTeamRepository teamRepository;

    @Autowired
    private IEmployeeRepository employeeRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceTest.class);



    @Test
    void testMarkAsDone() {
        // Arrange: Creăm clientul și echipa
        Client client = new Client();
        client.setName("Test Client");
        client.setContactPerson("John Client");
        client.setContactEmail("john.client@example.com");
        client = clientRepository.save(client);

        AuditTeam team = new AuditTeam();
        team.setTeamName("Audit Team");
        team = teamRepository.save(team);

        // Creăm angajatul pentru test
        Employee employee = new Employee();
        employee.setName("Default Employee");
        employee.setRole(Employee.EmployeeRole.PENTESTER);
        employee.setAvailable(true);
        employee = employeeRepository.save(employee);

        // Creăm proiectul
        Project project = new Project();
        project.setName("Test Project");
        project.setClient(client);
        project.setTeam(team);
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setStatus(Project.StatusProiect.ONGOING);
        project.setTasks(new ArrayList<>());

        // Creăm sarcina
        Task task = new Task();
        task.setDescription("Test Task");
        task.setStatus(Task.TaskStatus.PENDING);
        task.setAssignedTo(employee);
        task.setProject(project); // Asociem proiectul sarcinii
        project.getTasks().add(task); // Adăugăm sarcina la proiect

        project = projectRepository.save(project); // Salvăm proiectul și implicit sarcina

        // Act: Marcăm sarcina ca "DONE"
        Task savedTask = project.getTasks().get(0); // Luăm sarcina din proiectul salvat
        taskService.markAsDone(savedTask.getTaskId());

        // Assert: Verificăm că statusul sarcinii este actualizat
        Project updatedProject = projectRepository.findById(project.getProjectId()).orElseThrow();
        Task updatedTask = updatedProject.getTasks().stream()
                .filter(t -> t.getTaskId().equals(savedTask.getTaskId()))
                .findFirst()
                .orElseThrow();

        assertEquals(Task.TaskStatus.COMPLETED, updatedTask.getStatus(), "Task status should be COMPLETED.");
    }

    @Test
    void testAssignEmployee() {
        // Arrange: Creăm clientul și echipa
        Client client = new Client();
        client.setName("Test Client");
        client.setContactPerson("John Client");
        client.setContactEmail("john.client@example.com");
        client = clientRepository.save(client);

        AuditTeam team = new AuditTeam();
        team.setTeamName("Audit Team");
        team = teamRepository.save(team);

        // Creăm angajatul pentru test
        Employee employee = new Employee();
        employee.setName("Default Employee");
        employee.setRole(Employee.EmployeeRole.PENTESTER);
        employee.setAvailable(true);
        employee = employeeRepository.save(employee);

        // Creăm proiectul și sarcina
        Project project = new Project();
        project.setName("Test Project");
        project.setClient(client);
        project.setTeam(team);
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setStatus(Project.StatusProiect.ONGOING);
        project.setTasks(new ArrayList<>());

        Task task = new Task();
        task.setDescription("Test Task");
        task.setStatus(Task.TaskStatus.PENDING);
        task.setAssignedTo(employee);
        task.setProject(project); // Setăm proiectul în sarcină
        project.getTasks().add(task);

        project = projectRepository.save(project);

        // Creăm un alt angajat pentru atribuire
        Employee newEmployee = new Employee();
        newEmployee.setName("John Doe");
        newEmployee.setRole(Employee.EmployeeRole.PENTESTER);
        newEmployee.setAvailable(true);
        newEmployee = employeeRepository.save(newEmployee);

        assertNotNull(task.getTaskId(), "Task ID should not be null after saving.");
        assertNotNull(newEmployee.getEmployeeId(), "New Employee ID should not be null after saving.");

        // Act: Atribuim angajatul sarcinii
        taskService.assignEmployee(task.getTaskId(), newEmployee.getEmployeeId());

        // Assert: Verificăm că sarcina este asociată cu angajatul corect
        Project updatedProject = projectRepository.findById(project.getProjectId()).orElseThrow();
        Task updatedTask = updatedProject.getTasks().stream()
                .filter(t -> t.getTaskId().equals(task.getTaskId()))
                .findFirst()
                .orElseThrow();

        assertNotNull(updatedTask.getAssignedTo(), "Task should have an assigned employee.");
        assertEquals(newEmployee.getEmployeeId(), updatedTask.getAssignedTo().getEmployeeId(), "Task should be assigned to the correct employee.");
    }
    @Test
    void testAssignEmployeeNotAvailable() {
        logger.info(() -> "Starting test: testAssignEmployeeNotAvailable");

        // Arrange: Creăm un angajat indisponibil
        Employee unavailableEmployee = new Employee();
        unavailableEmployee.setName("Jane Doe");
        unavailableEmployee.setRole(Employee.EmployeeRole.PENTESTER);
        unavailableEmployee.setAvailable(false);
        unavailableEmployee = employeeRepository.save(unavailableEmployee);

        // Creăm un client valid
        Client client = new Client();
        client.setName("CyberSecure Inc.");
        client.setContactPerson("John Doe");
        client.setContactEmail("john.doe@cybersecure.com");
        client = clientRepository.save(client);


        // Creăm o echipă validă
        AuditTeam auditTeam = new AuditTeam();
        auditTeam.setTeamName("Audit Team Alpha");
        auditTeam = teamRepository.save(auditTeam);


        // Creăm un proiect valid
        Project project = new Project();
        project.setName("Test Project");
        project.setStatus(Project.StatusProiect.ONGOING);
        project.setClient(client);
        project.setTeam(auditTeam);
        project = projectRepository.save(project);


        // Creăm o sarcină asociată proiectului
        Task task = new Task();
        task.setDescription("Test Task");
        task.setStatus(Task.TaskStatus.PENDING);
        task.setProject(project);
        project.getTasks().add(task);
        project = projectRepository.save(project);

        final Task savedTask = project.getTasks().get(0);
        final Employee finalUnavailableEmployee = unavailableEmployee;

        // Act & Assert
        logger.info(() -> "Attempting to assign task " + savedTask.getTaskId() + " to unavailable employee " + finalUnavailableEmployee.getEmployeeId());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.assignEmployee(savedTask.getTaskId(), finalUnavailableEmployee.getEmployeeId());
        });

        logger.info(() -> "Assignment failed as expected with message: " + exception.getMessage());
        assertEquals("Employee is not available.", exception.getMessage());

        logger.info(() -> "Test completed: testAssignEmployeeNotAvailable");
    }

}