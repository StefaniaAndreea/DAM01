package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.impl.EmployeeServiceImpl;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TestFindEmployees {
    private static final Logger logger = LoggerFactory.getLogger(testProjectCOMPLETED.class);

    @Autowired
    private EmployeeServiceImpl employeeService;
    @Autowired
    private IEmployeeRepository employeeRepository;
    @Autowired
    private IAuditTeamRepository teamRepository;

    @Test
    public void testGetEmployeesByRole() {
        employeeRepository.deleteAll();
        Employee employee1 = new Employee("John Doe", null, Employee.EmployeeRole.PENTESTER, 4500.0f, true);
        Employee employee2 = new Employee("Jane Doe", null, Employee.EmployeeRole.MANAGER, 5500.0f, true);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> pentesters = employeeService.getEmployeesByRole(Employee.EmployeeRole.PENTESTER);
        assertEquals(1, pentesters.size());
        assertEquals("John Doe", pentesters.get(0).getName());
        logger.info("Employees found by role: " + pentesters);

    }

    @Test
    public void testGetEmployeesWithSalaryGreaterThan() {
        Employee employee1 = new Employee("John Doe", null, Employee.EmployeeRole.PENTESTER, 4500.0f, true);
        Employee employee2 = new Employee("Jane Doe", null, Employee.EmployeeRole.MANAGER, 5500.0f, true);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> highSalaryEmployees = employeeService.getEmployeesWithSalaryGreaterThan(5000.0f);
        assertEquals(1, highSalaryEmployees.size());
        assertEquals("Jane Doe", highSalaryEmployees.get(0).getName());
        logger.info("Employees found by role: " + highSalaryEmployees);

    }

    @Test
    public void testGetEmployeesByTeamId() {
        AuditTeam team = new AuditTeam("Team 1");
        teamRepository.save(team);

        Employee employee1 = new Employee("John Doe", team, Employee.EmployeeRole.PENTESTER, 4500.0f, true);
        Employee employee2 = new Employee("Jane Doe", null, Employee.EmployeeRole.MANAGER, 5500.0f, true);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        List<Employee> teamEmployees = employeeService.getEmployeesByTeamId(team.getTeamId());
        assertEquals(1, teamEmployees.size());
        assertEquals("John Doe", teamEmployees.get(0).getName());
        logger.info("Employees found by role: " + teamEmployees);

    }

}
