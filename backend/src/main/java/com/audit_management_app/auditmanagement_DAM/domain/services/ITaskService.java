package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITaskService {

    Task addTaskToProject(Integer projectId, Task task) throws IllegalArgumentException;
    Task addTaskWithoutEmployee(int projectId, Task task);
    void markAsDone(int taskId);
    void assignEmployee(int taskId, int employeeId);
    List<Task> findTasksByStatus(Task.TaskStatus status);
    List<Task> findTasksByEmployee(int employeeId);
    List<Task> findTasksByProject(int projectId);
    List<Task> findTasksByEmployeeAndStatus(int employeeId, Task.TaskStatus status);
    Task updateTask(Integer taskId, Task task);
    void deleteTask(Integer taskId);
    List<Task> findAllTasks();
    void updateTaskStatus(Integer taskId, Task.TaskStatus status);

}