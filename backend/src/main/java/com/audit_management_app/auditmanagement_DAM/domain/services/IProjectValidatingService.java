package com.audit_management_app.auditmanagement_DAM.domain.services;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;

import java.util.Set;

public interface IProjectValidatingService {

    Set<String> validate(Project project);

    void validateWithException(Project project) throws Exception;
}