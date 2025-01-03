package org.audit.views.projects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.audit.dto.ProjectDTO;
import org.audit.services.ProjectService;

import java.util.List;

@Route("projects/list")
public class ProjectsView extends VerticalLayout {

    private final ProjectService projectService;
    private final Grid<ProjectDTO> projectGrid = new Grid<>(ProjectDTO.class);

    public ProjectsView(ProjectService projectService) {
        this.projectService = projectService;

        setSizeFull(); // Setează layout-ul să ocupe tot ecranul
        configureGrid(); // Configurează Grid-ul pentru afișarea proiectelor

        add(projectGrid); // Adaugă Grid-ul în layout
        updateGrid(); // Populează Grid-ul cu datele din backend
    }

    private void configureGrid() {
        projectGrid.setSizeFull();
        projectGrid.setColumns("projectId", "name", "status", "progress");
        projectGrid.addComponentColumn(project -> {
            Button editButton = new Button("Edit", click -> {
                Notification.show("Edit button clicked for project ID: " + project.getProjectId(), 3000, Notification.Position.MIDDLE);
            });
            return editButton;
        }).setHeader("Actions");
    }


    private void updateGrid() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        projectGrid.setItems(projects);
    }

    private void editProject(ProjectDTO project) {
        System.out.println("editProject method called");
        if (project.getProjectId() == null) {
            Notification.show("Project ID is null. Cannot navigate.", 3000, Notification.Position.MIDDLE);
            return;
        }
        System.out.println("Navigating to edit project with ID: " + project.getProjectId());
        RouteParameters parameters = new RouteParameters("id", project.getProjectId().toString());
        getUI().ifPresent(ui -> ui.navigate(EditProjectView.class, parameters));
    }
}