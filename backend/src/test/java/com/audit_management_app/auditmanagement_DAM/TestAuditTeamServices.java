package com.audit_management_app.auditmanagement_DAM;

import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.impl.AuditTeamServiceImpl;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TestAuditTeamServices {
    @Autowired
    private AuditTeamServiceImpl auditTeamService;

    @Autowired
    private IAuditTeamRepository auditTeamRepository;

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Test
    void testAddTeam() {
        // Arrange
        String teamName = "Team A";
        Employee employee1 = new Employee();
        employee1.setName("Employee 1");
        employee1.setAvailable(true);
        employee1.setRole(Employee.EmployeeRole.PENTESTER);
        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setName("Employee 2");
        employee2.setAvailable(true);
        employee2.setRole(Employee.EmployeeRole.MANAGER);
        employeeRepository.save(employee2);

        List<Integer> memberIds = List.of(employee1.getEmployeeId(), employee2.getEmployeeId());

        // Act
        AuditTeam createdTeam = auditTeamService.addTeam(teamName, memberIds);

        // Assert
        assertNotNull(createdTeam);
        assertEquals("Team A", createdTeam.getTeamName());
        assertEquals(2, createdTeam.getMembers().size());
    }
    @Test
    void testDeleteTeam() {
        // Arrange
        AuditTeam team = new AuditTeam();
        team.setTeamName("Team B");
        auditTeamRepository.save(team);

        // Act
        auditTeamService.deleteTeam(team.getTeamId());

        // Assert
        assertFalse(auditTeamRepository.findById(team.getTeamId()).isPresent());
    }

    @Test
    void testAddMemberToTeam() {
        // Arrange
        AuditTeam team = new AuditTeam();
        team.setTeamName("Team C");
        auditTeamRepository.save(team);

        Employee employee = new Employee();
        employee.setName("Employee 3");
        employee.setAvailable(true);
        employee.setRole(Employee.EmployeeRole.MANAGER);
        employeeRepository.save(employee);

        // Act
        auditTeamService.addMemberToTeam(team.getTeamId(), employee.getEmployeeId());

        // Assert
        AuditTeam updatedTeam = auditTeamRepository.findById(team.getTeamId()).orElseThrow();
        assertEquals(1, updatedTeam.getMembers().size());
        assertEquals(team, employee.getTeam());
    }

}
