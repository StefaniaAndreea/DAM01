package com.audit_management_app.auditmanagement_DAM.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@NoArgsConstructor
public class ProjectDTO {
    private Integer projectId;

    private String name;

    private Integer clientId;  // ID-ul clientului asociat proiectului

    private Integer teamId;    // ID-ul echipei asociate proiectului

    private Date startDate;

    private Date endDate;

    private String status;     // Statusul proiectului (ONGOING, COMPLETED, ARCHIVED)

    private float progress;

}
