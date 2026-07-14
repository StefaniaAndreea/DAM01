package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.*;
import com.audit_management_app.auditmanagement_DAM.domain.services.*;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private IVulnerabilityRepository vulnerabilityRepository;

    @Transactional
    @Override
    public Project addProject(Project project) throws IllegalArgumentException {
        // Verificăm dacă clientul există
        Client client = clientRepository.findById(project.getClient().getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + project.getClient().getClientId() + " does not exist."));

        // Verificăm dacă echipa există
        AuditTeam team = teamRepository.findById(project.getTeam().getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Audit team with ID " + project.getTeam().getTeamId() + " does not exist."));

        // Verificăm dacă echipa este deja asignată unui alt proiect în același interval
        boolean isAssigned = projectRepository.isTeamAssignedToAnotherProject(
                team.getTeamId(), project.getStartDate(), project.getEndDate());

        if (isAssigned) {
            throw new IllegalArgumentException("Team is already assigned to another project during this period.");
        }

        // Salvăm proiectul
        Project savedProject = projectRepository.save(project);

        // Actualizăm listele echipei și clientului
        team.getAssignedProjects().add(savedProject);
        client.getProjects().add(savedProject);

        teamRepository.save(team);
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

        // Gestionăm schimbarea echipei doar dacă echipa a fost modificată
        if (project.getTeam() != null && !project.getTeam().equals(existingProject.getTeam())) {
            AuditTeam newTeam = project.getTeam();

            // Verificăm dacă echipa este deja asignată la alt proiect
            boolean isAssigned = projectRepository.isTeamAssignedToAnotherProject(
                    newTeam.getTeamId(), project.getStartDate(), project.getEndDate());

            if (isAssigned) {
                throw new IllegalArgumentException("The specified team is already assigned to another project in the same time period.");
            }

            // Actualizăm relația cu echipele
            if (existingProject.getTeam() != null) {
                existingProject.getTeam().getAssignedProjects().remove(existingProject);
                teamRepository.save(existingProject.getTeam());
            }

            newTeam.getAssignedProjects().add(existingProject);
            teamRepository.save(newTeam);

            existingProject.setTeam(newTeam);
        }

        return projectRepository.save(existingProject);
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
// Ștergem toate vulnerabilitățile asociate rapoartelor proiectului
        for (AuditReport report : project.getReports()) {
            List<Vulnerability> vulnerabilities = report.getVulnerabilities();
            vulnerabilityRepository.deleteAll(vulnerabilities); // Ștergem vulnerabilitățile
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

    @Override
    public Project findProjectById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + id + " not found"));
    }
}
