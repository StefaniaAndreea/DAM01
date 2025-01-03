package org.audit.views.projects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.audit.dto.ProjectDTO;
import org.audit.services.ProjectService;

import java.util.Optional;

@Route("projects/edit")
public class EditProjectView extends VerticalLayout implements HasUrlParameter<String> {

    private final ProjectService projectService;
    private ProjectDTO currentProject;

    private TextField nameField = new TextField("Name");
    private TextField statusField = new TextField("Status");
    private TextField progressField = new TextField("Progress");

    public EditProjectView(ProjectService projectService) {
        this.projectService = projectService;

        // Form pentru editarea proiectului
        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, statusField, progressField);

        Button saveButton = new Button("Save", click -> saveProject());
        Button cancelButton = new Button("Cancel", click -> getUI().ifPresent(ui -> ui.navigate(ProjectsView.class)));

        add(formLayout, saveButton, cancelButton);
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        try {
            Integer projectId = Integer.parseInt(id);
            this.currentProject = projectService.getProjectById(projectId);

            if (currentProject != null) {
                nameField.setValue(Optional.ofNullable(currentProject.getName()).orElse(""));
                statusField.setValue(Optional.ofNullable(currentProject.getStatus()).orElse(""));
                progressField.setValue(String.valueOf(currentProject.getProgress()));
            }
        } catch (NumberFormatException e) {
            Notification.show("Invalid project ID", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Failed to load project: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void saveProject() {
        try {
            currentProject.setName(nameField.getValue());
            currentProject.setStatus(statusField.getValue());
            currentProject.setProgress(Float.parseFloat(progressField.getValue()));

            projectService.updateProject(currentProject);

            Notification.show("Project updated successfully!", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(ProjectsView.class));
        } catch (Exception e) {
            Notification.show("Failed to update project: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }
}
