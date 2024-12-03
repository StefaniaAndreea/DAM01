package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;

public interface ITaskService {

    void markAsDone(int taskId);

    void assignEmployee(int taskId, int employeeId);
}