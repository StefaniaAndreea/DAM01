package org.audit.services;

import org.audit.dto.EmployeeDTO;
import org.audit.dto.EmployeeRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Service
public class EmployeeService implements IEmployeeService {

    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

    @Value("${backend.url.employees}")
    private String backendUrl; // URL-ul pentru endpoint-urile backend-ului

    private final RestTemplate restTemplate;

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public EmployeeDTO addEmployee(EmployeeDTO employee) {
        logger.info("Adding new employee: " + employee);
        HttpHeaders headers = generateHeaders();
        HttpEntity<EmployeeDTO> entity = new HttpEntity<>(employee, headers);

        EmployeeDTO savedEmployee = restTemplate.exchange(
                backendUrl,
                HttpMethod.POST,
                entity,
                EmployeeDTO.class
        ).getBody();

        logger.info("Added employee: " + savedEmployee);
        return savedEmployee;
    }

    @Override
    public EmployeeDTO updateEmployee(Integer id, EmployeeDTO employee) {
        logger.info("Updating employee with ID: " + id);
        HttpHeaders headers = generateHeaders();
        HttpEntity<EmployeeDTO> entity = new HttpEntity<>(employee, headers);

        EmployeeDTO updatedEmployee = restTemplate.exchange(
                backendUrl + "/" + id,
                HttpMethod.PUT,
                entity,
                EmployeeDTO.class
        ).getBody();

        logger.info("Updated employee: " + updatedEmployee);
        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(Integer id) {
        logger.info("Deleting employee with ID: " + id);
        restTemplate.delete(backendUrl + "/" + id);
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {
        logger.info("Fetching employee with ID: " + id);
        return restTemplate.getForObject(
                backendUrl + "/" + id,
                EmployeeDTO.class
        );
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        logger.info("Fetching all employees from backend");
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        EmployeeDTO[] employees = restTemplate.exchange(
                backendUrl,
                HttpMethod.GET,
                entity,
                EmployeeDTO[].class
        ).getBody();

        return List.of(employees);
    }

    @Override
    public List<EmployeeDTO> getAvailableEmployees() {
        logger.info("Fetching available employees");
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        EmployeeDTO[] employees = restTemplate.exchange(
                backendUrl + "/available",
                HttpMethod.GET,
                entity,
                EmployeeDTO[].class
        ).getBody();

        return List.of(employees);
    }

    @Override
    public List<EmployeeDTO> getBusyEmployees() {
        logger.info("Fetching busy employees");
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        EmployeeDTO[] employees = restTemplate.exchange(
                backendUrl + "/busy",
                HttpMethod.GET,
                entity,
                EmployeeDTO[].class
        ).getBody();

        return List.of(employees);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByRole(EmployeeRole role) {
        logger.info("Fetching employees with role: " + role);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        EmployeeDTO[] employees = restTemplate.exchange(
                backendUrl + "/role/" + role.name(), // Folosim `name()` pentru a trimite valoarea enum-ului ca string
                HttpMethod.GET,
                entity,
                EmployeeDTO[].class
        ).getBody();

        return List.of(employees);
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithSalaryGreaterThan(float salary) {
        logger.info("Fetching employees with salary greater than: " + salary);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        EmployeeDTO[] employees = restTemplate.exchange(
                backendUrl + "/salary/greater-than/" + salary,
                HttpMethod.GET,
                entity,
                EmployeeDTO[].class
        ).getBody();

        return List.of(employees);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByTeamId(Integer teamId) {
        logger.info("Fetching employees by team ID: " + teamId);
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        EmployeeDTO[] employees = restTemplate.exchange(
                backendUrl + "/team/" + teamId,
                HttpMethod.GET,
                entity,
                EmployeeDTO[].class
        ).getBody();

        return List.of(employees);
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithoutTeam() {
        logger.info("Fetching employees without team");
        HttpHeaders headers = generateHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        EmployeeDTO[] employees = restTemplate.exchange(
                backendUrl + "/without-team",
                HttpMethod.GET,
                entity,
                EmployeeDTO[].class
        ).getBody();

        return List.of(employees);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
