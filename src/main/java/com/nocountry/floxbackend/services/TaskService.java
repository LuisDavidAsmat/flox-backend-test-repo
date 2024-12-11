package com.nocountry.floxbackend.services;


import com.nocountry.floxbackend.entities.Task;
import com.nocountry.floxbackend.repositories.TaskRepo;
import org.springframework.stereotype.Service;

@Service
public class TaskService
{
    private final TaskRepo taskRepo;


    public TaskService(TaskRepo taskRepo)
    {
        this.taskRepo = taskRepo;
    }
}
