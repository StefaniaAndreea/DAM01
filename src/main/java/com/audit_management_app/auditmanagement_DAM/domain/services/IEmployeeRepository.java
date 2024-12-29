package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {

    // Găsește angajați după rol
    List<Employee> findByRole(Employee.EmployeeRole role);

    // Găsește angajați disponibili
    List<Employee> findByIsAvailableTrue();

    // Găsește angajați cu un salariu mai mare decât o valoare specificată
    @Query("SELECT e FROM Employee e WHERE e.salary > :salary")
    List<Employee> findBySalaryGreaterThan(@Param("salary") float salary);

    // Găsește angajați care aparțin unei echipe specifice
    @Query("SELECT e FROM Employee e WHERE e.team.teamId = :teamId")
    List<Employee> findByTeamId(@Param("teamId") int teamId);

    List<Employee> findByIsAvailableFalse();
    @Query("SELECT e FROM Employee e WHERE e.team IS NULL")
    List<Employee> findEmployeesWithoutTeam();

}