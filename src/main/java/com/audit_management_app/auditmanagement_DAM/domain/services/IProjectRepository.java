package com.audit_management_app.auditmanagement_DAM.domain.services;

import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Integer> {

    // Găsește proiecte după nume
    List<Project> findByName(String name);

    // Găsește proiecte după status
    @Query("SELECT p FROM Project p WHERE p.status = :status")
    List<Project> findByStatus(@Param("status") Project.StatusProiect status);

    // Găsește un proiect după ID (utilizând Optional)
    Optional<Project> findByProjectId(int projectId);

    // Găsește proiecte asociate unui client, folosind JOIN FETCH pentru a evita problema N+1
    @Query("SELECT p FROM Project p JOIN FETCH p.client c WHERE c.name LIKE %:clientName%")
    List<Project> findProjectsByClientName(@Param("clientName") String clientName);
}