package com.audit_management_app.auditmanagement_DAM.domain.projects;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int taskId;

    private String description;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;



    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED
    }

    public Task(String description, Employee assignedTo, TaskStatus status, Project project) {
        this.description = description;
        this.assignedTo = assignedTo;
        this.status = status;
        this.project = project;
    }
}