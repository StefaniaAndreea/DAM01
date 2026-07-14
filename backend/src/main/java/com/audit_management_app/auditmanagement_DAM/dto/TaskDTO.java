package com.audit_management_app.auditmanagement_DAM.dto;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaskDTO {
    private Integer taskId; // ID-ul unic al task-ului
    private String description; // Descrierea task-ului
    private Integer assignedToId; // ID-ul angajatului asignat (dacă este asignat)
    private Task.TaskStatus status; // Statusul task-ului (ex: PENDING, IN_PROGRESS, COMPLETED)
    private Integer projectId; // ID-ul proiectului asociat task-ului

    public TaskDTO() {
    }
}
