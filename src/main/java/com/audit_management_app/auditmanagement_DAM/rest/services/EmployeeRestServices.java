package com.audit_management_app.auditmanagement_DAM.rest.services;

import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeService;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import com.audit_management_app.auditmanagement_DAM.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/app/employees")
public class EmployeeRestServices {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IAuditTeamRepository auditTeamRepository;

    @PostMapping
    public EmployeeDTO addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeService.addEmployee(employee);
        return convertToDTO(savedEmployee);
    }

    // POST http://localhost:8088/api/rest/app/employees
    // {
    //    "name": "Vavilin Vladimir",
    //    "role": "PENTESTER",
    //    "salary": 4500.0,
    //    "available": true,
    //    "team":1
    //}

    @PutMapping("/{id}")
    public EmployeeDTO updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return convertToDTO(updatedEmployee);
    }
// PUT http://localhost:8088/api/rest/app/employees/{id}
//{
//    "name": "Vavilin Vladimir",
//        "role": "PENTESTER",
//        "salary": 7500.0,
//        "available": true,
//        "team":1
//}


    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return "Employee with ID " + id + " has been deleted.";
    }
// DELETE http://localhost:8088/api/rest/app/employees/{id}


    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Integer id) {
        return convertToDTO(employeeService.getEmployeeById(id));
    }
// GET http://localhost:8088/api/rest/app/employees/{id}
    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // GET http://localhost:8088/api/rest/app/employees

    @GetMapping("/available")
    public List<EmployeeDTO> getAvailableEmployees() {
        return employeeService.getAvailableEmployees().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    //GET http://localhost:8088/api/rest/app/employees/available

    @GetMapping("/busy")
    public List<EmployeeDTO> getBusyEmployees() {
        return employeeService.getBusyEmployees().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    //GET http://localhost:8088/api/rest/app/employees/busy

    @GetMapping("/role/{role}")
    public List<EmployeeDTO> getEmployeesByRole(@PathVariable String role) {
        return employeeService.getEmployeesByRole(Employee.EmployeeRole.valueOf(role))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //GET http://localhost:8088/api/rest/app/employees/role/PENTESTER
    @GetMapping("/salary/greater-than/{salary}")
    public List<EmployeeDTO> getEmployeesWithSalaryGreaterThan(@PathVariable float salary) {
        return employeeService.getEmployeesWithSalaryGreaterThan(salary)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
// GET http://localhost:8088/api/rest/app/employees/salary/greater-than/6000

    @GetMapping("/team/{teamId}")
    public List<EmployeeDTO> getEmployeesByTeamId(@PathVariable int teamId) {
        return employeeService.getEmployeesByTeamId(teamId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // http://localhost:8088/api/rest/app/employees/team/1

    @GetMapping("/without-team")
    public List<EmployeeDTO> getEmployeesWithoutTeam() {
        return employeeService.getEmployeesWithoutTeam().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // GET  http://localhost:8088/api/rest/app/employees/without-team


    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setName(employee.getName());
        dto.setRole(employee.getRole().name());
        dto.setSalary(employee.getSalary());
        dto.setAvailable(employee.isAvailable());
        if (employee.getTeam() != null) {
            dto.setTeamId(employee.getTeam().getTeamId());
        }
        return dto;
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setName(dto.getName());
        employee.setRole(Employee.EmployeeRole.valueOf(dto.getRole()));
        employee.setSalary(dto.getSalary());
        employee.setAvailable(dto.isAvailable());
        if (dto.getTeamId() != null) {
            AuditTeam team = auditTeamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team with ID " + dto.getTeamId() + " not found."));
            employee.setTeam(team);
        }
        return employee;
    }
}
