package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.services.IProjectValidatingService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
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
        // Validăm obiectul Project și colectăm violările
        Set<ConstraintViolation<Project>> violations = validator.validate(project);

        // Logăm numărul de violări
        logger.info("Violations count: " + violations.size());

        // Transformăm violările într-un set de mesaje detaliate
        return violations.stream()
                .map(violation -> violation.getMessage()
                        + " (Invalid value: " + violation.getInvalidValue() + ")")
                .collect(Collectors.toSet());
    }

    @Override
    public void validateWithException(Project project) throws ValidationException {
        // Validăm proiectul și colectăm mesajele de violare
        Set<String> violations = validate(project);

        // Logăm numărul de violări înainte de a arunca excepția
        logger.info("Violations count (to generate exception): " + violations.size());

        // Dacă există violări, generăm o excepție personalizată
        if (!violations.isEmpty()) {
            String violationExceptionMessage = violations.stream()
                    .map(violation -> "Validation failed: " + violation)
                    .collect(Collectors.joining(", "));
            throw new ValidationException(violationExceptionMessage);
        }
    }
}