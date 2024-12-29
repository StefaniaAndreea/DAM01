package com.audit_management_app.auditmanagement_DAM.domain.services.impl;

import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeService;
import com.audit_management_app.auditmanagement_DAM.domain.services.ITaskRepository;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private IAuditTeamRepository auditTeamRepository;

    @Autowired
    private ITaskRepository taskRepository;

    @Override
    public Employee addEmployee(Employee employee) {
        employee.setAvailable(true); // Angajații noi sunt disponibili implicit
        return employeeRepository.save(employee);
    }
    @Override
    public Employee updateEmployee(Integer id, Employee updatedEmployee) {
        // Verificăm dacă angajatul există
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + id + " does not exist."));

        // Actualizăm câmpurile angajatului
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setRole(updatedEmployee.getRole());
        existingEmployee.setSalary(updatedEmployee.getSalary());
        existingEmployee.setAvailable(updatedEmployee.isAvailable());

        // Gestionăm echipa
        if (updatedEmployee.getTeam() != null) {
            // Dacă o echipă este specificată, asociem echipa nouă
            Integer teamId = updatedEmployee.getTeam().getTeamId();
            AuditTeam team = auditTeamRepository.findById(teamId)
                    .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " not found."));
            existingEmployee.setTeam(team);
        } else if (updatedEmployee.getTeam() == null && existingEmployee.getTeam() != null) {
            // Dacă nu specificăm echipa și angajatul are deja una, menținem echipa existentă
            // Nu facem nimic, echipa rămâne neschimbată
        }

        // Salvăm modificările
        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        if (!employee.isAvailable()) {
            throw new IllegalArgumentException("Cannot delete an employee who is currently assigned to a project/task.");
        }
        employeeRepository.delete(employee);
    }
    @Override
    public Employee getEmployeeById(Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
    }
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getAvailableEmployees() {
        return employeeRepository.findByIsAvailableTrue();
    }
    @Override
    public List<Employee> getBusyEmployees() {
        return employeeRepository.findByIsAvailableFalse();
    }
    @Override
    public List<Employee> getEmployeesByRole(Employee.EmployeeRole role) {
        return employeeRepository.findByRole(role);
    }

    @Override
    public List<Employee> getEmployeesWithSalaryGreaterThan(float salary) {
        return employeeRepository.findBySalaryGreaterThan(salary);
    }

    @Override
    public List<Employee> getEmployeesByTeamId(int teamId) {
        return employeeRepository.findByTeamId(teamId);
    }
    @Override
    public List<Employee> getEmployeesWithoutTeam() {
        return employeeRepository.findEmployeesWithoutTeam();
    }
}
