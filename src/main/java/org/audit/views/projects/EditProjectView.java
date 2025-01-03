//package org.audit.views.projects;
//
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.combobox.ComboBox;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.IntegerField;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.*;
//import org.audit.dto.ProjectDTO;
//import org.audit.dto.StatusProiect;
//import org.audit.services.DateUtilAPI;
//import org.audit.services.ProjectService;
//
//import java.util.Optional;
//
//@Route("projects/edit")
//public class EditProjectView extends VerticalLayout implements HasUrlParameter<String> {
//
//    private final ProjectService projectService;
//    private ProjectDTO currentProject;
//
//    // Declarare câmpuri pentru proiect
//    private final TextField nameField = new TextField("Name");
//    private final IntegerField clientIdField = new IntegerField("Client ID");
//    private final IntegerField teamIdField = new IntegerField("Team ID");
//    private final DatePicker startDateField = new DatePicker("Start Date");
//    private final DatePicker endDateField = new DatePicker("End Date");
//    private final ComboBox<StatusProiect> statusField = new ComboBox<>("Status");
//    private final TextField progressField = new TextField("Progress (%)");
//
//    public EditProjectView(ProjectService projectService) {
//        this.projectService = projectService;
//
//        // Form pentru editarea proiectului
//        FormLayout formLayout = new FormLayout();
//        formLayout.add(nameField, clientIdField, teamIdField, startDateField, endDateField, statusField, progressField);
//
//        // Configurare ComboBox pentru Status
//        statusField.setItems(StatusProiect.values()); // Adăugare valori din enum
//        statusField.setItemLabelGenerator(StatusProiect::name); // Etichete pentru opțiuni
//
//        Button saveButton = new Button("Save", click -> saveProject());
//        Button cancelButton = new Button("Cancel", click -> getUI().ifPresent(ui -> ui.navigate(ProjectsView.class)));
//
//        add(formLayout, saveButton, cancelButton);
//    }
//
//    @Override
//    public void setParameter(BeforeEvent event, String id) {
//        try {
//            Integer projectId = Integer.parseInt(id);
//            this.currentProject = projectService.getProjectById(projectId);
//
//            if (currentProject != null) {
//                nameField.setValue(Optional.ofNullable(currentProject.getName()).orElse(""));
//                clientIdField.setValue(Optional.ofNullable(currentProject.getClientId()).orElse(0));
//                teamIdField.setValue(Optional.ofNullable(currentProject.getTeamId()).orElse(0));
//                startDateField.setValue(Optional.ofNullable(currentProject.getStartDate()).map(DateUtilAPI::asLocalDate).orElse(null));
//                endDateField.setValue(Optional.ofNullable(currentProject.getEndDate()).map(DateUtilAPI::asLocalDate).orElse(null));
//                statusField.setValue(currentProject.getStatus());
//                progressField.setValue(String.valueOf(currentProject.getProgress()));
//            }
//        } catch (NumberFormatException e) {
//            Notification.show("Invalid project ID", 3000, Notification.Position.MIDDLE);
//        } catch (Exception e) {
//            Notification.show("Failed to load project: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
//        }
//    }
//
//    private void saveProject() {
//        try {
//            currentProject.setName(nameField.getValue());
//            currentProject.setClientId(clientIdField.getValue());
//            currentProject.setTeamId(teamIdField.getValue());
//            currentProject.setStartDate(DateUtilAPI.asDate(startDateField.getValue()));
//            currentProject.setEndDate(DateUtilAPI.asDate(endDateField.getValue()));
//            currentProject.setStatus(statusField.getValue());
//            currentProject.setProgress(Float.parseFloat(progressField.getValue()));
//
//            projectService.updateProject(currentProject);
//
//            Notification.show("Project updated successfully!", 3000, Notification.Position.MIDDLE);
//            getUI().ifPresent(ui -> ui.navigate(ProjectsView.class));
//        } catch (Exception e) {
//            Notification.show("Failed to update project: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
//        }
//    }
//}
