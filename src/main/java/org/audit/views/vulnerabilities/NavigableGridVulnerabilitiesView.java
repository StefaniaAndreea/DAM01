package org.audit.views.vulnerabilities;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import org.audit.dto.VulnerabilityDTO;
import org.audit.dto.Status;
import org.audit.services.IVulnerabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
@Route("vulnerabilities/report")
@PageTitle("Vulnerabilities")
public class NavigableGridVulnerabilitiesView extends VerticalLayout implements HasUrlParameter<String> {

    private final IVulnerabilityService vulnerabilityService;
    private final Grid<VulnerabilityDTO> grid = new Grid<>(VulnerabilityDTO.class);

    private Integer reportId;
    private Integer projectId;

    @Autowired
    public NavigableGridVulnerabilitiesView(IVulnerabilityService vulnerabilityService) {
        this.vulnerabilityService = vulnerabilityService;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        String[] ids = parameter.split("-");
        this.reportId = Integer.parseInt(ids[0]);
        this.projectId = Integer.parseInt(ids[1]);

        initializeView();
    }

    private void initializeView() {
        removeAll(); // Clear layout

        // Title
        add(new H1("Vulnerabilities - Report " + reportId + " for Project " + projectId));

        // Buttons for Add and Delete
        Button addVulnerabilityButton = new Button("Add Vulnerability");
        Button deleteVulnerabilityButton = new Button("Delete Vulnerability");

        // Add button click listener
        addVulnerabilityButton.addClickListener(e -> getUI().ifPresent(ui ->
                ui.navigate("vulnerabilities/form/" + reportId + "-" + projectId)));

        // Delete button click listener
        deleteVulnerabilityButton.addClickListener(e -> {
            VulnerabilityDTO selected = grid.asSingleSelect().getValue();
            if (selected != null) {
                vulnerabilityService.deleteVulnerability(selected.getVulnerabilityId());
                Notification.show("Vulnerability deleted successfully!", 3000, Notification.Position.TOP_CENTER);
                refreshGrid();
            } else {
                Notification.show("Please select a vulnerability to delete.", 3000, Notification.Position.TOP_CENTER);
            }
        });

        // Layout for buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(addVulnerabilityButton, deleteVulnerabilityButton);
        buttonLayout.setSpacing(true);
        add(buttonLayout);

        // Grid setup
        configureGrid();
        refreshGrid();

        add(grid);
    }

    private void configureGrid() {
        grid.setColumns("vulnerabilityId", "description", "severity");

        // Add editable column for status
        grid.addColumn(new ComponentRenderer<>(vulnerability -> {
                    ComboBox<Status> statusComboBox = new ComboBox<>();
                    statusComboBox.setItems(Status.values());
                    statusComboBox.setValue(vulnerability.getStatus());
                    statusComboBox.addValueChangeListener(event -> {
                        Status newStatus = event.getValue();
                        if (newStatus != null) {
                            try {
                                vulnerability.setStatus(newStatus);
                                vulnerabilityService.updateVulnerabilityStatus(vulnerability.getVulnerabilityId(), newStatus);
                                Notification.show("Status updated successfully!", 3000, Notification.Position.TOP_CENTER);
                                refreshGrid();
                            } catch (Exception e) {
                                Notification.show("Failed to update status: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
                            }
                        }
                    });
                    return statusComboBox;
                }))
                .setHeader("Edit Status")
                .setAutoWidth(false)
                .setWidth("200px"); // Setează o lățime fixă
    }

    private void refreshGrid() {
        List<VulnerabilityDTO> vulnerabilities = vulnerabilityService.getVulnerabilitiesByReport(reportId);
        List<VulnerabilityDTO> mutableVulnerabilities = new ArrayList<>(vulnerabilities); // Transformă în listă modificabilă
        mutableVulnerabilities.sort((v1, v2) -> v1.getVulnerabilityId().compareTo(v2.getVulnerabilityId())); // Sortează după ID
        grid.setItems(mutableVulnerabilities);
    }

}
