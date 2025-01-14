package org.audit.views.projects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.audit.dto.ProjectDTO;
import org.audit.services.ProjectService;
import org.audit.views.layout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@PageTitle("Projects")
@Route(value = "projects", layout = MainLayout.class)
public class NavigableGridProjectsView extends VerticalLayout {

    private final ProjectService projectService;
    private final Grid<ProjectDTO> grid = new Grid<>(ProjectDTO.class);
    private final TextField filterText = new TextField();

    private final Button addProjectButton = new Button("Add Project");
    private final Button editProjectButton = new Button("Edit Project");
    private final Button deleteProjectButton = new Button("Delete Project");
    private final Button archiveProjectButton = new Button("Archive Project");

    @Autowired
    public NavigableGridProjectsView(ProjectService projectService) {
        this.projectService = projectService;
        initializeView();
        updateGrid();
    }

    private void initializeView() {
        H1 title = new H1("Projects");

        filterText.setPlaceholder("Filter by name...");
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateGrid());

        // Configure Grid to display relevant fields
        grid.setColumns();
        grid.addColumn(ProjectDTO::getProjectId).setHeader("Project ID");
        grid.addColumn(ProjectDTO::getName).setHeader("Name");
        grid.addColumn(ProjectDTO::getClientId).setHeader("Client ID");
        grid.addColumn(ProjectDTO::getTeamId).setHeader("Team ID");
        grid.addColumn(project -> formatDate(project.getStartDate())).setHeader("Start Date");
        grid.addColumn(project -> formatDate(project.getEndDate())).setHeader("End Date");
        grid.addColumn(ProjectDTO::getStatus).setHeader("Status");
        grid.addColumn(project -> project.getProgress() + "%").setHeader("Progress");
        grid.addComponentColumn(project -> {
            Button viewDashboardButton = new Button("View Dashboard", click -> {
                getUI().ifPresent(ui -> ui.navigate("projects/dashboard/" + project.getProjectId()));
            });
            return viewDashboardButton;
        }).setHeader("Actions");

        // Add click listeners for buttons
        addProjectButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("project-form")));
        editProjectButton.addClickListener(e -> editSelectedProject());
        deleteProjectButton.addClickListener(e -> deleteSelectedProject());
        archiveProjectButton.addClickListener(e -> archiveSelectedProject());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProjectButton, editProjectButton, archiveProjectButton, deleteProjectButton);
        add(title, toolbar, grid);
    }

    private void updateGrid() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        String filter = filterText.getValue();
        if (filter != null && !filter.isEmpty()) {
            projects = projects.stream()
                    .filter(p -> p.getName().toLowerCase().contains(filter.toLowerCase()))
                    .toList();
        }
        grid.setItems(projects);
    }

    private void editSelectedProject() {
        ProjectDTO selected = grid.asSingleSelect().getValue();
        if (selected != null) {
            getUI().ifPresent(ui -> ui.navigate("project-form/" + selected.getProjectId()));
        } else {
            Notification.show("Please select a project to edit.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void deleteSelectedProject() {
        ProjectDTO selected = grid.asSingleSelect().getValue();
        if (selected != null) {
            try {
                projectService.deleteProject(selected.getProjectId());
                updateGrid();
                Notification.show("Project deleted.", 3000, Notification.Position.TOP_CENTER);
            } catch (Exception ex) {
                Notification.show("Failed to delete project: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        } else {
            Notification.show("Please select a project to delete.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void archiveSelectedProject() {
        ProjectDTO selected = grid.asSingleSelect().getValue();
        if (selected != null) {
            try {
                projectService.archiveProject(selected.getProjectId());
                updateGrid();
                Notification.show("Project archived successfully.", 3000, Notification.Position.TOP_CENTER);
            } catch (Exception ex) {
                Notification.show("Failed to archive project: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        } else {
            Notification.show("Please select a project to archive.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
