package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*; // Importăm jakarta.persistence pentru JPA

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Project getAssignedProject() {
        return assignedProject;
    }

    public void setAssignedProject(Project assignedProject) {
        this.assignedProject = assignedProject;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
