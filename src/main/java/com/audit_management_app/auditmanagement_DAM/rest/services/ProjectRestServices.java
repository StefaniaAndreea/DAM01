package com.audit_management_app.auditmanagement_DAM.rest.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectService;
import com.audit_management_app.auditmanagement_DAM.domain.services.impl.ProjectServiceImpl;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.dto.ProjectDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/app/project")
@Transactional
public class ProjectRestServices {
    private static Logger logger = Logger.getLogger(ProjectServiceImpl.class.getName());

    private IProjectService projectService;
    // SBT Default Dependency Injection (Autowired for all prms)
    public ProjectRestServices(IProjectService projectService) {
        this.projectService = projectService;
    }
    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        logger.info("Fetching all projects...");
        // Obține toate proiectele folosind serviciul și convertește-le în DTO-uri
        return projectService.findAllProjects()  // Metoda din IProjectService
                .stream()
                .map(this::convertToDTO)        // Convertește fiecare entitate Project în DTO
                .collect(Collectors.toList()); // Returnează lista de DTO-uri
    }
    // POST: Adaugă un proiect nou
    @PostMapping
    @ResponseBody
    public ProjectDTO addProject(@Valid @RequestBody ProjectDTO projectDTO) {
        logger.info("Adding new project: " + projectDTO);
        Project project = convertToEntity(projectDTO);
        Project savedProject = projectService.addProject(project);
        return convertToDTO(savedProject);
    }

    // PUT: Actualizează un proiect existent
    @PutMapping
    @ResponseBody
    public ProjectDTO updateProject(@RequestBody ProjectDTO projectDTO) {
        logger.info("Received DTO for update: " + projectDTO);

        if (projectDTO.getProjectId() == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }

        Project project = convertToEntity(projectDTO);
        Project updatedProject = projectService.updateProject(project);

        return convertToDTO(updatedProject);
    }

    // DELETE: Șterge un proiect după ID
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteProject(@PathVariable int id) {
        logger.info("Deleting project with ID: " + id);
        projectService.deleteProject(id);
        return "Project with ID " + id + " has been deleted.";
    }

    // PUT: Arhivează un proiect
    @PutMapping("/{id}/archive")
    @ResponseBody
    public String archiveProject(@PathVariable int id) {
        logger.info("Archiving project with ID: " + id);
        projectService.archiveProject(id);
        return "Project with ID " + id + " has been archived.";
    }

    // GET: Găsește proiecte după status
    @GetMapping("/status/{status}")
    @ResponseBody
    public List<ProjectDTO> findProjectsByStatus(@PathVariable Project.StatusProiect status) {
        logger.info("Fetching projects with status: " + status);
        return projectService.findProjectsByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // GET: Găsește proiecte după client
    @GetMapping("/client/{clientName}")
    @ResponseBody
    public List<ProjectDTO> findProjectsByClient(@PathVariable String clientName) {
        logger.info("Fetching projects for client: " + clientName);
        return projectService.findProjectsByClient(clientName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Metode auxiliare pentru conversia între DTO și Entitate
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(project.getProjectId());
        dto.setName(project.getName());
        dto.setClientId(project.getClient() != null ? project.getClient().getClientId() : null);
        dto.setTeamId(project.getTeam() != null ? project.getTeam().getTeamId() : null);
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus().name());
        dto.setProgress(project.getProgress());
        return dto;
    }

    private Project convertToEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setProjectId(dto.getProjectId());
        project.setName(dto.getName());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setProgress(dto.getProgress());
        project.setStatus(Project.StatusProiect.valueOf(dto.getStatus()));

        // Populează relațiile
        if (dto.getClientId() != null) {
            project.setClient(new Client());
            project.getClient().setClientId(dto.getClientId());
        }
        if (dto.getTeamId() != null) {
            project.setTeam(new AuditTeam());
            project.getTeam().setTeamId(dto.getTeamId());
        }

        return project;
    }
}
