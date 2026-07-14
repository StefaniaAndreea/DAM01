package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectEntityFactory;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Service
public class ProjectEntityFactoryJPA implements IProjectEntityFactory {

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IAuditTeamRepository teamRepository;

    @Override
    @Transactional
    public Task createTask(String description, int projectId) {
        // Găsim proiectul după ID
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Proiectul nu a fost găsit: ID " + projectId));

        // Creăm și configurăm o nouă sarcină
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(Task.TaskStatus.PENDING);
        task.setProject(project);

        // Adăugăm sarcina la lista de sarcini a proiectului
        project.getTasks().add(task);

        // Salvăm proiectul, care include și sarcina
        projectRepository.save(project);

        return task;
    }

    @Override
    @Transactional
    public Project createProject(String name, int clientId, int teamId, Date startDate, Date endDate) {
        // Creăm un nou proiect
        Project project = new Project();
        project.setName(name);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setProgress(0);
        project.setStatus(Project.StatusProiect.ONGOING);

        // Setăm listele de rapoarte și sarcini (goală inițial)
        project.setReports(new ArrayList<>());
        project.setTasks(new ArrayList<>());

        // Asociem proiectul unui client
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Clientul nu a fost găsit: ID " + clientId));
        project.setClient(client);

        // Asociem proiectul unei echipe
        AuditTeam team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Echipa nu a fost găsită: ID " + teamId));
        project.setTeam(team);

        // Salvăm proiectul
        return projectRepository.save(project);
    }

    @Override
    public Project toEntity(Project projectDTO) {
        // Găsim entitatea existentă
        Project projectEntity = projectRepository.findById(projectDTO.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Proiectul nu a fost găsit: ID " + projectDTO.getProjectId()));

        // Actualizăm câmpurile proiectului existent
        projectEntity.setName(projectDTO.getName());
        projectEntity.setStartDate(projectDTO.getStartDate());
        projectEntity.setEndDate(projectDTO.getEndDate());
        projectEntity.setStatus(projectDTO.getStatus());
        projectEntity.setProgress(projectDTO.getProgress());

        return projectEntity;
    }
}