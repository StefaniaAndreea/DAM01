package com.audit_management_app.auditmanagement_DAM.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeDTO {
    private Integer employeeId;
    private String name;
    private String role;
    private float salary;
    private boolean isAvailable;
    private Integer teamId;
}
