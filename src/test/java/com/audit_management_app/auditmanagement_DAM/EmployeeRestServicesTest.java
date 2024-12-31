package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.dto.EmployeeDTO;
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
public class EmployeeRestServicesTest {

    private static Integer employeeId; // Variabilă globală și statică
    private static Logger logger = Logger.getLogger(EmployeeRestServicesTest.class.getName());
    private static String BASE_URL = "http://localhost:8088/api/rest/app/employees";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    public void testGetAllEmployees() {
        logger.info("DEBUG: Testing GET all employees ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        List<EmployeeDTO> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                request,
                List.class
        ).getBody();

        assertNotNull(response, "Employees list is null!");
        logger.info("Employees list retrieved: " + response.size() + " employees.");
    }

    @Test
    @Order(2)
    public void testAddEmployee() {
        logger.info("DEBUG: Testing POST add employee ...");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(null);
        employeeDTO.setName("John Doe");
        employeeDTO.setRole("PENTESTER");
        employeeDTO.setSalary(5000.0f);
        employeeDTO.setAvailable(true);
        employeeDTO.setTeamId(1);

        HttpEntity<EmployeeDTO> request = new HttpEntity<>(employeeDTO, headers);

        EmployeeDTO response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                request,
                EmployeeDTO.class
        ).getBody();

        assertNotNull(response, "Failed to add employee!");
        assertNotNull(response.getEmployeeId(), "Employee ID must not be null!");

        employeeId = response.getEmployeeId(); // Setează `employeeId` pentru testele ulterioare
        logger.info("Employee added with ID: " + employeeId);
    }

    @Test
    @Order(3)
    public void testUpdateEmployee() {
        logger.info("DEBUG: Testing PUT update employee ...");
        logger.info("Employee ID: " + employeeId);

        if (employeeId == null) {
            employeeId = 1; // ID-ul unui angajat existent în baza de date
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employeeId);
        employeeDTO.setName("Jane Doe");
        employeeDTO.setRole("PENTESTER");
        employeeDTO.setSalary(6000.0f);
        employeeDTO.setAvailable(false);
        employeeDTO.setTeamId(1);
        HttpEntity<EmployeeDTO> request = new HttpEntity<>(employeeDTO, headers);

        EmployeeDTO response = restTemplate.exchange(
                BASE_URL + "/" + employeeId,
                HttpMethod.PUT,
                request,
                EmployeeDTO.class
        ).getBody();

        assertNotNull(response, "Failed to update employee!");
        assertEquals(employeeId, response.getEmployeeId(), "Employee ID does not match!");
        assertEquals("Jane Doe", response.getName(), "Employee name does not match!");
        logger.info("Employee updated successfully: " + response);
    }

    @Test
    @Order(4)
    public void testGetEmployeeById() {
        logger.info("DEBUG: Testing GET employee by ID ...");

        if (employeeId == null) {
            employeeId = 1; // ID-ul unui angajat existent
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        EmployeeDTO response = restTemplate.exchange(
                BASE_URL + "/" + employeeId,
                HttpMethod.GET,
                request,
                EmployeeDTO.class
        ).getBody();

        assertNotNull(response, "Failed to retrieve employee by ID!");
        assertEquals(employeeId, response.getEmployeeId(), "Employee ID does not match!");
        logger.info("Employee retrieved by ID: " + response);
    }


    @Test
    @Order(5)
    public void testDeleteEmployee() {
        logger.info("DEBUG: Testing DELETE employee ...");

        if (employeeId == null) {
            employeeId = 1; // ID-ul unui angajat existent
        }

        // Asigură-te că angajatul este disponibil înainte de ștergere
        EmployeeDTO employeeDTO = restTemplate.getForObject(BASE_URL + "/" + employeeId, EmployeeDTO.class);
        assertNotNull(employeeDTO, "Employee not found!");
        if (!employeeDTO.isAvailable()) {
            logger.info("Employee is not available. Updating to make it available.");
            employeeDTO.setAvailable(true);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<EmployeeDTO> updateRequest = new HttpEntity<>(employeeDTO, headers);
            restTemplate.exchange(BASE_URL + "/" + employeeId, HttpMethod.PUT, updateRequest, EmployeeDTO.class);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> request = new HttpEntity<>(headers);
        restTemplate.exchange(BASE_URL + "/" + employeeId, HttpMethod.DELETE, request, Void.class);

        logger.info("Employee with ID " + employeeId + " deleted.");
    }
}
