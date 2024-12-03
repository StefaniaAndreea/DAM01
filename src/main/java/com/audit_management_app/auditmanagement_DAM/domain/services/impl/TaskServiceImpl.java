package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.ITaskService;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectRepository;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void markAsDone(int taskId) {
        // Găsim proiectul care conține sarcina
        Task task = findTaskById(taskId);

        // Setăm statusul ca COMPLETED
        task.setStatus(Task.TaskStatus.COMPLETED);

        // Salvăm proiectul cu sarcina actualizată
        projectRepository.save(task.getProject());
    }

    @Override
    @Transactional
    public void assignEmployee(int taskId, int employeeId) {
        // Găsim sarcina prin intermediul proiectului
        Task task = findTaskById(taskId);

        // Găsim angajatul după ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        // Setăm angajatul sarcinii
        task.setAssignedTo(employee);

        // Salvăm proiectul cu sarcina actualizată
        projectRepository.save(task.getProject());
    }

    private Task findTaskById(int taskId) {
        return projectRepository.findAll().stream()
                .flatMap(project -> project.getTasks().stream())
                .filter(task -> task.getTaskId() == taskId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
    }
}