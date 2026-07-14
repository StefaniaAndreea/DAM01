package org.audit.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private Integer taskId; // ID-ul unic al task-ului
    private String description; // Descrierea task-ului
    private Integer assignedToId; // ID-ul angajatului asignat (dacă este asignat)
    private TaskStatus status; // Statusul task-ului (enum: PENDING, IN_PROGRESS, COMPLETED)
    private Integer projectId; // ID-ul proiectului asociat task-ului

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Integer assignedToId) {
        this.assignedToId = assignedToId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
