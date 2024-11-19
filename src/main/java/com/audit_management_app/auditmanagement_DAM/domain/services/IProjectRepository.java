package com.audit_management_app.auditmanagement_DAM.domain.services;
import com.audit_management_app.auditmanagement_DAM.domain.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByName(String name);

    @Query("SELECT p FROM Project p WHERE p.status = :status")
    List<Project> findByStatus(@Param("status") Project.StatusProiect status);

    Project findByProjectId(int projectId);
}
