package com.audit_management_app.auditmanagement_DAM.domain.projects;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class AuditReport implements Serializable {

    @Id @EqualsAndHashCode.Include
    @GeneratedValue
    private int reportId;

    @ManyToOne
    private Project project;

    private String filePath;

    @Temporal(TemporalType.DATE)
    private Date submissionDate;

    @ManyToOne
    private Employee author;
    @OneToMany(mappedBy = "report",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Vulnerability> vulnerabilities = new ArrayList<>();

    public void submitReport(String filePath, Employee author) {
        this.filePath = filePath;
        this.author = author;
        this.submissionDate = new Date();
    }

    public AuditReport(int reportId, String filePath, Project project, Date submissionDate, Employee author) {
        this.reportId = reportId;
        this.filePath = filePath;
        this.project = project;
        this.submissionDate = submissionDate;
        this.author = author;
    }
}
