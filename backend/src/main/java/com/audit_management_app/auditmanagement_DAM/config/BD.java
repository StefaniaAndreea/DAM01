package com.audit_management_app.auditmanagement_DAM.config;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Client;
import com.audit_management_app.auditmanagement_DAM.domain.services.IAuditTeamRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IClientRepository;
import com.audit_management_app.auditmanagement_DAM.domain.services.IEmployeeRepository;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;
import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.Employee;
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

    @Autowired
    private IEmployeeRepository employeeRepository;
    @Bean
        CommandLineRunner seedDatabase() {
            return args -> {
                // Inserăm un Client
                if (clientRepository.count() == 0) {
                    Client client = new Client();
                    client.setName("Rotariu Anamaria");
                    client.setContactPerson("Vavilin Vladiminr");
                    client.setContactEmail("anamaria@example.com");
                    client.setDescription("Student pasionat devenit client");
                    clientRepository.save(client);
                    System.out.println("Client inserat cu succes!");
                }

                // Inserăm o echipă de audit
                if (auditTeamRepository.count() == 0) {
                    AuditTeam team = new AuditTeam();
                    team.setTeamName("Team SIA");
                    auditTeamRepository.save(team);
                    System.out.println("AuditTeam inserată cu succes!");
                }
                // Inserăm un angajat
                if (employeeRepository.count() == 0) {
                    Employee employee = new Employee();
                    employee.setName("Sava Ștefania");
                    employee.setRole(Employee.EmployeeRole.PENTESTER);
                    employee.setSalary(4500);
                    employee.setAvailable(true);
                    employeeRepository.save(employee);
                    System.out.println("Angajat inserat cu succes!");
                }
            };
        }
    }

