package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Integer> {
    // Găsirea task-urilor după status
    List<Task> findByStatus(Task.TaskStatus status);

    // Găsirea task-urilor asignate unui angajat
    List<Task> findByAssignedTo(Employee employee);

    // Găsirea task-urilor asociate unui proiect
    List<Task> findByProject(Project project);

    // Găsirea task-urilor unui angajat după status
    List<Task> findByAssignedToAndStatus(Employee employee, Task.TaskStatus status);
}
