package com.nocountry.floxbackend.dtos;

import com.nocountry.floxbackend.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private LocalDateTime dueDate;


    private Long projectId;
    private String projectName;
    private Long assigneeId;
    private String assigneeName;

    // Constructor for creating a new task
    public TaskDTO(String title, String description, TaskStatus taskStatus, LocalDateTime dueDate)
    {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.dueDate = dueDate;
    }
}
