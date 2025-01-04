package org.audit.views.projects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.audit.dto.AuditTeamDTO;
import org.audit.dto.ClientDTO;
import org.audit.dto.ProjectDTO;
import org.audit.dto.StatusProiect;
import org.audit.services.DateUtilAPI;
import org.audit.services.IAuditTeamService;
import org.audit.services.IClientService;
import org.audit.services.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

@PageTitle("Project Form")
@Route(value = "project-form")
public class FormProjectView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final IProjectService projectService;
    private final IClientService clientService;
    private final IAuditTeamService auditTeamService;
    private final Binder<ProjectDTO> binder = new BeanValidationBinder<>(ProjectDTO.class);

    private ProjectDTO project;

    private final H1 title = new H1("Project Form");

    // Fields for ProjectDTO properties
    private final TextField nameField = new TextField("Project Name");
    private final ComboBox<StatusProiect> statusField = new ComboBox<>("Status"); // Dropdown pentru Status
    private final ComboBox<ClientDTO> clientIdField = new ComboBox<>("Client ID"); // Dropdown pentru clienți
    private final TextField clientNameField = new TextField("Client Name");
    private final ComboBox<AuditTeamDTO> teamIdField = new ComboBox<>("Team");
    private final TextField teamNameField = new TextField("Team Name");
    private final DatePicker startDateField = new DatePicker("Start Date");
    private final DatePicker endDateField = new DatePicker("End Date");
    private final TextField progressField = new TextField("Progress (%)");

    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    @Autowired
    public FormProjectView(IProjectService projectService, IClientService clientService, IAuditTeamService auditTeamService) {
        this.projectService = projectService;
        this.clientService = clientService;
        this.auditTeamService = auditTeamService;
        initializeView();
    }
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null) {
            this.project = projectService.getProjectById(id);
        } else {
            this.project = new ProjectDTO();
            this.project.setClientId(null); // Inițializează cu valori implicite
        }
        binder.setBean(this.project);
    }

    private void initializeView() {
        // Configurare ComboBox pentru clienți
        clientIdField.setItems(clientService.getAllClients()); // Populează lista cu clienți
        clientIdField.setItemLabelGenerator(client -> String.valueOf(client.getClientId())); // Afișează doar ID-ul
        clientIdField.setAllowCustomValue(false); // Nu permite valori personalizate
        clientIdField.setPlaceholder("Select a client"); // Placeholder pentru UI
        clientIdField.setClearButtonVisible(true); // Permite ștergerea selecției
        clientIdField.addValueChangeListener(event -> {
            ClientDTO selectedClient = event.getValue();
            if (selectedClient != null) {
                project.setClientId(selectedClient.getClientId());
                clientNameField.setValue(selectedClient.getName());
            } else {
                project.setClientId(null);
                clientNameField.clear();
            }
        });
        teamIdField.setItems(auditTeamService.getAllTeams()); // Populează ComboBox-ul cu echipe
        teamIdField.setItemLabelGenerator(team -> String.valueOf(team.getTeamId())); // Afișează doar ID-ul echipei
        teamIdField.setPlaceholder("Select a team");
        teamIdField.setClearButtonVisible(true); // Permite ștergerea selecției
        teamIdField.addValueChangeListener(event -> {
            AuditTeamDTO selectedTeam = event.getValue();
            if (selectedTeam != null) {
                project.setTeamId(selectedTeam.getTeamId());
                teamNameField.setValue(selectedTeam.getTeamName());
            } else {
                project.setTeamId(null);
                teamNameField.clear();
            }
        });

        // Configurare ComboBox pentru Status
        statusField.setItems(StatusProiect.values()); // Populează dropdown-ul cu valorile din enum
        statusField.setItemLabelGenerator(StatusProiect::name); // Afișează numele valorilor enum

        // Binder for fields
        binder.forField(nameField).bind(ProjectDTO::getName, ProjectDTO::setName);
        binder.forField(clientIdField)
                .withNullRepresentation(null) // Permite valori null
                .bind(
                        project -> clientService.getAllClients().stream()
                                .filter(client -> client.getClientId().equals(project.getClientId()))
                                .findFirst()
                                .orElse(null), // Găsește clientul corespunzător
                        (project, client) -> {
                            if (client != null) {
                                project.setClientId(client.getClientId());
                            } else {
                                project.setClientId(null);
                            }
                        }
                );
        binder.forField(clientNameField).bind(ProjectDTO::getClientName, null);
        binder.forField(teamIdField)
                .withNullRepresentation(null)
                .bind(
                        project -> auditTeamService.getAllTeams().stream()
                                .filter(team -> team.getTeamId().equals(project.getTeamId()))
                                .findFirst()
                                .orElse(null),
                        (project, team) -> {
                            if (team != null) {
                                project.setTeamId(team.getTeamId());
                                project.setTeamName(team.getTeamName());
                            } else {
                                project.setTeamId(null);
                                project.setTeamName(null);
                            }
                        }
                );

        binder.forField(teamNameField).bind(ProjectDTO::getTeamName, null);
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
                nameField, statusField, clientIdField, clientNameField, teamIdField, teamNameField,
                startDateField, endDateField, progressField
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
