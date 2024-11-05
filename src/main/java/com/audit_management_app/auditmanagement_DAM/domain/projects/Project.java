package com.audit_management_app.auditmanagement_DAM.domain.projects;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.projects.AuditReport;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private AuditTeam team;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private float progress;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditReport> reports;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    public void addReport(AuditReport report) {
        reports.add(report);
        report.setProject(this);
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setProject(this);
    }

    public void markAsCompleted() {
        this.status = Status.ARCHIVED;
    }

    public void calculateProgress() {
        long completedTasks = tasks.stream().filter(task -> task.getStatus() == Task.TaskStatus.COMPLETED).count();
        this.progress = (float) completedTasks / tasks.size() * 100;
    }



    public enum Status {
        ONGOING, COMPLETED, ARCHIVED
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Project(int projectId, String name, AuditTeam team, Client client, Date startDate, Date endDate, Status status, float progress) {
        this.projectId = projectId;
        this.name = name;
        this.team = team;
        this.client = client;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.progress = progress;
    }

}
