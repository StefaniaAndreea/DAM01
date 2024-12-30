package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectService;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IAuditTeamRepository teamRepository;

    @Override
    public Project addProject(Project project) throws IllegalArgumentException {
        // Verificăm dacă clientul există
        if (!clientRepository.existsById(project.getClient().getClientId())) {
            throw new IllegalArgumentException("Client with ID " + project.getClient().getClientId() + " does not exist.");
        }

        // Verificăm dacă echipa există
        if (!teamRepository.existsById(project.getTeam().getTeamId())) {
            throw new IllegalArgumentException("Audit team with ID " + project.getTeam().getTeamId() + " does not exist.");
        }

        // Salvăm proiectul
        Project savedProject = projectRepository.save(project);
        // Adăugăm proiectul în lista echipei
        AuditTeam team = savedProject.getTeam();
        team.getAssignedProjects().add(savedProject);
        teamRepository.save(team);

        // Adăugăm proiectul în lista clientului
        Client client=savedProject.getClient();
        client.getProjects().add(savedProject);
        clientRepository.save(client);

        return savedProject;
    }

    @Override
    public Project updateProject(Project project) throws IllegalArgumentException {
        // Verificăm dacă proiectul există
        Project existingProject = projectRepository.findById(project.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + project.getProjectId() + " does not exist."));

        // Verificăm dacă proiectul este arhivat
        if (existingProject.getStatus() == Project.StatusProiect.ARCHIVED) {
            throw new IllegalArgumentException("Archived projects cannot be modified.");
        }

        // Actualizăm câmpurile proiectului
        existingProject.setName(project.getName());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setEndDate(project.getEndDate());
        existingProject.setProgress(project.getProgress());
        existingProject.setStatus(project.getStatus());
        // Actualizăm echipa doar dacă este permis conform conditiei specificate anterior
        // Gestionăm schimbarea echipei
        if (!existingProject.getTeam().equals(project.getTeam())) {
            AuditTeam oldTeam = existingProject.getTeam();
            AuditTeam newTeam = project.getTeam();

            // Eliminăm proiectul din lista echipei vechi
            if (oldTeam != null) {
                oldTeam.getAssignedProjects().remove(existingProject);
                teamRepository.save(oldTeam);
            }

            // Adăugăm proiectul în lista echipei noi
            if (newTeam != null) {
                newTeam.getAssignedProjects().add(existingProject);
                teamRepository.save(newTeam);
            }

            // Actualizăm echipa proiectului
            existingProject.setTeam(newTeam);
        }        return projectRepository.save(existingProject);
    }


    @Override
    public void deleteProject(int projectId) throws IllegalArgumentException {
        // Verificăm dacă proiectul există
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));

        // Eliminăm proiectul din lista echipei
        AuditTeam team = project.getTeam();
        if (team != null) {
            team.getAssignedProjects().remove(project);
            teamRepository.save(team);
        }

        //Eliminam proiectul din lista clientului
        Client client = project.getClient();
        if (client !=null){
            client.getProjects().remove(project);
            clientRepository.save(client);
        }

        // Ștergem proiectul
        projectRepository.deleteById(projectId);
    }

    @Override
    public void archiveProject(int projectId) throws IllegalArgumentException {
        // Găsim proiectul
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));

        // Marcăm proiectul ca arhivat
        project.setStatus(Project.StatusProiect.ARCHIVED);
        projectRepository.save(project);
    }

    @Override
    public List<Project> findProjectsByStatus(Project.StatusProiect status) {
        return projectRepository.findByStatus(status);
    }
    @Override
    public List<Project> findProjectsByClient(String clientName) throws IllegalArgumentException {
        List<Project> projects = projectRepository.findProjectsByClientName(clientName);
        if (projects.isEmpty()) {
            throw new IllegalArgumentException("Nu există proiecte asociate clientului cu numele: " + clientName);
        }
        return projects;
    }

    @Override
    public List<Project> findAllProjects() {
        // Returnează toate proiectele folosind repository-ul
        return projectRepository.findAll();
    }


}
