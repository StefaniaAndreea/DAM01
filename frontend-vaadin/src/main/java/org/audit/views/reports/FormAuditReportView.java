package org.audit.views.reports;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.audit.dto.AuditReportDTO;
import org.audit.dto.EmployeeDTO;
import org.audit.services.AuditReportService;
import org.audit.services.EmployeeService;
import org.audit.services.ProjectService;

import java.time.LocalDate;
import java.util.List;

@PageTitle("Audit Report Form")
@Route("audit-report-form")
public class FormAuditReportView extends VerticalLayout implements HasUrlParameter<String> {

    private final AuditReportService auditReportService;
    private final ProjectService projectService;
    private final EmployeeService employeeService;

    private final TextField projectIdField = new TextField("Project ID");
    private final TextField filePathField = new TextField("File Path");
    private final DatePicker submissionDatePicker = new DatePicker("Submission Date");
    private final ComboBox<EmployeeDTO> authorComboBox = new ComboBox<>("Author");

    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    private Integer projectId;

    public FormAuditReportView(AuditReportService auditReportService, ProjectService projectService, EmployeeService employeeService) {
        this.auditReportService = auditReportService;
        this.projectService = projectService;
        this.employeeService = employeeService;

        setPadding(true);
        setSpacing(true);

        add(new H1("Audit Report Form"));

        configureFields();
        configureButtons();
        buildFormLayout();
    }

    private void configureFields() {
        projectIdField.setReadOnly(true);

        submissionDatePicker.setValue(LocalDate.now());
        authorComboBox.setPlaceholder("Select an author");
        authorComboBox.setItemLabelGenerator(EmployeeDTO::getName);
    }

    private void configureButtons() {
        saveButton.addClickListener(e -> saveAuditReport());
        cancelButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("reports/details/" + projectId)));
    }

    private void buildFormLayout() {
        HorizontalLayout firstRow = new HorizontalLayout(projectIdField, filePathField);
        HorizontalLayout secondRow = new HorizontalLayout(submissionDatePicker, authorComboBox);
        HorizontalLayout buttonRow = new HorizontalLayout(saveButton, cancelButton);

        add(firstRow, secondRow, buttonRow);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try {
            projectId = Integer.parseInt(parameter);
            projectIdField.setValue(String.valueOf(projectId));

            populateAuthorComboBox();
        } catch (NumberFormatException e) {
            Notification.show("Invalid project ID.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void populateAuthorComboBox() {
        try {
            var project = projectService.getProjectById(projectId);
            if (project != null && project.getTeamId() != null) {
                List<EmployeeDTO> teamMembers = employeeService.getEmployeesByTeamId(project.getTeamId());
                authorComboBox.setItems(teamMembers);
            } else {
                Notification.show("No team assigned to this project.", 3000, Notification.Position.TOP_CENTER);
            }
        } catch (Exception e) {
            Notification.show("Error fetching team members: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void saveAuditReport() {
        try {
            AuditReportDTO newReport = new AuditReportDTO();
            newReport.setProjectId(projectId);
            newReport.setFilePath(filePathField.getValue());
            newReport.setSubmissionDate(java.sql.Date.valueOf(submissionDatePicker.getValue()));

            EmployeeDTO selectedAuthor = authorComboBox.getValue();
            if (selectedAuthor == null) {
                Notification.show("Please select an author.", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            newReport.setAuthorId(selectedAuthor.getEmployeeId());

            auditReportService.addReport(newReport);
            Notification.show("Audit Report added successfully!", 3000, Notification.Position.TOP_CENTER);

            getUI().ifPresent(ui -> ui.navigate("reports/details/" + projectId));
        } catch (Exception e) {
            Notification.show("Error saving audit report: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }
}
