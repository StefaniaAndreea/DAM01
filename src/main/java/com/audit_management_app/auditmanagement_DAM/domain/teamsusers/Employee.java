package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer employeeId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private AuditTeam team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole role;

    private float salary;

    @Column(nullable = false)
    private boolean isAvailable;

    public enum EmployeeRole {
        PENTESTER,
        MANAGER
    }

    public Employee(String name, AuditTeam team, EmployeeRole role, float salary, boolean isAvailable) {
        this.name = name;
        this.team = team;
        this.role = role;
        this.salary = salary;
        this.isAvailable = isAvailable;
    }

    public void assignToTeam(AuditTeam team) {
        this.team = team;
    }

    public void updateAvailability(boolean availability) {
        this.isAvailable = availability;
    }
}