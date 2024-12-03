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
        employee.setRole(Employee.EmployeeRole.AUDITOR);
        employee.setAvailable(true);
        employee = employeeRepository.save(employee);

        // Creăm proiectul și sarcina
        Project project = new Project();
        project.setName("Test Project");
        project.setClient(client); // Setăm relația ManyToOne cu Client
        project.setTeam(team); // Setăm relația ManyToOne cu AuditTeam
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setStatus(Project.StatusProiect.ONGOING);
        project.setTasks(new ArrayList<>());

        Task task = new Task();
        task.setDescription("Test Task");
        task.setStatus(Task.TaskStatus.PENDING);
        task.setAssignedTo(employee); // Setăm angajatul
        project.getTasks().add(task);

        project = projectRepository.save(project);

        // Act: Marcăm sarcina ca "DONE"
        taskService.markAsDone(task.getTaskId());

        // Assert: Verificăm că statusul sarcinii este actualizat
        Project updatedProject = projectRepository.findById(project.getProjectId()).orElseThrow();
        Task updatedTask = updatedProject.getTasks().stream()
                .filter(t -> Long.valueOf(t.getTaskId()).equals(Long.valueOf(task.getTaskId())))
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
        employee.setRole(Employee.EmployeeRole.AUDITOR);
        employee.setAvailable(true);
        employee = employeeRepository.save(employee);

        // Creăm proiectul și sarcina
        Project project = new Project();
        project.setName("Test Project");
        project.setClient(client); // Setăm relația ManyToOne cu Client
        project.setTeam(team); // Setăm relația ManyToOne cu AuditTeam
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setStatus(Project.StatusProiect.ONGOING);
        project.setTasks(new ArrayList<>());

        Task task = new Task();
        task.setDescription("Test Task");
        task.setStatus(Task.TaskStatus.PENDING);
        task.setAssignedTo(employee); // Setăm angajatul
        project.getTasks().add(task);

        project = projectRepository.save(project);

        // Creăm un alt angajat pentru atribuire
        Employee newEmployee = new Employee();
        newEmployee.setName("John Doe");
        newEmployee.setRole(Employee.EmployeeRole.PENTESTER);
        newEmployee.setAvailable(true);
        newEmployee = employeeRepository.save(newEmployee);

        // Act: Atribuim angajatul sarcinii
        taskService.assignEmployee(task.getTaskId(), newEmployee.getEmployeeId());

        // Assert: Verificăm că sarcina este asociată cu angajatul corect
        Project updatedProject = projectRepository.findById(project.getProjectId()).orElseThrow();
        Task updatedTask = updatedProject.getTasks().stream()
                .filter(t -> Long.valueOf(t.getTaskId()).equals(Long.valueOf(task.getTaskId())))
                .findFirst()
                .orElseThrow();

        assertNotNull(updatedTask.getAssignedTo(), "Task should have an assigned employee.");
        assertEquals(newEmployee.getEmployeeId(), updatedTask.getAssignedTo().getEmployeeId(), "Task should be assigned to the correct employee.");
    }
}