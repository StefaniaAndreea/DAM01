package com.audit_management_app.auditmanagement_DAM.rest.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientService;
import com.audit_management_app.auditmanagement_DAM.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/app/clients")
public class ClientRestServices {

    @Autowired
    private IClientService clientService;

    @PostMapping
    public ClientDTO addClient(@RequestBody ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        Client savedClient = clientService.addClient(client);
        return convertToDTO(savedClient);
    }

    //POST http://localhost:8088/api/rest/app/clients
    //{
    //    "name": "Vladimir",
    //    "contactPerson": "Anamaria",
    //    "contactEmail": "vladimir@gmail.com",
    //    "description": "Test client 2 for the application."
    //}

    @PutMapping("/{id}")
    public ClientDTO updateClient(@PathVariable Integer id, @RequestBody ClientDTO clientDTO) {
        Client updatedClient = clientService.updateClient(id, convertToEntity(clientDTO));
        return convertToDTO(updatedClient);
    }
    //PUT http://localhost:8088/api/rest/app/clients/2
//    {
//        "name": "Vladimir",
//            "contactPerson": "Stefania",
//            "contactEmail": "vladimir@gmail.com",
//            "description": "Test client 2 for the application. -updated"
//    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable Integer id) {
        return convertToDTO(clientService.getClientById(id));
    }

    //GET http://localhost:8088/api/rest/app/clients/1

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
//GET http://localhost:8088/api/rest/app/clients
    @GetMapping("/search/{name}")
    public List<ClientDTO> searchClientsByName(@PathVariable String name) {
        return clientService.findClientsByName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setClientId(client.getClientId());
        dto.setName(client.getName());
        dto.setContactPerson(client.getContactPerson());
        dto.setContactEmail(client.getContactEmail());
        dto.setDescription(client.getDescription());
        return dto;
    }

    private Client convertToEntity(ClientDTO dto) {
        Client client = new Client();
        client.setClientId(dto.getClientId());
        client.setName(dto.getName());
        client.setContactPerson(dto.getContactPerson());
        client.setContactEmail(dto.getContactEmail());
        client.setDescription(dto.getDescription());
        return client;
    }
}
