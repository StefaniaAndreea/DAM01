package org.audit.views.projects;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.audit.services.IAuditReportService;
import org.audit.services.IClientService;
import org.audit.services.IAuditTeamService;
import org.audit.services.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.audit.views.clients.ClientDetailsView;
import org.audit.views.teams.TeamDetailsView;
import org.audit.views.reports.ReportsView;

@Component
@Scope("prototype")
@Route("projects/dashboard")
@PageTitle("Project Dashboard")
public class ProjectDashboardView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final IClientService clientService;
    private final IAuditTeamService auditTeamService;
    private final IEmployeeService employeeService;
    private final IAuditReportService auditReportService;

    @Autowired
    public ProjectDashboardView(IClientService clientService, IAuditTeamService auditTeamService,
                                IEmployeeService employeeService, IAuditReportService auditReportService) {
        this.clientService = clientService;
        this.auditTeamService = auditTeamService;
        this.employeeService = employeeService;
        this.auditReportService = auditReportService;

        setWidthFull();
        setSpacing(false);
    }

    @Override
    public void setParameter(BeforeEvent event, Integer projectId) {
        // Placeholder IDs (to be replaced with actual logic for retrieving clientId and teamId for the project)
        Integer clientId = 1; // Replace with actual logic
        Integer teamId = 1;   // Replace with actual logic

        removeAll(); // Clear the layout
        add(new H1("Project Dashboard"));

        createTopSection(clientId, teamId);
        createBottomSection(projectId);
    }

    private void createTopSection(Integer clientId, Integer teamId) {
        // Secțiunea Client
        ClientDetailsView clientDetailsView = new ClientDetailsView(clientService);
        clientDetailsView.setParameter(null, clientId);

        // Secțiunea Team
        TeamDetailsView teamDetailsView = new TeamDetailsView(auditTeamService, employeeService);
        teamDetailsView.setParameter(null, teamId);

        // Configurăm dimensiunile pentru secțiuni
        clientDetailsView.setWidth("25%");
        teamDetailsView.setWidth("75%");

        // Layout-ul pe rând
        HorizontalLayout topSection = new HorizontalLayout(clientDetailsView, teamDetailsView);
        topSection.setWidthFull();
        topSection.setSpacing(true);
        topSection.getStyle().set("padding-left", "20px").set("padding-right", "20px");

        add(topSection);
    }

    private void createBottomSection(Integer projectId) {
        // Secțiunea Reports
        ReportsView reportsView = new ReportsView(auditReportService);
        reportsView.setParameter(null, projectId);

        // Wrapper pentru layout
        VerticalLayout bottomSection = new VerticalLayout(reportsView);
        bottomSection.setWidthFull();
        bottomSection.getStyle().set("padding-left", "20px").set("padding-right", "20px");

        add(bottomSection);
    }
}
