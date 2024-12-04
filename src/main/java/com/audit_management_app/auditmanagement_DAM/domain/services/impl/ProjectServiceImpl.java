package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Project project) throws IllegalArgumentException {
        // Verificăm dacă proiectul există
        Project existingProject = projectRepository.findById(project.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + project.getProjectId() + " does not exist."));

        // Actualizăm câmpurile proiectului
        existingProject.setName(project.getName());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setEndDate(project.getEndDate());
        existingProject.setProgress(project.getProgress());

        return projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(int projectId) throws IllegalArgumentException {
        // Verificăm dacă proiectul există
        if (!projectRepository.existsById(projectId)) {
            throw new IllegalArgumentException("Project with ID " + projectId + " does not exist.");
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

}
