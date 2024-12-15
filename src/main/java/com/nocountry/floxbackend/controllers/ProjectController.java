package com.nocountry.floxbackend.controllers;


import com.nocountry.floxbackend.dtos.ProjectDTO;
import com.nocountry.floxbackend.entities.Project;
import com.nocountry.floxbackend.entities.ProjectProjection;
import com.nocountry.floxbackend.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController
{
    private final ProjectService projectServ;

    public ProjectController(ProjectService projectServ) {
        this.projectServ = projectServ;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createProjectWithTasks(@Valid @RequestBody Project project)
    {
        try
        {
            Project createdProject = projectServ.createProjectWithInitialTasks(project);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating project: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects()
    {
        try
        {
            List<ProjectDTO> projects = projectServ.getAllProjection();

            if (projects.isEmpty())
            {
                System.out.println("pojects is empty!!");
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(projects);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject (@PathVariable Long id,
                                                  @RequestBody Project projectDetails )
    {
        try
        {
            ProjectDTO updatedProject = projectServ.updateProject(id,projectDetails);

            if (updatedProject == null)
            {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(updatedProject);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject (@PathVariable Long id)
    {
        try
        {
            projectServ.deleteProject(id);

            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.noContent().build();
        }
    }


}
