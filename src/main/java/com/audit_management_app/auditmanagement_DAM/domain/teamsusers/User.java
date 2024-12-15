package com.audit_management_app.auditmanagement_DAM.domain.teamsusers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
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

    public enum UserRole {
        ADMINISTRATOR("Full Access"),
        MANAGER("Manager Access"),
        PENTESTER("Limited Access");

        private final String permissions;

        UserRole(String permissions) {
            this.permissions = permissions;
        }

        public String getPermissions() {
            return permissions;
        }
    }

    public User(String username, String password, UserRole role, Employee employee) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.employee = employee;
    }
}