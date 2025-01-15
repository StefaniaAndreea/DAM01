package org.audit.views.projects;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.audit.dto.ProjectDTO;
import org.audit.services.*;
import org.audit.views.layout.MainLayout;
import org.audit.views.tasks.ProjectTasksView;
import org.audit.views.clients.ClientDetailsView;
import org.audit.views.teams.TeamDetailsView;
import org.audit.views.reports.ReportsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Route(value = "projects/dashboard", layout = MainLayout.class)
@PageTitle("Project Dashboard")
public class ProjectDashboardView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final IClientService clientService;
    private final IAuditTeamService auditTeamService;
    private final IEmployeeService employeeService;
    private final IAuditReportService auditReportService;
    private final IProjectService projectService;
    private final ITaskService taskService;

    @Autowired
    public ProjectDashboardView(IClientService clientService, IAuditTeamService auditTeamService,
                                IEmployeeService employeeService, IAuditReportService auditReportService,
                                IProjectService projectService, ITaskService taskService) {
        this.clientService = clientService;
        this.auditTeamService = auditTeamService;
        this.employeeService = employeeService;
        this.auditReportService = auditReportService;
        this.projectService = projectService;
        this.taskService = taskService;

        setWidthFull();
        setSpacing(false);
    }

    @Override
    public void setParameter(BeforeEvent event, Integer projectId) {
        ProjectDTO project = projectService.getProjectById(projectId);

        if (project == null) {
            removeAll();
            add(new H1("Project not found."));
            return;
        }

        Integer clientId = project.getClientId();
        Integer teamId = project.getTeamId();

        removeAll();
        add(new H1("Project Dashboard"));

        createTopSection(clientId, teamId);
        createTasksSection(projectId);
        createBottomSection(projectId);
    }

    private void createTopSection(Integer clientId, Integer teamId) {
        ClientDetailsView clientDetailsView = new ClientDetailsView(clientService);
        clientDetailsView.setParameter(null, clientId);

        TeamDetailsView teamDetailsView = new TeamDetailsView(auditTeamService, employeeService);
        teamDetailsView.setParameter(null, teamId);

        clientDetailsView.setWidth("25%");
        teamDetailsView.setWidth("75%");

        HorizontalLayout topSection = new HorizontalLayout(clientDetailsView, teamDetailsView);
        topSection.setWidthFull();
        topSection.setSpacing(true);
        topSection.getStyle().set("padding-left", "20px").set("padding-right", "20px");

        add(topSection);
    }

    private void createTasksSection(Integer projectId) {
        ProjectTasksView tasksView = new ProjectTasksView(taskService, employeeService, projectId);

        tasksView.setWidthFull();
        tasksView.getStyle().set("padding-left", "20px").set("padding-right", "20px");

        add(tasksView);
    }

    private void createBottomSection(Integer projectId) {
        ReportsView reportsView = new ReportsView(auditReportService);
        reportsView.setParameter(null, projectId);

        VerticalLayout bottomSection = new VerticalLayout(reportsView);
        bottomSection.setWidthFull();
        bottomSection.getStyle().set("padding-left", "20px").set("padding-right", "20px");

        add(bottomSection);
    }
}
