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
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int userId;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

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


    public enum UserRole {
        ADMINISTRATOR, MANAGER, PENTESTER
    }

}
