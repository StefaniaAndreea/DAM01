package com.audit_management_app.auditmanagement_DAM.rest.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.services.ITaskService;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import com.audit_management_app.auditmanagement_DAM.dto.TaskDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/app/tasks")
@Transactional
public class TaskRestServices {
    @Autowired
    private ITaskService taskService;

    @PostMapping("/projects/{projectId}")
    public TaskDTO addTaskToProject(@PathVariable Integer projectId, @RequestBody TaskDTO taskDTO) {
        // Convertim DTO în entitate Task
        Task task = convertToEntity(taskDTO);

        // Apelăm serviciul pentru a adăuga task-ul
        Task createdTask = taskService.addTaskToProject(projectId, task);

        // Convertim entitatea Task în DTO pentru răspuns
        return convertToDTO(createdTask);
    }
//POST http://localhost:8088/api/rest/app/tasks/projects/1
    //{
//  "description": "Task Description",
//  "status": "PENDING",
//  "assignedToId": 1
//}
    @PostMapping("/projects/{projectId}/without-employee")
    public TaskDTO addTaskWithoutEmployee(@PathVariable Integer projectId, @RequestBody TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        Task createdTask = taskService.addTaskWithoutEmployee(projectId, task);
        return convertToDTO(createdTask);
    }

    //POST http://localhost:8088/api/rest/app/tasks/projects/1/without-employee
    // {
    //  "description": "Review project documentation",
    //  "status": "IN_PROGRESS"
    //}
    @PutMapping("/{taskId}/mark-as-done")
    public String markTaskAsDone(@PathVariable Integer taskId) {
        taskService.markAsDone(taskId);
        return "Task with ID " + taskId + " marked as COMPLETED.";
    }

    //PUT http://localhost:8088/api/rest/app/tasks/2/mark-as-done

    @PutMapping("/{taskId}/assign-employee/{employeeId}")
    public String assignEmployeeToTask(@PathVariable Integer taskId, @PathVariable Integer employeeId) {
        taskService.assignEmployee(taskId, employeeId);
        return "Employee with ID " + employeeId + " assigned to task with ID " + taskId + ".";
    }
    //PUT http://localhost:8088/api/rest/app/tasks/2/assign-employee/1


    @PutMapping("/{taskId}")
    public TaskDTO updateTask(@PathVariable Integer taskId, @RequestBody TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        Task updatedTask = taskService.updateTask(taskId, task);
        return convertToDTO(updatedTask);
    }

    //PUT http://localhost:8088/api/rest/app/tasks/2
    // {
    //  "description": "Updated task description",
    //  "status": "COMPLETED",
    //  "assignedToId": 4
    //}

    @GetMapping("/status/{status}")
    public List<TaskDTO> findTasksByStatus(@PathVariable Task.TaskStatus status) {
        return taskService.findTasksByStatus(status).stream()
                .map(this::convertToDTO)
                .toList();
    }
    //GET http://localhost:8088/api/rest/app/tasks/status/{status}

    @GetMapping("/employee/{employeeId}")
    public List<TaskDTO> findTasksByEmployee(@PathVariable Integer employeeId) {
        return taskService.findTasksByEmployee(employeeId).stream()
                .map(this::convertToDTO)
                .toList();
    }
    //GET http://localhost:8088/api/rest/app/tasks/employee/1

    @GetMapping("/project/{projectId}")
    public List<TaskDTO> findTasksByProject(@PathVariable Integer projectId) {
        return taskService.findTasksByProject(projectId).stream()
                .map(this::convertToDTO)
                .toList();
    }
    //GET http://localhost:8088/api/rest/app/tasks/project/1

    @GetMapping("/employee/{employeeId}/status/{status}")
    public List<TaskDTO> findTasksByEmployeeAndStatus(@PathVariable Integer employeeId, @PathVariable Task.TaskStatus status) {
        return taskService.findTasksByEmployeeAndStatus(employeeId, status).stream()
                .map(this::convertToDTO)
                .toList();
    }
    //GET http://localhost:8088/api/rest/app/tasks/employee/1/status/PENDING
    @GetMapping("/all")
    public List<TaskDTO> getAllTasks() {
        return taskService.findAllTasks().stream()
                .map(this::convertToDTO)
                .toList();
    }

    //GET http://localhost:8088/api/rest/app/tasks/all

    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return "Task with ID " + taskId + " has been deleted.";
    }

    // DELETE http://localhost:8088/api/rest/app/tasks/{taskId}

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        if (taskDTO.getAssignedToId() != null) {
            Employee employee = new Employee();
            employee.setEmployeeId(taskDTO.getAssignedToId());
            task.setAssignedTo(employee);
        }
        return task;
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getEmployeeId());
        }
        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getProjectId());
        }
        return dto;
    }
}
