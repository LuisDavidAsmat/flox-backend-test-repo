package com.nocountry.floxbackend.services;


import com.nocountry.floxbackend.dtos.CommentDTO;
import com.nocountry.floxbackend.entities.Comment;
import com.nocountry.floxbackend.entities.FloxUser;
import com.nocountry.floxbackend.entities.Project;
import com.nocountry.floxbackend.entities.Task;
import com.nocountry.floxbackend.exceptions.ResourceNotFoundException;
import com.nocountry.floxbackend.repositories.CommentRepo;
import com.nocountry.floxbackend.repositories.FloxUserRepo;
import com.nocountry.floxbackend.repositories.TaskRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final TaskRepo taskRepo;
    private final FloxUserRepo floxUserRepo;
    private final static Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService(CommentRepo commentRepo, TaskRepo taskRepo,
                          FloxUserRepo floxUserRepo)
    {
        this.commentRepo = commentRepo;
        this.taskRepo = taskRepo;
        this.floxUserRepo = floxUserRepo;
    }


    public CommentDTO createComment(CommentDTO commentDTO)
    {
        Comment comment = new Comment();
        System.out.println("cccc: " + commentDTO.getContent() + "\n\n");
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        Task task = taskRepo.findById(commentDTO.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        comment.setTask(task);

        FloxUser taskAuthor = floxUserRepo.findById(commentDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        comment.setAuthor(taskAuthor);

        Comment createdComment = commentRepo.save(comment);

        return convertToDTO(createdComment);
    }

    private CommentDTO convertToDTO(Comment createdComment)
    {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(createdComment.getId());
        commentDTO.setContent(createdComment.getContent());
        commentDTO.setCreatedAt(createdComment.getCreatedAt());
        commentDTO.setTaskId(createdComment.getTask().getId());
        commentDTO.setAuthorId(createdComment.getAuthor().getUserId());

        return commentDTO;
    }

    public List<CommentDTO> getAllComments()
    {
        List<Comment> commentList = commentRepo.findAll();

        return commentList
                .stream()
                .map(
                        comment ->
                        {
                            return new CommentDTO(
                                    comment.getId(),
                                    comment.getContent(),
                                    comment.getCreatedAt(),
                                    comment.getTask().getId(),
                                    comment.getTask().getTitle(),
                                    comment.getAuthor().getUserId(),
                                    comment.getAuthor().getUsername()
                            );
                        }
                ).toList();
    }

    public CommentDTO getCommentById(Long commentId)
    {
        Optional<Comment> optionalComment = commentRepo.findById(commentId);

        if (optionalComment.isEmpty())
        {
            return null;
        }

        Comment comment = optionalComment.get();

        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getTask().getId(),
                comment.getTask().getTitle(),
                comment.getAuthor().getUserId(),
                comment.getAuthor().getUsername()
        );
    }

    public CommentDTO updateComment(Long id, CommentDTO commentDTO)
    {
        Comment existingComment = commentRepo.findById(id).orElse(null);

        if (existingComment == null)
        {
            return null;
        }
        //logger.info("{}{}", "sdfsdfsdfdsLn\n\n", comment.getTask().getId());
        existingComment.setContent(commentDTO.getContent());

        String taskTitle = commentDTO.getTaskTitle();
        Task task = taskRepo.findByTitle(taskTitle);

        String authorUsername = commentDTO.getAuthorUsername();
        FloxUser floxUser = floxUserRepo.findByUsername(authorUsername).orElse(null);

        existingComment.setTask(task);
        existingComment.setAuthor(floxUser);

        Comment updatedComment = commentRepo.save(existingComment);

        return new CommentDTO(
                updatedComment.getId(),
                updatedComment.getContent(),
                updatedComment.getCreatedAt(),
                updatedComment.getTask().getId(),
                updatedComment.getTask().getTitle(),
                updatedComment.getAuthor().getUserId(),
                updatedComment.getAuthor().getUsername()
        );
    }

    public void deleteComment(Long id)
    {
        Comment comment = commentRepo.findById(id)
                .orElseThrow( () -> new RuntimeException("Comment was not found with id: " + id));

        commentRepo.delete(comment);
    }
}