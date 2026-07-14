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
        // Dacă angajatul are o echipă specificată, îl adăugăm în lista members
        if (employee.getTeam() != null) {
            AuditTeam team = auditTeamRepository.findById(employee.getTeam().getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + employee.getTeam().getTeamId()));
            team.getMembers().add(employee);
        }
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
            AuditTeam newTeam = auditTeamRepository.findById(teamId)
                    .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " not found."));

            // Dacă angajatul are deja o echipă, îl eliminăm din lista acelei echipe
            if (existingEmployee.getTeam() != null && !existingEmployee.getTeam().equals(newTeam)) {
                existingEmployee.getTeam().getMembers().remove(existingEmployee);
            }

            // Adăugăm angajatul în noua echipă
            newTeam.getMembers().add(existingEmployee);
            existingEmployee.setTeam(newTeam);
        } else if (updatedEmployee.getTeam() == null && existingEmployee.getTeam() != null) {
            // Dacă nu specificăm echipa și angajatul are deja una, îl eliminăm din lista acelei echipe
            existingEmployee.getTeam().getMembers().remove(existingEmployee);
            existingEmployee.setTeam(null); // Resetăm echipa
        }

        // Salvăm modificările
        return employeeRepository.save(existingEmployee);
    }
    @Override
    public void updateEmployeeAvailability(Integer employeeId, boolean isAvailable) {
        Employee employee = getEmployeeById(employeeId);
        employee.setAvailable(isAvailable);
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        if (!employee.isAvailable()) {
            throw new IllegalArgumentException("Cannot delete an employee who is currently assigned to a project/task.");
        }

        // Eliminăm angajatul din echipa sa, dacă există
        if (employee.getTeam() != null) {
            employee.getTeam().getMembers().remove(employee);
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
