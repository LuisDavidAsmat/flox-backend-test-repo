package com.nocountry.floxbackend.services;


import com.nocountry.floxbackend.dtos.FloxUserDTO;
import com.nocountry.floxbackend.dtos.ProjectDTO;
import com.nocountry.floxbackend.dtos.ProjectDTO2;
import com.nocountry.floxbackend.dtos.TaskDTO;
import com.nocountry.floxbackend.entities.*;
import com.nocountry.floxbackend.repositories.FloxUserRepo;
import com.nocountry.floxbackend.repositories.ProjectRepo;
import com.nocountry.floxbackend.repositories.TaskRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService
{
    private final ProjectRepo projectRepo;
    private final ModelMapper modelMapper;
    private final TaskRepo taskRepo;
    private final FloxUserRepo floxUserRepo;
    private final static Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public ProjectService(ProjectRepo projectRepo, ModelMapper modelMapper,
                          TaskRepo taskRepo, FloxUserRepo floxUserRepo)
    {
        this.projectRepo = projectRepo;
        this.modelMapper = modelMapper;
        this.taskRepo = taskRepo;
        this.floxUserRepo = floxUserRepo;
    }

    public Project createProjectWithInitialTasks(Project project)
    {
        Project savedProject  = projectRepo.save(project);

        project.getTasks()
                .forEach(
                        task ->
                        {
                            task.setProject(project);
                            task.setAssignee(project.getCreator());

                            try
                            {
                                taskRepo.save(task);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });

        return savedProject ;
    }

    public List<ProjectDTO> getAllProjection()
    {
        List<ProjectProjection> projects = projectRepo.findAllProjected();

        return projects.stream().map(
                project ->
                {
                    Set<String> tasks = projectRepo.findTaskTitlesProjectId(project.getId());
                    Set<String> members = projectRepo.findMemberUsernameByProjectId(project.getId());

                    return new ProjectDTO(
                            project.getId(),
                            project.getName(),
                            project.getDescription(),
                            project.getStartDate(),
                            project.getEndDate(),
                            project.getBudget(),
                            project.getCompletionPercentage(),
                            project.getCreatorName(),
                            tasks,
                            members
                    );

                }).toList();
    }

    // not used
    public List<ProjectDTO> getAllProjects ()
    {
        List<Project> projects = projectRepo.findAllProjectsWithAssociations();

        return projects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProjectDTO convertToDto(Project project)
    {
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        projectDTO.setCreatorName(project.getCreator().getUsername());

        projectDTO.setTasks(project.getTasks().stream()
                .map(Task::getTitle)
                .collect(Collectors.toSet()));

        Set<String> memberUsernames = project.getMembers().stream()
                .map(FloxUser::getUsername)
                .collect(Collectors.toSet());

        projectDTO.setMembers(memberUsernames);

        return projectDTO;
    }

    public Optional<Project> getProjectById(Long id)
    {
        return projectRepo.findById(id);
    }

    public ProjectDTO updateProject (Long id, Project projectNewInfo) {

        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found for this id :: " + id));

        project.setName(projectNewInfo.getName());
        project.setDescription(projectNewInfo.getDescription());
        project.setStartDate(projectNewInfo.getStartDate());
        project.setEndDate(projectNewInfo.getEndDate());
        project.setBudget(projectNewInfo.getBudget());
        project.setCompletionPercentage(projectNewInfo.getCompletionPercentage());
        project.setCreator(projectNewInfo.getCreator());
        project.setMembers(projectNewInfo.getMembers());
        project.setTasks(projectNewInfo.getTasks());

        Project updatedProject = projectRepo.save(project);


        Set<String> tasks = projectRepo.findTaskTitlesProjectId(id);
        Set<String> members = projectRepo.findMemberUsernameByProjectId(id);

        return new ProjectDTO(
                updatedProject.getId(),
                updatedProject.getName(),
                updatedProject.getDescription(),
                updatedProject.getStartDate(),
                updatedProject.getEndDate(),
                updatedProject.getBudget(),
                updatedProject.getCompletionPercentage(),
                project.getCreator().getUsername(),
                tasks,
                members
        );
    }

    public void deleteProject (Long id)
    {
        Project project = projectRepo.findById(id)
                .orElseThrow( () -> new RuntimeException("Project was not found with id: " + id));

        projectRepo.delete(project);
    }

    public ProjectDTO findProjectById(Long id)
    {
        //Optional<Project> optionalProject = projectRepo.findById(id);
        Optional<ProjectProjection> projectProjection = projectRepo.findProjectProjectionById(id);

        if (projectProjection.isPresent())
        {
            ProjectProjection project = projectProjection.get();

            Set<String> tasks = projectRepo.findTaskTitlesProjectId(id);
            Set<String> members = projectRepo.findMemberUsernameByProjectId(id);

            return new ProjectDTO(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getBudget(),
                    project.getCompletionPercentage(),
                    project.getCreatorName(),
                    tasks,
                    members
            );
        }

        return null;
    }

    public ProjectDTO2 findProjectById2(Long id)
    {
        //Optional<Project> optionalProject = projectRepo.findById(id);
        Optional<Project> projectProjection = projectRepo.findProjectWithAssociations(id);

        if (projectProjection.isPresent())
        {
            Project project = projectProjection.get();

            FloxUserDTO floxUserDTO = new FloxUserDTO();

            floxUserDTO.setUserId(project.getCreator().getUserId());
            floxUserDTO.setUsername(project.getCreator().getUsername());
            floxUserDTO.setEmail(project.getCreator().getEmail());
            floxUserDTO.setUserRole(project.getCreator().getUserRole());

            Set<TaskDTO> taskDTOS = project.getTasks().stream()
                    .map(task -> new TaskDTO(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getTaskStatus(),
                            task.getDueDate(),
                            task.getProject().getId(),
                            task.getProject().getName(),
                            task.getAssignee().getUserId(),
                            task.getAssignee().getUsername()
                    ))
                    .collect(Collectors.toSet());

            Set<FloxUserDTO> members =  project.getMembers().stream()
                    .map(
                            member -> new FloxUserDTO(
                                    member.getUserId(),
                                    member.getUsername(),
                                    member.getEmail(),
                                    member.getUserRole()
                            ))
                    .collect(Collectors.toSet());


            return new ProjectDTO2(
                    project.getId(),
                    project.getName(),
                    project.getDescription(),
                    project.getStartDate(),
                    project.getEndDate(),
                    project.getBudget(),
                    project.getCompletionPercentage(),
                    floxUserDTO,
//                    project.getCreator().getUserId(),
//                    project.getCreator().getUsername(),
                    taskDTOS,
                    members

            );
        }

        return null;
    }



    public List<ProjectDTO> findProjectsByUsersEmail(String userEmail)
    {
        FloxUser floxUser = floxUserRepo.findByEmail(userEmail);

        if (floxUser == null)
        {
            return null;
        }


        List<Project> projectsbyUser = projectRepo.findProjectByUser(floxUser);

       // return projectsbyUser;

        return projectsbyUser.stream().map(
                project ->
                {
                    Set<String> tasks = projectRepo.findTaskTitlesProjectId(project.getId());
                    Set<String> members = projectRepo.findMemberUsernameByProjectId(project.getId());

                    return new ProjectDTO(
                            project.getId(),
                            project.getName(),
                            project.getDescription(),
                            project.getStartDate(),
                            project.getEndDate(),
                            project.getBudget(),
                            project.getCompletionPercentage(),
                            project.getCreator().getUsername(),
                            tasks,
                            members
                    );

                }).toList();

    }
}
