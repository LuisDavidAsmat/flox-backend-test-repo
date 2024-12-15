package com.nocountry.floxbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO
{
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double budget;
    private Double completionPercentage;
    private String creatorName;
    private Set<String> tasks;
    private Set<String> members;
//    private FloxUserDTO creator;
//    private Set<TaskDTO> tasks;
//    private Set<FloxUserDTO> members;
}
