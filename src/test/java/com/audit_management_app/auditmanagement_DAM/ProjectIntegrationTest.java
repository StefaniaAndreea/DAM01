package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProjectIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testProjectCreationAndAssociations() {
        // Crearea unui client
        Client client = new Client();
        client.setName("CyberSecurity Inc.");
        client.setContactPerson("Alice Johnson");
        client.setContactEmail("alice@cybersecurity.com");
        client.setDescription("A client focused on cybersecurity solutions.");
        entityManager.persist(client);

        // Crearea unei echipe de audit
        AuditTeam auditTeam = new AuditTeam();
        auditTeam.setTeamName("Team Alpha");
        entityManager.persist(auditTeam);

        // Crearea unor angajați și adăugarea acestora în echipa de audit
        Employee employee1 = new Employee();
        employee1.setName("John Doe");
        employee1.setRole(Role.PENTESTER);
        employee1.setSalary(5000);
        employee1.setAvailable(true);
        entityManager.persist(employee1);

        Employee employee2 = new Employee();
        employee2.setName("Jane Smith");
        employee2.setRole(Role.MANAGER);
        employee2.setSalary(7000);
        employee2.setAvailable(true);
        entityManager.persist(employee2);

        auditTeam.setMembers(List.of(employee1, employee2));
        entityManager.merge(auditTeam);  // Actualizăm echipa cu membrii

        // Crearea unui proiect și asocierea acestuia cu clientul și echipa de audit
        Project project = new Project();
        project.setName("Vulnerability Assessment");
        project.setClient(client);
        project.setTeam(auditTeam);
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setStatus(Project.Status.ONGOING);
        project.setProgress(0.0f);
        entityManager.persist(project);

        // Verificăm dacă proiectul a fost salvat corect
        Project savedProject = entityManager.find(Project.class, project.getProjectId());
        assertNotNull(savedProject, "Proiectul ar trebui să fie salvat în baza de date");
        assertEquals("Vulnerability Assessment", savedProject.getName(), "Numele proiectului ar trebui să fie 'Vulnerability Assessment'");
        assertEquals(client, savedProject.getClient(), "Proiectul ar trebui să fie asociat cu clientul 'CyberSecurity Inc.'");
        assertEquals(auditTeam, savedProject.getTeam(), "Proiectul ar trebui să fie asociat cu echipa de audit 'Team Alpha'");

        // Verificăm dacă relația client-proiect funcționează corect
        assertTrue(client.getProjects().contains(savedProject), "Clientul ar trebui să conțină proiectul creat");

        // Verificăm dacă echipa are membrii salvați corect
        AuditTeam savedTeam = entityManager.find(AuditTeam.class, auditTeam.getTeamId());
        assertNotNull(savedTeam, "Echipa ar trebui să fie salvată în baza de date");
        assertEquals(2, savedTeam.getMembers().size(), "Echipa ar trebui să conțină 2 membri");

        // Verificăm dacă angajații au fost salvați corect și sunt asociați cu echipa de audit
        Employee savedEmployee1 = entityManager.find(Employee.class, employee1.getEmployeeId());
        Employee savedEmployee2 = entityManager.find(Employee.class, employee2.getEmployeeId());
        assertNotNull(savedEmployee1, "Angajatul 1 ar trebui să fie salvat în baza de date");
        assertNotNull(savedEmployee2, "Angajatul 2 ar trebui să fie salvat în baza de date");
        assertTrue(savedTeam.getMembers().contains(savedEmployee1), "Echipa ar trebui să conțină angajatul 1");
        assertTrue(savedTeam.getMembers().contains(savedEmployee2), "Echipa ar trebui să conțină angajatul 2");

        // Verificăm actualizarea progresului proiectului
        project.setProgress(50.0f);
        entityManager.merge(project);

        Project updatedProject = entityManager.find(Project.class, project.getProjectId());
        assertEquals(50.0f, updatedProject.getProgress(), "Progresul ar trebui să fie actualizat la 50%");

        // Verificăm actualizarea statusului proiectului
        project.setStatus(Project.Status.COMPLETED);
        entityManager.merge(project);

        Project updatedProjectStatus = entityManager.find(Project.class, project.getProjectId());
        assertEquals(Project.Status.COMPLETED, updatedProjectStatus.getStatus(), "Statusul ar trebui să fie 'COMPLETED'");

        // Test pentru eliminarea clientului și verificarea impactului asupra proiectului
        entityManager.remove(client);
        Project projectWithoutClient = entityManager.find(Project.class, project.getProjectId());
        assertNull(projectWithoutClient.getClient(), "Clientul ar trebui să fie eliminat din proiect după ștergerea clientului asociat");
    }
}
