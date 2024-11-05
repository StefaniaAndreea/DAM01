package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
