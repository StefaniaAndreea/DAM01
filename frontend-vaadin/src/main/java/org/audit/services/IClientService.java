package org.audit.services;

import org.audit.dto.ClientDTO;

import java.util.List;

public interface IClientService {
    ClientDTO addClient(ClientDTO client);
    ClientDTO updateClient(Integer id, ClientDTO client);
    ClientDTO getClientById(Integer id);
    List<ClientDTO> getAllClients();
    List<ClientDTO> searchClientsByName(String name);
}