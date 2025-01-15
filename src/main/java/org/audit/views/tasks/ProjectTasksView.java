package org.audit.views.tasks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.audit.dto.EmployeeDTO;
import org.audit.dto.TaskDTO;
import org.audit.dto.TaskStatus;
import org.audit.services.ITaskService;
import org.audit.services.IEmployeeService;

import java.util.List;

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
        Button markAsDoneButton = new Button("Mark as Done", e -> markTaskAsDone());
        Button deleteTaskButton = new Button("Delete Task", e -> deleteSelectedTask());
        HorizontalLayout actions = new HorizontalLayout(markAsDoneButton, deleteTaskButton);

        add(taskGrid, actions);

        loadTasks();
    }

    private void configureTaskGrid() {
        taskGrid.setColumns();
        taskGrid.addColumn(TaskDTO::getTaskId).setHeader("Task ID").setSortable(true);
        taskGrid.addColumn(TaskDTO::getDescription).setHeader("Description").setSortable(true);

        // Status column with ComboBox
        Grid.Column<TaskDTO> statusColumn = taskGrid.addColumn(task -> task.getStatus().name())
                .setHeader("Status").setSortable(true);

        Binder<TaskDTO> binder = new Binder<>(TaskDTO.class);
        Editor<TaskDTO> editor = taskGrid.getEditor();
        editor.setBinder(binder);

        ComboBox<TaskStatus> statusComboBox = new ComboBox<>();
        statusComboBox.setItems(TaskStatus.values());
        binder.forField(statusComboBox).bind(TaskDTO::getStatus, TaskDTO::setStatus);

        statusColumn.setEditorComponent(statusComboBox);

        taskGrid.addItemDoubleClickListener(event -> {
            editor.editItem(event.getItem());
            statusComboBox.focus();
        });

        editor.addCloseListener(event -> {
            if (editor.getItem() != null) {
                TaskDTO task = editor.getItem();
                try {
                    taskService.updateTask(task.getTaskId(), task); // Update task in backend
                    Notification.show("Task status updated successfully.", 3000, Notification.Position.TOP_CENTER);
                    loadTasks(); // Refresh grid
                } catch (Exception ex) {
                    Notification.show("Failed to update task: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
                }
            }
        });

        // Assigned to column
        taskGrid.addColumn(new TextRenderer<>(task -> {
            Integer assignedToId = task.getAssignedToId();
            if (assignedToId != null) {
                EmployeeDTO employee = employeeService.getEmployeeById(assignedToId);
                return employee != null ? employee.getName() : "Unknown";
            }
            return "Unassigned";
        })).setHeader("Assigned To").setSortable(true);
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
}
