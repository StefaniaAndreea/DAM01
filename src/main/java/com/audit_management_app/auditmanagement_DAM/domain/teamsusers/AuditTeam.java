package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class AuditTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamId;

    private String teamName;

    @ManyToMany
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> members;

    @OneToOne
    @JoinColumn(name = "assigned_project_id")
    private Project assignedProject;

    public void addMember(Employee employee) {
        if (employee.isAvailable()) {
            members.add(employee);
            employee.updateAvailability(false);
        }
    }

    public void removeMember(Employee employee) {
        members.remove(employee);
        employee.updateAvailability(true);
    }

    public void assignToProject(Project project) {
        this.assignedProject = project;
    }

    public void disbandTeam() {
        members.forEach(member -> member.updateAvailability(true));
        members.clear();
        this.assignedProject = null;
    }

    public AuditTeam(int teamId, String teamName, Project assignedProject) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.assignedProject = assignedProject;
    }
}
