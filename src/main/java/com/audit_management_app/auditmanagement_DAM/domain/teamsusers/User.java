package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private Employee employee;

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String getPermissions() {
        return switch (this.role) {
            case ADMINISTRATOR -> "Full Access";
            case MANAGER -> "Manager Access";
            case PENTESTER -> "Limited Access";
        };
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public enum Role {
        ADMINISTRATOR, MANAGER, PENTESTER
    }

    public User(int userId, String username, String password, Role role, Employee employee) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.employee = employee;
    }

    public int getUserId() {
        return userId;
    }
}
