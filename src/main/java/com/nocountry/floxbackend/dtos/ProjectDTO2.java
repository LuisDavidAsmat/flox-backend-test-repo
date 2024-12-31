package com.nocountry.floxbackend.dtos;

import com.nocountry.floxbackend.entities.FloxUser;
import com.nocountry.floxbackend.entities.Task;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO2
{
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double budget;
    private Double completionPercentage;
    private FloxUserDTO creator;
//    private Long creatorId;
//    private String creatorName;
    private Set<TaskDTO> tasks;
    private Set<FloxUserDTO> members;



//    private FloxUserDTO creator;
//    private Set<TaskDTO> tasks;
//    private Set<FloxUserDTO> members;
}
