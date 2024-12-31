package com.nocountry.floxbackend.dtos;

import com.nocountry.floxbackend.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
//    public TaskDTO(String title, String description, TaskStatus taskStatus, LocalDateTime dueDate
//    , Long projectId)
//    {
//        this.title = title;
//        this.description = description;
//        this.taskStatus = taskStatus;
//        this.dueDate = dueDate;
//        this.projectId = projectId;
//    }

}
