package com.audit_management_app.auditmanagement_DAM.domain.projects;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    private String description;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private Employee assignedTo;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public void markAsDone() {
        this.status = TaskStatus.COMPLETED;
    }

    public void assignEmployee(Employee employee) {
        this.assignedTo = employee;
    }

    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED
    }

    public Task(int taskId, String description, Employee assignedTo, TaskStatus status, Project project) {
        this.taskId = taskId;
        this.description = description;
        this.assignedTo = assignedTo;
        this.status = status;
        this.project = project;
    }
}
