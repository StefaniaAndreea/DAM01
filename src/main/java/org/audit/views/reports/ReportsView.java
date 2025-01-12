package org.audit.views.reports;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    @Autowired
    public ReportsView(IAuditReportService auditReportService) {
        this.auditReportService = auditReportService;
    }

    @Override
    public void setParameter(BeforeEvent event, Integer projectId) {
        List<AuditReportDTO> reports = auditReportService.getReportsByProjectId(projectId);
        initializeView(reports);
    }

    private void initializeView(List<AuditReportDTO> reports) {
        removeAll(); // Curățăm layout-ul

        if (!reports.isEmpty()) {
            add(new H1("Reports"));
            Grid<AuditReportDTO> grid = new Grid<>(AuditReportDTO.class);
            grid.setItems(reports);
            grid.setColumns("reportId", "filePath", "authorId");

            // Formatarea datei în format zz/ll/aaaa
            grid.addColumn(report -> DATE_FORMAT.format(report.getSubmissionDate()))
                    .setHeader("Submission Date");

            add(grid);
        } else {
            add(new H1("No reports found for this project."));
        }
    }

}
