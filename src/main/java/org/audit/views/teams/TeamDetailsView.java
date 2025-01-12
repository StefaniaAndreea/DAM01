package org.audit.views.teams;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.audit.dto.AuditTeamDTO;
import org.audit.dto.EmployeeDTO;
import org.audit.services.IAuditTeamService;
import org.audit.services.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
@Route("teams/details")
@PageTitle("Team Details")
public class TeamDetailsView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final IAuditTeamService auditTeamService;
    private final IEmployeeService employeeService;

    private AuditTeamDTO team;

    private final H1 title = new H1("Team Details");
    private final TextField teamNameField = new TextField("Team Name");
    private final Grid<EmployeeDTO> memberGrid = new Grid<>(EmployeeDTO.class);

    @Autowired
    public TeamDetailsView(IAuditTeamService auditTeamService, IEmployeeService employeeService) {
        this.auditTeamService = auditTeamService;
        this.employeeService = employeeService;
    }

    @Override
    public void setParameter(BeforeEvent event, Integer teamId) {
        team = auditTeamService.getTeamById(teamId);
        List<EmployeeDTO> teamMembers = employeeService.getEmployeesByTeamId(teamId);
        initializeView(team, teamMembers);
    }

    private void initializeView(AuditTeamDTO team, List<EmployeeDTO> teamMembers) {
        removeAll(); // Curățăm layout-ul

        if (team != null) {
            // Setăm detaliile echipei
            teamNameField.setValue(team.getTeamName());
            teamNameField.setReadOnly(true);

            // Configurăm Grid-ul pentru membrii echipei
            memberGrid.setItems(teamMembers);
            memberGrid.setColumns("employeeId", "name", "role", "salary");
            memberGrid.addColumn(employee -> employee.isAvailable() ? "Yes" : "No").setHeader("Available");

            // Adăugăm o coloană pentru acțiuni
            memberGrid.addColumn(new ComponentRenderer<>(employee -> {
                Button toggleButton = new Button(employee.isAvailable() ? "Set as Unavailable" : "Set as Available");
                toggleButton.addClickListener(e -> toggleEmployeeAvailability(employee));
                return toggleButton;
            })).setHeader("Actions").setAutoWidth(true).setFlexGrow(0);

            // Configurăm dimensiunea grilei
            memberGrid.setWidthFull();
            memberGrid.setHeight("300px");
            memberGrid.getStyle().set("overflow-x", "auto");

            // Adăugăm grila într-un container cu scroll
            VerticalLayout gridContainer = new VerticalLayout(memberGrid);
            gridContainer.setWidthFull();
            gridContainer.setPadding(false);

            add(title, teamNameField, gridContainer);
        } else {
            add(new H1("Team not found."));
        }
    }

    private void toggleEmployeeAvailability(EmployeeDTO employee) {
        try {
            // Inversăm disponibilitatea angajatului
            boolean newAvailability = !employee.isAvailable();
            employeeService.updateEmployeeAvailability(employee.getEmployeeId(), newAvailability);

            // Afișăm o notificare și actualizăm grila
            Notification.show("Employee availability updated successfully!", 3000, Notification.Position.TOP_CENTER);
            refreshTeamMembers();
        } catch (Exception e) {
            Notification.show("Error updating availability: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    private void refreshTeamMembers() {
        if (team != null) {
            List<EmployeeDTO> updatedTeamMembers = employeeService.getEmployeesByTeamId(team.getTeamId());
            memberGrid.setItems(updatedTeamMembers);
        }
    }
}
