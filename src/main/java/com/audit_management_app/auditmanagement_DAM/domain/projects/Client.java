package com.audit_management_app.auditmanagement_DAM.domain.projects;

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
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer clientId;

    private String name;

    private String contactPerson;

    private String contactEmail;

    private String description;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    public Client(String name, String contactPerson, String contactEmail, String description) {
        this.name = name;
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.description = description;
    }
}