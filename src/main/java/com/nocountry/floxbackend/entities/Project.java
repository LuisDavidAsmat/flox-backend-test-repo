package com.nocountry.floxbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotNull
    @PastOrPresent
    private LocalDateTime startDate;

    @NotNull
    @Future(message = "End date must be after start date.")
    //@PastOrPresent
    private LocalDateTime endDate;

    @NotNull
    private Double budget;

    @NotNull
    @Min(0)
    @Max(100)
    private Double completionPercentage;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private FloxUser creator;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<FloxUser> members;
}
