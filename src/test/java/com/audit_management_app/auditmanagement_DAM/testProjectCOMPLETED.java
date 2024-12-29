package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.services.*;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class testProjectCOMPLETED {

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IAuditTeamRepository auditTeamRepository;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IProjectService projectService;
    private static final Logger logger = LoggerFactory.getLogger(testProjectCOMPLETED.class);


    @Test
    public void testMarkTaskAsDone_UpdatesProjectStatus() {
        Client client1=new Client("Vladimir","Anamaria","savastefania@gmail.com","test client");
        clientRepository.save(client1);
        AuditTeam auditTeam1=new AuditTeam("DAM01");
        auditTeamRepository.save(auditTeam1);

        // Setup: Creează un proiect și două task-uri
        Project project = new Project();
        project.setName("Test Project");
        project.setClient(client1);
        project.setTeam(auditTeam1);
        project.setStatus(Project.StatusProiect.ONGOING);
        projectRepository.save(project);

        Task task1 = new Task();
        task1.setDescription("Task 1");
        task1.setStatus(Task.TaskStatus.IN_PROGRESS);
        task1.setProject(project);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setDescription("Task 2");
        task2.setStatus(Task.TaskStatus.IN_PROGRESS);
        task2.setProject(project);
        taskRepository.save(task2);

        // Act: Marchează task-urile ca "COMPLETED"
        taskService.markAsDone(task1.getTaskId());
        taskService.markAsDone(task2.getTaskId());

        // Assert: Verifică că proiectul este acum COMPLETED
        Project updatedProject = projectRepository.findById(project.getProjectId()).orElseThrow();
        assertEquals(Project.StatusProiect.COMPLETED, updatedProject.getStatus());
    }

    @Test
    public void testUpdateArchivedProject_ThrowsException() {
        Client client1=new Client("Vladimir","Anamaria","savastefania@gmail.com","test client");
        clientRepository.save(client1);
        AuditTeam auditTeam1=new AuditTeam("DAM01");
        auditTeamRepository.save(auditTeam1);
        Project project = new Project();
        project.setName("Archived Project");
        project.setClient(client1);
        project.setTeam(auditTeam1);
        project.setStatus(Project.StatusProiect.ARCHIVED);
        projectRepository.save(project);

        Project update = new Project();
        update.setProjectId(project.getProjectId());
        update.setName("Updated Name");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.updateProject(update);
        });

        assertEquals("Archived projects cannot be modified.", exception.getMessage());
        logger.error("Exception occurred: {}", exception.getMessage());
    }

}
