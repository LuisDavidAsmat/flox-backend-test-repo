package com.nocountry.floxbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long taskId;
    private String taskTitle;
    private Long authorId;
    private String authorUsername; // Optional: For displaying author's username


}
