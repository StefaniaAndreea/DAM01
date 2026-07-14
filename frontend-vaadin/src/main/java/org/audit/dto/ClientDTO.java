package org.audit.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private Integer clientId;
    private String name;
    private String contactPerson;
    private String contactEmail;
    private String description;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientDTO(Integer clientId, String name, String contactPerson, String contactEmail, String description) {
        this.clientId = clientId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.description = description;
    }

    public ClientDTO() {
    }
}
