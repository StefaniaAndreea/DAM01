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
    private EmployeeRole role;

    private float salary;

    private boolean isAvailable;

    public enum EmployeeRole {
        ADMINISTRATOR, MANAGER, PENTESTER
    }
}
