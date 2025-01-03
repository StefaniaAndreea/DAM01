package org.audit.views.projects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.audit.dto.ProjectDTO;
import org.audit.services.DateUtilAPI;
import org.audit.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

@PageTitle("Project Form")
@Route(value = "project-form")
public class FormProjectView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final ProjectService projectService;
    private final Binder<ProjectDTO> binder = new BeanValidationBinder<>(ProjectDTO.class);

    private ProjectDTO project;

    private final H1 title = new H1("Project Form");

    // Fields for ProjectDTO properties
    private final TextField nameField = new TextField("Project Name");
    private final IntegerField clientIdField = new IntegerField("Client ID");
    private final TextField clientNameField = new TextField("Client Name");
    private final IntegerField teamIdField = new IntegerField("Team ID");
    private final TextField teamNameField = new TextField("Team Name");
    private final DatePicker startDateField = new DatePicker("Start Date");
    private final DatePicker endDateField = new DatePicker("End Date");
    private final TextField statusField = new TextField("Status");
    private final TextField progressField = new TextField("Progress (%)");

    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    @Autowired
    public FormProjectView(ProjectService projectService) {
        this.projectService = projectService;
        initializeView();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null) {
            this.project = projectService.getProjectById(id);
        } else {
            this.project = new ProjectDTO();
        }
        binder.setBean(this.project);
    }

    private void initializeView() {
        // Binder for fields
        binder.forField(nameField).bind(ProjectDTO::getName, ProjectDTO::setName);
        binder.forField(clientIdField).bind(ProjectDTO::getClientId, ProjectDTO::setClientId);
        binder.forField(clientNameField).bind(ProjectDTO::getClientName, ProjectDTO::setClientName);
        binder.forField(teamIdField).bind(ProjectDTO::getTeamId, ProjectDTO::setTeamId);
        binder.forField(teamNameField).bind(ProjectDTO::getTeamName, ProjectDTO::setTeamName);
        binder.forField(startDateField)
                .withConverter(
                        localDate -> localDate == null ? null : DateUtilAPI.asDate(localDate),
                        date -> date == null ? null : DateUtilAPI.asLocalDate(date)
                )
                .bind(ProjectDTO::getStartDate, ProjectDTO::setStartDate);
        binder.forField(endDateField)
                .withConverter(
                        localDate -> localDate == null ? null : DateUtilAPI.asDate(localDate),
                        date -> date == null ? null : DateUtilAPI.asLocalDate(date)
                )
                .bind(ProjectDTO::getEndDate, ProjectDTO::setEndDate);
        binder.forField(statusField).bind(ProjectDTO::getStatus, ProjectDTO::setStatus);
        binder.forField(progressField)
                .withConverter(
                        text -> text == null || text.isEmpty() ? 0f : Float.parseFloat(text),
                        floatValue -> floatValue == null ? "" : floatValue.toString()
                )
                .bind(ProjectDTO::getProgress, ProjectDTO::setProgress);

        // Form layout
        FormLayout formLayout = new FormLayout(
                nameField, clientIdField, clientNameField, teamIdField, teamNameField,
                startDateField, endDateField, statusField, progressField
        );

        // Buttons layout
        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);

        saveButton.addClickListener(e -> saveProject());
        cancelButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(NavigableGridProjectsView.class)));

        // Add components to layout
        add(title, formLayout, actions);
    }

    private void saveProject() {
        try {
            if (project.getProjectId() != null) {
                projectService.updateProject(project);
            } else {
                projectService.addProject(project);
            }
            Notification.show("Project saved successfully!", 3000, Notification.Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate(NavigableGridProjectsView.class));
        } catch (Exception ex) {
            Notification.show("Error saving project: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }
}