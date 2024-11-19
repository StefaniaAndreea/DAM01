package com.audit_management_app.auditmanagement_DAM.domain.projects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int clientId;
    private String name;
    private String contactPerson;
    private String contactEmail;
    private String description;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Project> projects;


}



