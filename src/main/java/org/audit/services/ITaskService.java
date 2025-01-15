package org.audit.services;

import org.audit.dto.TaskDTO;
import org.audit.dto.TaskStatus;

import java.util.List;

public interface ITaskService {
    TaskDTO addTaskToProject(Integer projectId, TaskDTO task);
    TaskDTO addTaskWithoutEmployee(Integer projectId, TaskDTO task);
    void markTaskAsDone(Integer taskId);
    void assignEmployeeToTask(Integer taskId, Integer employeeId);
    TaskDTO updateTask(Integer taskId, TaskDTO task);
    void deleteTask(Integer taskId);
    List<TaskDTO> getTasksByStatus(TaskStatus status);
    List<TaskDTO> getTasksByEmployee(Integer employeeId);
    List<TaskDTO> getTasksByProject(Integer projectId);
    List<TaskDTO> getAllTasks();
    void updateTaskStatus(Integer taskId, TaskStatus status);
}
