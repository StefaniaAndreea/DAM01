package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectRepository;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class IProjectRepositoryTest {

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IAuditTeamRepository auditTeamRepository;

    @Test
    void testFindProjectsByStatus() {
        // Arrange: Creăm entități pentru test
        Client client = new Client();
        client.setName("CyberSecure Inc.");
        client.setContactPerson("John Doe");
        client.setContactEmail("john.doe@cybersecure.com");
        clientRepository.save(client);

        AuditTeam team = new AuditTeam();
        team.setTeamName("Security Team");
        auditTeamRepository.save(team);

        Project project = new Project();
        project.setName("Cyber Audit 2024");
        project.setClient(client);
        project.setTeam(team);
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setStatus(Project.StatusProiect.ONGOING);
        projectRepository.save(project);

        // Act: Căutăm proiectele după status
        List<Project> ongoingProjects = projectRepository.findByStatus(Project.StatusProiect.ONGOING);

        // Assert: Verificăm rezultatele
        assertNotNull(ongoingProjects, "Lista de proiecte nu trebuie să fie null.");
        assertFalse(ongoingProjects.isEmpty(), "Lista de proiecte nu trebuie să fie goală.");
        assertEquals("Cyber Audit 2024", ongoingProjects.get(0).getName(), "Numele proiectului nu se potrivește.");
    }

    @Test
    void testFindProjectsByClientName() {
        // Arrange: Creăm entități pentru test
        Client client = new Client();
        client.setName("Secure Solutions");
        client.setContactPerson("Alice Smith");
        client.setContactEmail("alice.smith@securesolutions.com");
        clientRepository.save(client);

        Project project = new Project();
        project.setName("Network Security Review");
        project.setClient(client);
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setStatus(Project.StatusProiect.COMPLETED);
        projectRepository.save(project);

        // Act: Căutăm proiectele după numele clientului
        List<Project> clientProjects = projectRepository.findProjectsByClientName("Secure Solutions");

        // Assert: Verificăm rezultatele
        assertNotNull(clientProjects, "Lista de proiecte nu trebuie să fie null.");
        assertFalse(clientProjects.isEmpty(), "Lista de proiecte nu trebuie să fie goală.");
        assertEquals("Network Security Review", clientProjects.get(0).getName(), "Numele proiectului nu se potrivește.");
    }
}
