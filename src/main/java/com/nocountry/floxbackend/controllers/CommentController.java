package com.nocountry.floxbackend.controllers;


import com.nocountry.floxbackend.dtos.CommentDTO;
import com.nocountry.floxbackend.dtos.ProjectDTO;
import com.nocountry.floxbackend.entities.Comment;
import com.nocountry.floxbackend.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController
{
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment (@Valid @RequestBody CommentDTO commentDTO)
    {
        CommentDTO createdComment = commentService.createComment(commentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> viewComments()
    {
        try
        {
            List<CommentDTO> commentDTOList = commentService.getAllComments();

            if (commentDTOList.isEmpty())
            {
                System.out.println("comments are empty!!");
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(commentDTOList);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> viewComment(@PathVariable Long id)
    {
        try
        {
            CommentDTO commentDTO = commentService.getCommentById(id);

            if (commentDTO == null)
            {
                System.out.println("comments are empty!!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(commentDTO);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO)
    {
        try
        {
            CommentDTO updatedCommentDTO = commentService.updateComment(id, commentDTO);

            if (updatedCommentDTO == null)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(updatedCommentDTO);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment (@PathVariable Long id)
    {
        try
        {
            commentService.deleteComment(id);

            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.noContent().build();
        }
    }

}
