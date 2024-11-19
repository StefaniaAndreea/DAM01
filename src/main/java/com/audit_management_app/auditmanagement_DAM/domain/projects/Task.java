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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private int taskId;

    private String description;

    @ManyToOne
    private Employee assignedTo;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne

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
}
