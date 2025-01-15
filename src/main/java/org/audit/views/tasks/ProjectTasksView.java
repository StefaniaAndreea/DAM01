package org.audit.views.tasks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.audit.dto.EmployeeDTO;
import org.audit.dto.TaskDTO;
import org.audit.dto.TaskStatus;
import org.audit.services.ITaskService;
import org.audit.services.IEmployeeService;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectTasksView extends VerticalLayout {
    private final ITaskService taskService;
    private final IEmployeeService employeeService;
    private final Integer projectId;
    private final Grid<TaskDTO> taskGrid = new Grid<>(TaskDTO.class);

    public ProjectTasksView(ITaskService taskService, IEmployeeService employeeService, Integer projectId) {
        this.taskService = taskService;
        this.employeeService = employeeService;
        this.projectId = projectId;

        setSpacing(true);
        setPadding(true);
        setWidthFull();

        add(new H1("Tasks"));

        // Configure task grid
        configureTaskGrid();

        // Add action buttons
        Button addTaskButton = new Button("Add Task", e -> openAddTaskDialog());
        Button markAsDoneButton = new Button("Mark as Done", e -> markTaskAsDone());
        Button deleteTaskButton = new Button("Delete Task", e -> deleteSelectedTask());
        HorizontalLayout actions = new HorizontalLayout(addTaskButton, markAsDoneButton, deleteTaskButton);

        add(taskGrid, actions);

        loadTasks();
    }

    private void configureTaskGrid() {
        taskGrid.setColumns();
        taskGrid.addColumn(TaskDTO::getTaskId).setHeader("Task ID").setSortable(true);
        taskGrid.addColumn(TaskDTO::getDescription).setHeader("Description").setSortable(true);

        // Status column with ComboBox
        taskGrid.addComponentColumn(task -> {
            ComboBox<TaskStatus> statusComboBox = new ComboBox<>();
            statusComboBox.setItems(TaskStatus.values());
            statusComboBox.setValue(task.getStatus());
            statusComboBox.addValueChangeListener(event -> {
                task.setStatus(event.getValue());
                try {
                    taskService.updateTaskStatus(task.getTaskId(), event.getValue());
                    Notification.show("Task status updated successfully.", 3000, Notification.Position.TOP_CENTER);
                } catch (Exception ex) {
                    Notification.show("Failed to update task: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
                }
            });
            return statusComboBox;
        }).setHeader("Status").setSortable(true);

        // Assigned to column
        taskGrid.addColumn(task -> {
            Integer assignedToId = task.getAssignedToId();
            if (assignedToId != null) {
                EmployeeDTO employee = employeeService.getEmployeeById(assignedToId);
                return employee != null ? employee.getName() : "Unknown";
            }
            return "Unassigned";
        }).setHeader("Assigned To").setSortable(true);

        taskGrid.setWidthFull();
    }

    private void loadTasks() {
        List<TaskDTO> tasks = taskService.getTasksByProject(projectId);
        taskGrid.setItems(tasks);
    }

    private void markTaskAsDone() {
        TaskDTO selectedTask = taskGrid.asSingleSelect().getValue();
        if (selectedTask != null && selectedTask.getStatus() != TaskStatus.COMPLETED) {
            taskService.markTaskAsDone(selectedTask.getTaskId());
            Notification.show("Task marked as done.", 3000, Notification.Position.TOP_CENTER);
            loadTasks();
        } else {
            Notification.show("Please select an incomplete task.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void deleteSelectedTask() {
        TaskDTO selectedTask = taskGrid.asSingleSelect().getValue();
        if (selectedTask != null) {
            taskService.deleteTask(selectedTask.getTaskId());
            Notification.show("Task deleted.", 3000, Notification.Position.TOP_CENTER);
            loadTasks();
        } else {
            Notification.show("Please select a task to delete.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void openAddTaskDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("500px");

        TextField descriptionField = new TextField("Description");
        ComboBox<TaskStatus> statusField = new ComboBox<>("Status", TaskStatus.values());
        ComboBox<EmployeeDTO> assignedToField = new ComboBox<>("Assign To");

        // Încărcare angajați fără duplicări
        List<EmployeeDTO> employees = employeeService.getEmployeesByTeamId(projectId)
                .stream()
                .distinct() // Elimină angajații duplicat
                .collect(Collectors.toList());
        assignedToField.setItems(employees);
        assignedToField.setItemLabelGenerator(EmployeeDTO::getName);

        Button saveButton = new Button("Save", e -> {
            try {
                TaskDTO newTask = new TaskDTO();
                newTask.setDescription(descriptionField.getValue());
                newTask.setStatus(statusField.getValue());
                EmployeeDTO assignedTo = assignedToField.getValue();
                if (assignedTo != null) {
                    newTask.setAssignedToId(assignedTo.getEmployeeId());
                }
                newTask.setProjectId(projectId);

                taskService.addTaskToProject(projectId, newTask);
                Notification.show("Task added successfully.", 3000, Notification.Position.TOP_CENTER);
                dialog.close();
                loadTasks();
            } catch (Exception ex) {
                Notification.show("Failed to add task: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        FormLayout formLayout = new FormLayout();
        formLayout.add(descriptionField, statusField, assignedToField);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttons);
        dialog.add(dialogLayout);
        dialog.open();
    }


}
