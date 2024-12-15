package com.nocountry.floxbackend.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class CommentDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long taskId;
    private String taskName;
    private Long authorId;
    private String authorUsername; // Optional: For displaying author's username
}
