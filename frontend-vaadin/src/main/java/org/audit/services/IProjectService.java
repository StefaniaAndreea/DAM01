package org.audit.services;

import org.audit.dto.ProjectDTO;

import java.util.List;

public interface IProjectService {
    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(Integer projectId);
    ProjectDTO addProject(ProjectDTO project);
    ProjectDTO updateProject(ProjectDTO project);
    void deleteProject(Integer projectId);
    List<ProjectDTO> findProjectsByStatus(String status);
    List<ProjectDTO> findProjectsByClient(String clientName);
    void archiveProject(Integer projectId);
}