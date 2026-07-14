package org.audit.services;

import org.audit.dto.EmployeeDTO;
import org.audit.dto.EmployeeRole;

import java.util.List;

public interface IEmployeeService {
    EmployeeDTO addEmployee(EmployeeDTO employee);
    EmployeeDTO updateEmployee(Integer id, EmployeeDTO employee);
    void deleteEmployee(Integer id);
    EmployeeDTO getEmployeeById(Integer id);
    List<EmployeeDTO> getAllEmployees();
    List<EmployeeDTO> getAvailableEmployees();
    List<EmployeeDTO> getBusyEmployees();
    List<EmployeeDTO> getEmployeesByRole(EmployeeRole role);
    List<EmployeeDTO> getEmployeesWithSalaryGreaterThan(float salary);
    List<EmployeeDTO> getEmployeesByTeamId(Integer teamId);
    List<EmployeeDTO> getEmployeesWithoutTeam();
    void updateEmployeeAvailability(Integer employeeId, boolean isAvailable);

}
