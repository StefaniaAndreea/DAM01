package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*; // Importăm jakarta.persistence pentru JPA

@Data
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private float salary;

    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "assigned_project_id")
    private Project assignedProject;

    public void assignToProject(Project project) {
        this.assignedProject = project;
        this.isAvailable = false;
    }

    public void removeFromProject() {
        this.assignedProject = null;
        this.isAvailable = true;
    }

    public void updateAvailability(boolean status) {
        this.isAvailable = status;
    }

    public enum Role {
        ADMINISTRATOR, MANAGER, PENTESTER
    }

    public Employee(int employeeId, String name, float salary, Role role, boolean isAvailable, Project assignedProject) {
        this.employeeId = employeeId;
        this.name = name;
        this.salary = salary;
        this.role = role;
        this.isAvailable = isAvailable;
        this.assignedProject = assignedProject;
    }
}
