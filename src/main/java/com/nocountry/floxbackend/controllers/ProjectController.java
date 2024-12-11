package com.nocountry.floxbackend.controllers;


import com.nocountry.floxbackend.entities.Project;
import com.nocountry.floxbackend.services.ProjectService;
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
    public Project createProject(@RequestBody Project project)
    {
        return projectServ.creteProject(project);
    }

    @GetMapping
    public List<Project> getAllProjects()
    {
        return projectServ.getAllProjects();
    }
}
