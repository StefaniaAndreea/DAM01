package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.teamsusers.AuditTeam;

import java.util.List;

public interface IAuditTeamService {
    // Adaugă o echipă nouă
    AuditTeam addTeam(String teamName, List<Integer> memberIds);

    // Actualizează detaliile unei echipe
    AuditTeam updateTeam(Integer teamId, String newName, List<Integer> memberIds);

    // Șterge o echipă existentă
    void deleteTeam(Integer teamId);

    // Returnează detalii despre o echipă pe baza ID-ului
    AuditTeam getTeamById(Integer teamId);

    // Returnează toate echipele
    List<AuditTeam> getAllTeams();

    // Adaugă un membru într-o echipă
    void addMemberToTeam(Integer teamId, Integer employeeId);

//    // Elimină un membru dintr-o echipă
//    void removeMemberFromTeam(Integer teamId, Integer employeeId);

    // Returnează echipele care nu sunt asignate niciunui proiect
    List<AuditTeam> getTeamsWithoutProjects();

}
