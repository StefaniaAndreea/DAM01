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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public Client(String name, String contactPerson, String description, String contactEmail) {
        this.name = name;
        this.contactPerson = contactPerson;
        this.description = description;
        this.contactEmail = contactEmail;
    }

    public int getClientId() {
        return clientId;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
