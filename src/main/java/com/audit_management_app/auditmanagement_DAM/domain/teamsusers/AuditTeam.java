package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuditTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int teamId;

    private String teamName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Employee> members = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "assigned_project_id")
    private Project assignedProject;

    public AuditTeam(String teamName) {
        this.teamName = teamName;
    }

    public void addMember(Employee employee) {
        members.add(employee);
        employee.setTeam(this);
    }

    public void removeMember(Employee employee) {
        members.remove(employee);
        employee.setTeam(null);
    }

    public void assignProject(Project project) {
        this.assignedProject = project;
    }
}