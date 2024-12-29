package com.audit_management_app.auditmanagement_DAM.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class AuditTeamDTO {
    private Integer teamId;
    private String teamName;
    private List<Integer> memberIds;
}
