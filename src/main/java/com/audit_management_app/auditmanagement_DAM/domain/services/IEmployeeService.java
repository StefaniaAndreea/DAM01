package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;

import java.util.List;

public interface IEmployeeService {
    Employee addEmployee(Employee employee);
    Employee updateEmployee(Integer employeeId, Employee employee);
    void deleteEmployee(Integer employeeId);
    Employee getEmployeeById(Integer employeeId);
    List<Employee> getAllEmployees();
    List<Employee> getAvailableEmployees();
    List<Employee> getBusyEmployees();
    List<Employee> getEmployeesByRole(Employee.EmployeeRole role);
    List<Employee> getEmployeesWithSalaryGreaterThan(float salary);
    List<Employee> getEmployeesByTeamId(int teamId);
    List<Employee> getEmployeesWithoutTeam();

}
