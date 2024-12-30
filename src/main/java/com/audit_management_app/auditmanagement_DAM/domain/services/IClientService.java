package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;

import java.util.List;

public interface IClientService {
    Client addClient(Client client);

    Client updateClient(Integer clientId, Client updatedClient);

    Client getClientById(Integer clientId);

    List<Client> getAllClients();

    List<Client> findClientsByName(String name);
}
