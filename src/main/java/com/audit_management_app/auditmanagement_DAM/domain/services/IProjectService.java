package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Task;

import java.util.List;

public interface IProjectService {

    /**
     * Adaugă un proiect nou.
     *
     * @param project Proiectul care trebuie adăugat.
     * @return Proiectul salvat.
     * @throws IllegalArgumentException Dacă clientul sau echipa nu există.
     */
    Project addProject(Project project) throws IllegalArgumentException;

    /**
     * Actualizează un proiect existent.
     *
     * @param project Proiectul actualizat.
     * @return Proiectul salvat după actualizare.
     * @throws IllegalArgumentException Dacă proiectul nu există.
     */
    Project updateProject(Project project) throws IllegalArgumentException;

    /**
     * Șterge un proiect după ID-ul său.
     *
     * @param projectId ID-ul proiectului care trebuie șters.
     * @throws IllegalArgumentException Dacă proiectul nu există.
     */
    void deleteProject(int projectId) throws IllegalArgumentException;

    /**
     * Arhivează un proiect, marcându-l ca "ARCHIVED".
     *
     * @param projectId ID-ul proiectului care trebuie arhivat.
     * @throws IllegalArgumentException Dacă proiectul nu există.
     */
    void archiveProject(int projectId) throws IllegalArgumentException;

    /**
     * Găsește proiecte după status.
     *
     * @param status Statusul proiectelor de căutat.
     * @return Lista proiectelor găsite.
     */
    List<Project> findProjectsByStatus(Project.StatusProiect status);


    List<Project> findProjectsByClient(String clientName) throws IllegalArgumentException;

    public List<Project> findAllProjects();

}

