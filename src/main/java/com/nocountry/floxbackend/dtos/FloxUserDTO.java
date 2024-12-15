package com.nocountry.floxbackend.dtos;

import com.nocountry.floxbackend.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

public class FloxUserDTO {

    private Long userId;
    private String username;
    private String email;
    private UserRole userRole;
    // Consider adding more fields based on specific use cases, such as:
     private Set<ProjectDTO> createdProjects;
     private Set<TaskDTO> assignedTasks;
     private Set<CommentDTO> comments;
}
