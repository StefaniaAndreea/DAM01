package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

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