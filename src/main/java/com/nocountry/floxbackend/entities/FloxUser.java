package com.nocountry.floxbackend.entities;


import com.nocountry.floxbackend.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="flox_users")
@Getter
@Setter
@NoArgsConstructor
public class FloxUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "creator")
    private List<Project> createdProjects;

    @OneToMany(mappedBy = "assignee")
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;
}