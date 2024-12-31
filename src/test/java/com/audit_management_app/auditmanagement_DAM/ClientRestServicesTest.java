package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.dto.ClientDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientRestServicesTest {

    private static Integer clientId; // Variabilă globală și statică
    private static Logger logger = Logger.getLogger(ClientRestServicesTest.class.getName());
    private static String BASE_URL = "http://localhost:8088/api/rest/app/clients";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    public void testGetAllClients() {
        logger.info("DEBUG: Testing GET all clients ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<ClientDTO> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Clients list is null!");
        logger.info("Clients list retrieved: " + response.size() + " clients.");
    }

    @Test
    @Order(2)
    public void testAddClient() {
        logger.info("DEBUG: Testing POST add client ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientId(null);
        clientDTO.setName("Test Client");
        clientDTO.setContactPerson("Contact Person");
        clientDTO.setContactEmail("test@example.com");
        clientDTO.setDescription("Test Description");

        HttpEntity<ClientDTO> request = new HttpEntity<>(clientDTO, headers);

        ClientDTO response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                request,
                ClientDTO.class
        ).getBody();

        assertNotNull(response, "Failed to add client!");
        assertNotNull(response.getClientId(), "Client ID must not be null!");

        clientId = response.getClientId(); // Setează `clientId` pentru testele ulterioare
        logger.info("Client added with ID: " + clientId);
    }

    @Test
    @Order(3)
    public void testUpdateClient() {
        logger.info("DEBUG: Testing PUT update client ...");
        logger.info("Client ID: " + clientId);

        // Setăm direct un clientId existent pentru acest test
        if (clientId == null) {
            clientId = 1; // ID-ul clientului existent în baza de date
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientId(clientId);
        clientDTO.setName("Updated Client");
        clientDTO.setContactPerson("Updated Contact");
        clientDTO.setContactEmail("updated@example.com");
        clientDTO.setDescription("Updated Description");
        HttpEntity<ClientDTO> request = new HttpEntity<>(clientDTO, headers);

        ClientDTO response = restTemplate.exchange(
                BASE_URL + "/" + clientId,
                HttpMethod.PUT,
                request,
                ClientDTO.class
        ).getBody();

        assertNotNull(response, "Failed to update client!");
        assertEquals(clientId, response.getClientId(), "Client ID does not match!");
        assertEquals("Updated Client", response.getName(), "Client name does not match!");
        logger.info("Client updated successfully: " + response);
    }

    @Test
    @Order(4)
    public void testGetClientById() {
        logger.info("DEBUG: Testing GET client by ID ...");

        // Verificăm dacă clientId este setat, altfel folosim un ID fix pentru test
        if (clientId == null) {
            clientId = 1; // ID-ul unui client existent
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ClientDTO response = restTemplate.exchange(
                BASE_URL + "/" + clientId,
                HttpMethod.GET,
                request,
                ClientDTO.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve client by ID!");
        assertEquals(clientId, response.getClientId(), "Client ID does not match!");
        logger.info("Client retrieved by ID: " + response);
    }

    @Test
    @Order(5)
    public void testSearchClientsByName() {
        logger.info("DEBUG: Testing GET clients by name ...");

        String searchName = "Test Client";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<ClientDTO> response = restTemplate.exchange(
                BASE_URL + "/search/" + searchName,
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve clients by name!");
        logger.info("Clients with name '" + searchName + "': " + response.size());
    }
}
