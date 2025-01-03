package org.audit.services;

import org.audit.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ClientService implements IClientService {

    @Value("${backend.url.clients}") // URL-ul pentru endpoint-ul backend-ului
    private String backendUrl;

    private final RestTemplate restTemplate;

    public ClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ClientDTO> getAllClients() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ClientDTO[] clients = restTemplate.exchange(
                backendUrl,
                HttpMethod.GET,
                entity,
                ClientDTO[].class
        ).getBody();

        return List.of(clients);
    }
}
