package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
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
public class AuditTeam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer teamId;

    private String teamName;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<Employee> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<Project> assignedProjects = new ArrayList<>();

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

}