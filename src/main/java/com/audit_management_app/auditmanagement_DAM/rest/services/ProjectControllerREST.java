package com.audit_management_app.auditmanagement_DAM.rest.services;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.dto.ProjectDTO;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectControllerREST {

    private static final Logger logger = Logger.getLogger(ProjectControllerREST.class.getName());

    @Autowired
    private IProjectRepository projectRepository;

    // GET: Obține toate proiectele
    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        logger.info("Fetching all projects");
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // GET: Obține un proiect după ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Integer id) {
        Optional<Project> projectOpt = projectRepository.findById(id);
        return projectOpt.map(project -> ResponseEntity.ok(convertToDTO(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Adaugă un nou proiect
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        Project savedProject = projectRepository.save(project);
        return ResponseEntity.ok(convertToDTO(savedProject));
    }

    // PUT: Actualizează un proiect existent
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Integer id, @RequestBody ProjectDTO projectDTO) {
        Optional<Project> projectOpt = projectRepository.findById(id);
        if (projectOpt.isPresent()) {
            Project existingProject = projectOpt.get();
            existingProject.setName(projectDTO.getName());
            existingProject.setStartDate(projectDTO.getStartDate());
            existingProject.setEndDate(projectDTO.getEndDate());
            existingProject.setProgress(projectDTO.getProgress());
            existingProject.setStatus(Project.StatusProiect.valueOf(projectDTO.getStatus()));
            Project updatedProject = projectRepository.save(existingProject);
            return ResponseEntity.ok(convertToDTO(updatedProject));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Șterge un proiect după ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Metode auxiliare pentru conversie între DTO și Entitate
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(project.getProjectId());
        dto.setName(project.getName());
        dto.setClientId(project.getClient() != null ? project.getClient().getClientId() : null);
        dto.setTeamId(project.getTeam() != null ? project.getTeam().getTeamId() : null);
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setProgress(project.getProgress());
        dto.setStatus(project.getStatus().name());
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
        // Populăm Client și Team dacă este necesar
        if (dto.getClientId() != null) {
            Client client = new Client();
            client.setClientId(dto.getClientId());
            project.setClient(client);
        }
        if (dto.getTeamId() != null) {
            AuditTeam team = new AuditTeam();
            team.setTeamId(dto.getTeamId());
            project.setTeam(team);
        }
        return project;
    }
}
