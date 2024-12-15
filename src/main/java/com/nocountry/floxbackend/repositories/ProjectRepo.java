package com.nocountry.floxbackend.repositories;

import com.nocountry.floxbackend.entities.Project;
import com.nocountry.floxbackend.entities.ProjectProjection;
import jakarta.validation.OverridesAttribute;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface ProjectRepo extends JpaRepository<Project, Long>
{

//    @EntityGraph(attributePaths = {"creator", "tasks", "members"})
//    List<Project> findAll();

//    @EntityGraph(attributePaths = {"creator", "tasks", "members"})
//    Optional<Project> findById(Long id);

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.creator " +
            "LEFT JOIN FETCH p.tasks " +
            "LEFT JOIN FETCH p.members")
    List<Project> findAllProjectsWithAssociations();

    @Query("SELECT p.id AS id, " +
            "p.name AS name, " +
            "p.description AS description, " +
            "p.startDate AS startDate, " +
            "p.endDate AS endDate, " +
            "p.budget AS budget, " +
            "p.completionPercentage AS completionPercentage, " +
            "u.username AS creatorName " +
            "FROM Project p "+
            "LEFT JOIN p.creator u ")
    List<ProjectProjection> findAllProjected();

    @Query("SELECT p.id AS id, " +
            "p.name AS name, " +
            "p.description AS description, " +
            "p.startDate AS startDate, " +
            "p.endDate AS endDate, " +
            "p.budget AS budget, " +
            "p.completionPercentage AS completionPercentage, " +
            "u.username AS creatorName " +
            "FROM Project p "+
            "LEFT JOIN p.creator u " +
            "WHERE p.id = :projectId")
    Optional<ProjectProjection> findProjectProjectionById(@Param("projectId") Long projectId);

    @Query("SELECT t.title FROM Task t WHERE t.project.id = :projectId")
    Set<String> findTaskTitlesProjectId (Long projectId);

    @Query("SELECT m.username FROM FloxUser m JOIN m.projects p WHERE p.id = :projectId")
    Set<String> findMemberUsernameByProjectId(Long projectId);

}
