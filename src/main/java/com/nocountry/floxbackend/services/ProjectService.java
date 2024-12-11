package com.nocountry.floxbackend.services;


import com.nocountry.floxbackend.entities.Project;
import com.nocountry.floxbackend.repositories.ProjectRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService
{
    private final ProjectRepo projectRepo;

    public ProjectService(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    public Project creteProject(Project project)
    {
        return projectRepo.save(project);
    }

    public List<Project> getAllProjects ()
    {
        return projectRepo.findAll();
    }

    public Optional<Project> getProjectById(Long id)
    {
        return projectRepo.findById(id);
    }

    public Project updateProject (Long id, Project projectNewInfo)
    {
        Project project = projectRepo.findById(id)
                .orElseThrow( () -> new RuntimeException("Project was not found with id: " + id));

        project.setName(projectNewInfo.getName());
        project.setDescription(projectNewInfo.getDescription());
        project.setStartDate(projectNewInfo.getStartDate());
        project.setEndDate(projectNewInfo.getEndDate());
        project.setBudget(projectNewInfo.getBudget());
        project.setCompletionPercentage(projectNewInfo.getCompletionPercentage());
        project.setCreator(projectNewInfo.getCreator());
        project.setMembers(projectNewInfo.getMembers());
        project.setTasks(projectNewInfo.getTasks());

        return projectRepo.save(project);
    }

    public void deleteProject (Long id)
    {
        Project project = projectRepo.findById(id)
                .orElseThrow( () -> new RuntimeException("Project was not found with id: " + id));

        projectRepo.delete(project);
    }

}
