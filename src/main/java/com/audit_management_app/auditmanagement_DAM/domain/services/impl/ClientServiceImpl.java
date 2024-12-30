package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements IClientService {
    @Autowired
    IClientRepository clientRepository;

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Integer clientId, Client updatedClient) {
        // Verificăm dacă clientul există
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + clientId + " does not exist."));

        // Actualizăm câmpurile clientului
        existingClient.setName(updatedClient.getName());
        existingClient.setContactPerson(updatedClient.getContactPerson());
        existingClient.setContactEmail(updatedClient.getContactEmail());
        existingClient.setDescription(updatedClient.getDescription());

        // Salvăm clientul actualizat
        return clientRepository.save(existingClient);
    }

    @Override
    public Client getClientById(Integer clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + clientId + " does not exist."));
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> findClientsByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }
}
