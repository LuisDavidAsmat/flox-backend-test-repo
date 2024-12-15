package com.nocountry.floxbackend.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.Set;

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

    private Set<Project> createdProjects;

    @ManyToMany(mappedBy = "members")

    private Set<Project> projects;

    @OneToMany(mappedBy = "assignee")

    private Set<Task> assignedTasks;

    @OneToMany(mappedBy = "author")

    private Set<Comment> comments;
}