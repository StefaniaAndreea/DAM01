package org.audit.views.projects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.audit.dto.ProjectDTO;
import org.audit.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@PageTitle("Projects")
@Route(value = "projects")
public class NavigableGridProjectsView extends VerticalLayout {

    private final ProjectService projectService;
    private final Grid<ProjectDTO> grid = new Grid<>(ProjectDTO.class);
    private final TextField filterText = new TextField();

    private final Button addProjectButton = new Button("Add Project");
    private final Button editProjectButton = new Button("Edit Project");
    private final Button deleteProjectButton = new Button("Delete Project");

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

        addProjectButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(FormProjectView.class)));
        editProjectButton.addClickListener(e -> editSelectedProject());
        deleteProjectButton.addClickListener(e -> deleteSelectedProject());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProjectButton, editProjectButton, deleteProjectButton);
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
            getUI().ifPresent(ui -> ui.navigate(FormProjectView.class, selected.getProjectId()));
        } else {
            Notification.show("Please select a project to edit.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void deleteSelectedProject() {
        ProjectDTO selected = grid.asSingleSelect().getValue();
        if (selected != null) {
            projectService.deleteProject(selected.getProjectId());
            updateGrid();
            Notification.show("Project deleted.", 3000, Notification.Position.TOP_CENTER);
        } else {
            Notification.show("Please select a project to delete.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}