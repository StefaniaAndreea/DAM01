package com.audit_management_app.auditmanagement_DAM.domain.services;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;

import java.util.Date;
import java.util.List;

public interface IProjectEntityFactory {

    Project createProject(String name, int clientId, int teamId, Date startDate, Date endDate);

    Task createTask(String description, int projectId);

    Project toEntity(Project projectDTO);
}