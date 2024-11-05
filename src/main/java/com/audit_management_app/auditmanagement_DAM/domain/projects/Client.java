package com.audit_management_app.auditmanagement_DAM.domain.projects;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;

    private String name;
    private String contactPerson;
    private String contactEmail;
    private String description;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    public void addProject(Project project) {
        projects.add(project);
        project.setClient(this);
    }

    public List<Project> getActiveProjects() {
        return projects.stream()
                .filter(project -> project.getStatus() == Project.Status.ONGOING)
                .toList();
    }

    public Client(int clientId, String name, String contactPerson, String description, String contactEmail) {
        this.clientId = clientId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.description = description;
        this.contactEmail = contactEmail;
    }

}
