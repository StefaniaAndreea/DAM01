package com.audit_management_app.auditmanagement_DAM.config;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientRepository;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BD {
        @Autowired
        private IClientRepository clientRepository;

        @Autowired
        private IAuditTeamRepository auditTeamRepository;

        @Bean
        CommandLineRunner seedDatabase() {
            return args -> {
                // Inserăm un Client
                if (clientRepository.count() == 0) {
                    Client client = new Client();
                    client.setName("Client Test");
                    client.setContactPerson("John Doe");
                    client.setContactEmail("john.doe@example.com");
                    client.setDescription("Test client description");
                    clientRepository.save(client);
                    System.out.println("Client inserat cu succes!");
                }

                // Inserăm o echipă de audit
                if (auditTeamRepository.count() == 0) {
                    AuditTeam team = new AuditTeam();
                    team.setTeamName("Team Alpha");
                    auditTeamRepository.save(team);
                    System.out.println("AuditTeam inserată cu succes!");
                }
            };
        }
    }

