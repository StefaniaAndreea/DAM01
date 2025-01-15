package org.audit.views.employees;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.audit.dto.EmployeeDTO;
import org.audit.services.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@Route("employees/master-detail")
@PageTitle("Employees Master-Detail")
public class MasterDetailEmployeeView extends VerticalLayout {

    private final IEmployeeService employeeService;
    private final Grid<EmployeeDTO> employeeGrid = new Grid<>(EmployeeDTO.class);
    private final Binder<EmployeeDTO> binder = new Binder<>(EmployeeDTO.class);

    private final TextField nameField = new TextField("Name");
    private final TextField roleField = new TextField("Role");
    private final TextField salaryField = new TextField("Salary");
    private final Button saveButton = new Button("Save");
    private final Button clearButton = new Button("Clear");

    private EmployeeDTO currentEmployee;

    @Autowired
    public MasterDetailEmployeeView(IEmployeeService employeeService) {
        this.employeeService = employeeService;

        // Configure layout
        setWidthFull();
        setSpacing(true);
        setPadding(true);

        // Add title
        H1 title = new H1("Employees Master-Detail");
        add(title);

        // Configure grid
        configureGrid();

        // Configure form
        VerticalLayout formLayout = configureForm();

        // Combine grid and form in a horizontal layout
        HorizontalLayout mainLayout = new HorizontalLayout(employeeGrid, formLayout);
        mainLayout.setWidthFull();
        mainLayout.setFlexGrow(2, employeeGrid);
        mainLayout.setFlexGrow(1, formLayout);

        add(mainLayout);

        loadEmployees();
    }

    private void configureGrid() {
        employeeGrid.setColumns("employeeId", "name", "role", "salary");
        employeeGrid.setWidthFull();
        employeeGrid.setHeight("600px");
        employeeGrid.asSingleSelect().addValueChangeListener(event -> {
            currentEmployee = event.getValue();
            if (currentEmployee != null) {
                binder.readBean(currentEmployee);
            }
        });
    }

    private VerticalLayout configureForm() {
        VerticalLayout formLayout = new VerticalLayout();

        binder.bind(nameField, EmployeeDTO::getName, EmployeeDTO::setName);
        binder.bind(roleField, EmployeeDTO::getRole, EmployeeDTO::setRole);
        binder.forField(salaryField)
                .withConverter(
                        value -> value == null || value.isEmpty() ? 0.0f : Float.parseFloat(value),
                        String::valueOf
                )
                .bind(EmployeeDTO::getSalary, EmployeeDTO::setSalary);

        saveButton.addClickListener(e -> saveEmployee());
        clearButton.addClickListener(e -> clearForm());

        formLayout.add(nameField, roleField, salaryField, saveButton, clearButton);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidth("400px");
        return formLayout;
    }

    private void saveEmployee() {
        try {
            if (currentEmployee == null) {
                currentEmployee = new EmployeeDTO();
            }
            binder.writeBean(currentEmployee);

            if (currentEmployee.getEmployeeId() == null) {
                // Adaugă un nou angajat
                employeeService.addEmployee(currentEmployee);
            } else {
                // Actualizează angajatul existent
                employeeService.updateEmployee(currentEmployee.getEmployeeId(), currentEmployee);
            }

            Notification.show("Employee saved successfully.", 3000, Notification.Position.TOP_CENTER);
            loadEmployees();
            clearForm();
        } catch (Exception ex) {
            Notification.show("Failed to save employee: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void clearForm() {
        currentEmployee = null;
        binder.readBean(null);
    }

    private void loadEmployees() {
        try {
            List<EmployeeDTO> employees = employeeService.getAllEmployees();
            System.out.println("Employees loaded: " + employees); // Debugging

            // Elimină duplicatele dacă lista conține intrări redundante
            employeeGrid.setItems(employees.stream().distinct().collect(Collectors.toList()));
        } catch (Exception ex) {
            Notification.show("Failed to load employees: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

}
