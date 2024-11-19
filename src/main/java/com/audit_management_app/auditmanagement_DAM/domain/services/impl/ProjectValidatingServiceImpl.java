package com.audit_management_app.auditmanagement_DAM.domain.services.impl;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectValidatingService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ProjectValidatingServiceImpl implements IProjectValidatingService {

    private static final Logger logger = Logger.getLogger(ProjectValidatingServiceImpl.class.getName());

    @Autowired
    private Validator validator;

    @Override
    public Set<String> validate(Project project) {

        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        logger.info("Violations count: " + violations.size());
        return violations.stream()
                .map(violation -> violation.getMessage()
                        + " (" + violation.getInvalidValue()
                        + ") is an invalid value!")
                .collect(Collectors.toSet());
    }

    @Override
    public void validateWithException(Project project) throws Exception {
        Set<String> violations = validate(project);
        logger.info("Violations count (to generate exception): " + violations.size());
        if (!violations.isEmpty()) {
            String violationExceptionMessage = violations.stream()
                    .map(violation -> "\n>>> Validation exception: " + violation)
                    .collect(Collectors.joining(", "));
            throw new Exception(violationExceptionMessage);
        }
    }
}
