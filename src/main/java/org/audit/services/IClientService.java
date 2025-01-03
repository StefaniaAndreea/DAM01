package org.audit.services;

import org.audit.dto.ClientDTO;

import java.util.List;

public interface IClientService {
    List<ClientDTO> getAllClients();
}
