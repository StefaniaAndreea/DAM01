package org.audit.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Integer employeeId;
    private String name;
    private EmployeeRole role;
    private float salary;
    private boolean isAvailable;
    private Integer teamId;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public EmployeeDTO(Integer employeeId, String name, EmployeeRole role, boolean isAvailable, float salary, Integer teamId) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.isAvailable = isAvailable;
        this.salary = salary;
        this.teamId = teamId;
    }

    public EmployeeDTO() {
    }
}
