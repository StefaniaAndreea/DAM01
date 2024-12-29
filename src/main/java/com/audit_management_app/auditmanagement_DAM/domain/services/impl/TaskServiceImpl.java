package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.ITaskRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.ITaskService;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectRepository;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class TaskServiceImpl implements ITaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private ITaskRepository taskRepository;

    @Override
    public Task addTaskToProject(Integer projectId, Task task) throws IllegalArgumentException {
        // Verificăm dacă proiectul există
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist."));
        if (project.getStatus() == Project.StatusProiect.ARCHIVED) {
            throw new IllegalArgumentException("Cannot add tasks to an archived project.");
        }
        // Verificăm dacă angajatul există (dacă este specificat)
        if (task.getAssignedTo() != null) {
            Integer assignedToId = task.getAssignedTo().getEmployeeId();
            Employee employee = employeeRepository.findById(assignedToId)
                    .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + assignedToId + " does not exist."));
            task.setAssignedTo(employee);
        }

        // Asociem task-ul cu proiectul
        task.setProject(project);

        // Salvăm task-ul
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task addTaskWithoutEmployee(int projectId, Task task) {
        // Găsim proiectul în care dorim să adăugăm task-ul
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));

        // Asociem task-ul cu proiectul
        task.setProject(project);

        // Salvăm task-ul în baza de date
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void markAsDone(int taskId) {
        Task task = findTaskById(taskId);

        task.setStatus(Task.TaskStatus.COMPLETED);
        taskRepository.save(task);

        // Verificăm statusul angajatului
        checkAndSetEmployeeAvailability(task);

        // Verificăm statusul proiectului
        checkAndSetProjectCompletion(task);
    }

    private void checkAndSetEmployeeAvailability(Task task) {
        if (task.getAssignedTo() != null) {
            Integer employeeId = task.getAssignedTo().getEmployeeId();
            boolean areAllEmployeeTasksCompleted = taskRepository.areAllTasksCompleted(employeeId);

            if (areAllEmployeeTasksCompleted) {
                Employee employee = task.getAssignedTo();
                employee.setAvailable(true);
                employeeRepository.save(employee);
            }
        }
    }

    private void checkAndSetProjectCompletion(Task task) {
        Project project = task.getProject();
        boolean areAllProjectTasksCompleted = project.getTasks().stream()
                .allMatch(t -> t.getStatus() == Task.TaskStatus.COMPLETED);

        if (areAllProjectTasksCompleted) {
            project.setStatus(Project.StatusProiect.COMPLETED);
            projectRepository.save(project);
        }
    }

    @Override
    @Transactional
    public void assignEmployee(int taskId, int employeeId) {
        // Găsim task-ul
        Task task = findTaskById(taskId);

        // Găsim angajatul
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        // Verificăm disponibilitatea
        if (!employee.isAvailable()) {
            throw new IllegalArgumentException("Employee is not available.");
        }

        // Asignăm angajatul
        task.setAssignedTo(employee);

        // Salvăm task-ul
        taskRepository.save(task);
        logger.info("Successfully assigned employee {} to task {}", employeeId, taskId);
    }

    @Override
    @Transactional
    public void deleteTask(Integer taskId) {
        // Verificăm dacă task-ul există
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
        // Ștergem task-ul
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public Task updateTask(Integer taskId, Task updatedTask) {
        // Verificăm dacă task-ul există
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));

        // Actualizăm câmpurile task-ului
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        if (updatedTask.getAssignedTo() != null) {
            existingTask.setAssignedTo(updatedTask.getAssignedTo());
        }

        // Salvăm task-ul actualizat
        return taskRepository.save(existingTask);
    }

    private Task findTaskById(int taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
    }

    @Override
    @Transactional
    public List<Task> findTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public List<Task> findTasksByEmployee(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        return taskRepository.findByAssignedTo(employee);
    }

    @Override
    @Transactional
    public List<Task> findTasksByProject(int projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
        return taskRepository.findByProject(project);
    }

    @Override
    @Transactional
    public List<Task> findTasksByEmployeeAndStatus(int employeeId, Task.TaskStatus status) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        return taskRepository.findByAssignedToAndStatus(employee, status);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
}