package com.audit_management_app.auditmanagement_DAM.domain.projects;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class AuditReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reportId;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String filePath;

    @Temporal(TemporalType.DATE)
    private Date submissionDate;

    @ManyToOne
    private Employee author;

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

    public int getReportId() {
        return reportId;
    }

}
