package com.audit_management_app.auditmanagement_DAM.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private Integer clientId;
    private String name;
    private String contactPerson;
    private String contactEmail;
    private String description;
}
