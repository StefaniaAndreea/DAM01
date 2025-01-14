package org.audit.views.reports;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.audit.dto.AuditReportDTO;
import org.audit.services.IAuditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Scope("prototype")
@Route("reports/details")
@PageTitle("Reports")
public class ReportsView extends VerticalLayout implements HasUrlParameter<Integer> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private final IAuditReportService auditReportService;

    private Integer projectId; // Stocăm ID-ul proiectului pentru a-l utiliza în URL

    private final Grid<AuditReportDTO> grid = new Grid<>();
    private final Button addReportButton = new Button("Add Report");
    private final Button deleteReportButton = new Button("Delete Report");

    @Autowired
    public ReportsView(IAuditReportService auditReportService) {
        this.auditReportService = auditReportService;
        setupLayout();
    }

    @Override
    public void setParameter(BeforeEvent event, Integer projectId) {
        this.projectId = projectId; // Salvăm ID-ul proiectului
        refreshGrid();
    }

    private void setupLayout() {
        // Adăugăm titlul
        add(new H1("Reports"));

        // Configurăm toolbar-ul
        HorizontalLayout toolbar = new HorizontalLayout(addReportButton, deleteReportButton);
        toolbar.setSpacing(true);
        add(toolbar);

        // Configurăm grila
        grid.addColumn(AuditReportDTO::getReportId).setHeader("Report ID");
        grid.addColumn(AuditReportDTO::getFilePath).setHeader("File Path");
        grid.addColumn(AuditReportDTO::getAuthorId).setHeader("Author ID");
        grid.addColumn(report -> DATE_FORMAT.format(report.getSubmissionDate()))
                .setHeader("Submission Date");
        grid.addColumn(new ComponentRenderer<>(report -> {
            Button seeDetailsButton = new Button("See Details");
            seeDetailsButton.addClickListener(e -> getUI().ifPresent(ui ->
                    ui.navigate("vulnerabilities/report/" + report.getReportId() + "-" + projectId)));
            return seeDetailsButton;
        })).setHeader("Actions");

        add(grid);

        // Configurăm acțiunile butoanelor
        addReportButton.addClickListener(e -> addReport());
        deleteReportButton.addClickListener(e -> deleteSelectedReport());
    }

    private void addReport() {
        try {
            // Navigăm către formularul pentru adăugarea unui raport nou
            getUI().ifPresent(ui ->
                    ui.navigate(FormAuditReportView.class, String.valueOf(projectId))
            );
        } catch (Exception e) {
            // Gestionăm eventualele erori de navigare
            Notification.show("Failed to navigate to the Add Report form: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }
    private void deleteSelectedReport() {
        AuditReportDTO selected = grid.asSingleSelect().getValue();

        // Verificăm dacă există o selecție înainte de a face orice altceva
        if (selected == null) {
            Notification.show("Please select a report to delete.", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        // Dacă există o selecție, încercăm să ștergem raportul
        try {
            auditReportService.deleteReport(selected.getReportId()); // Utilizăm metoda `deleteReport`
            Notification.show("Report deleted successfully!", 3000, Notification.Position.TOP_CENTER);
            refreshGrid(); // Actualizăm grila
        } catch (Exception ex) {
            // Gestionăm orice eroare din backend
            Notification.show("Error deleting report: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void refreshGrid() {
        try {
            List<AuditReportDTO> reports = auditReportService.getReportsByProjectId(projectId);
            grid.setItems(reports);
        } catch (Exception e) {
            Notification.show("Failed to refresh reports: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }
}
