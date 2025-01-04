package org.audit.services;

import lombok.extern.slf4j.Slf4j;
import org.audit.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Service
public class ClientService implements IClientService {

    @Value("${backend.url.clients}") // URL-ul pentru endpoint-ul backend-ului
    private String backendUrl;

    private final RestTemplate restTemplate;

    public ClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private static final Logger logger = Logger.getLogger(ClientService.class.getName());

    @Override
    public ClientDTO addClient(ClientDTO client) {
        logger.info("Adding new client: " + client);
        HttpHeaders headers = generateHeaders();
        HttpEntity<ClientDTO> entity = new HttpEntity<>(client, headers);

        ClientDTO savedClient = restTemplate.exchange(
                backendUrl,
                HttpMethod.POST,
                entity,
                ClientDTO.class
        ).getBody();

        logger.info("Added client: " + savedClient);
        return savedClient;
    }

    @Override
    public ClientDTO updateClient(Integer id, ClientDTO client) {
        logger.info("Updating client with ID: " + id);
        HttpHeaders headers = generateHeaders();
        HttpEntity<ClientDTO> entity = new HttpEntity<>(client, headers);

        ClientDTO updatedClient = restTemplate.exchange(
                backendUrl + "/" + id,
                HttpMethod.PUT,
                entity,
                ClientDTO.class
        ).getBody();

        logger.info("Updated client: " + updatedClient);
        return updatedClient;
    }

    @Override
    public ClientDTO getClientById(Integer id) {
        logger.info("Fetching client with ID: " + id);
        ClientDTO client = restTemplate.getForObject(
                backendUrl + "/" + id,
                ClientDTO.class
        );

        logger.info("Fetched client: " + client);
        return client;
    }

    @Override
    public List<ClientDTO> getAllClients() {
        logger.info("Fetching all clients from backend: " + backendUrl);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ClientDTO[] clients = restTemplate.exchange(
                backendUrl,
                HttpMethod.GET,
                entity,
                ClientDTO[].class
        ).getBody();

        return List.of(clients);
    }

    @Override
    public List<ClientDTO> searchClientsByName(String name) {
        logger.info("Searching clients by name: " + name);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ClientDTO[] clients = restTemplate.exchange(
                backendUrl + "/search/" + name,
                HttpMethod.GET,
                entity,
                ClientDTO[].class
        ).getBody();

        return List.of(clients);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}

