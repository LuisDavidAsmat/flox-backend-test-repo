package com.nocountry.floxbackend.services;


import com.nocountry.floxbackend.dtos.TaskDTO;
import com.nocountry.floxbackend.entities.FloxUser;
import com.nocountry.floxbackend.entities.Project;
import com.nocountry.floxbackend.entities.Task;
import com.nocountry.floxbackend.exceptions.ResourceNotFoundException;
import com.nocountry.floxbackend.repositories.FloxUserRepo;
import com.nocountry.floxbackend.repositories.ProjectRepo;
import com.nocountry.floxbackend.repositories.TaskRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService
{
    private final TaskRepo taskRepo;
    private final ModelMapper modelMapper;
    private final ProjectRepo projectRepo;
    private final FloxUserRepo floxUserRepo;
    private final static Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepo taskRepo, ModelMapper modelMapper, ProjectRepo projectRepo, FloxUserRepo floxUserRepo)
    {
        this.taskRepo = taskRepo;
        this.modelMapper = modelMapper;
        this.projectRepo = projectRepo;
        this.floxUserRepo = floxUserRepo;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks()
    {
        List<Task> tasks = taskRepo.findAll();

        return tasks.stream()
                .map(task ->
                        {
                            TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);

                            if (task.getAssignee() != null)
                            {
                                taskDTO.setAssigneeName(task.getAssignee().getUsername());
                            }
                            return taskDTO;
                        }
                )
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);

        if (task.getAssignee() != null)
        {
            taskDTO.setAssigneeName(task.getAssignee().getUsername());
        }
        return taskDTO;
    }

    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);
        Task savedTask = taskRepo.save(task);
        return modelMapper.map(savedTask, TaskDTO.class);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setTaskStatus(taskDTO.getTaskStatus());
        task.setDueDate(taskDTO.getDueDate());

        Long projectId = taskDTO.getProjectId();
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("project not found"));
        task.setProject(project);
//        projectRepo.save(project);

        Long floxUserId = taskDTO.getAssigneeId();
        FloxUser assignee = floxUserRepo.findById(floxUserId)
                .orElseThrow(() -> new ResourceNotFoundException("project not found"));

        if (assignee != null)
        {
            logger.info("assinge was not vnull\n\n {}", taskDTO.getProjectId());
        }
        task.setAssignee(assignee);
        floxUserRepo.save(assignee);

        Task updatedTask = taskRepo.save(task);

        TaskDTO updatedTaskDTO = modelMapper.map(updatedTask, TaskDTO.class);

        if (task.getAssignee() != null)
        {
            updatedTaskDTO.setAssigneeName(task.getAssignee().getUsername());
        }

        return updatedTaskDTO;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTask(@PathVariable Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
        taskRepo.delete(task);
    }
}
