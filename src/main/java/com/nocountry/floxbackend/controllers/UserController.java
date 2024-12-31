package com.nocountry.floxbackend.controllers;

import com.nocountry.floxbackend.dtos.*;
import com.nocountry.floxbackend.entities.FloxUser;
import com.nocountry.floxbackend.entities.Project;
import com.nocountry.floxbackend.entities.UserProjection;
import com.nocountry.floxbackend.exceptions.ResourceNotFoundException;
import com.nocountry.floxbackend.repositories.FloxUserRepo;
import com.nocountry.floxbackend.services.FloxAuthService;
import com.nocountry.floxbackend.services.FloxUserService;
import com.nocountry.floxbackend.services.ProjectService;
import com.nocountry.floxbackend.services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController
{
    private final FloxUserService floxUserService;
    private final ProjectService projectService;

    public UserController(FloxUserService floxUserService, ProjectService projectService
    )
    {
        this.floxUserService = floxUserService;
        this.projectService = projectService;
    }


    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminDashboard()
    { return ResponseEntity.ok("Welcome to the admin dashboard!"); }

    @GetMapping("/user/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> getUserProfile()
    { return ResponseEntity.ok("Welcome to your profile!, user"); }


    @GetMapping("/users")
    public ResponseEntity<?> viewUsers ()
    {
        try
        {
            List<UserProjection> floxUserDTOS = floxUserService.findAllUserProjection();

            if (floxUserDTOS == null)
            {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(floxUserDTOS);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> viewUserById (@PathVariable Long id)
    {
        try
        {
            UserProjection floxUserProjection = floxUserService.findUserProjectionById(id);

            if (floxUserProjection == null)
            {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(floxUserProjection);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @PutMapping("/users/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser (@PathVariable Long id, @RequestBody FloxUser newUserDetails)
    {
        try
        {
            FloxUserDTO floxUserProjection = floxUserService.updateUser(id, newUserDetails);

            if (floxUserProjection == null)
            {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(floxUserProjection);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUserById (@PathVariable Long id)
    {
        try
        {
            FloxUser floxUser = floxUserService.findUserById(id);

            if(floxUser == null)
            {
                return ResponseEntity.notFound().build();
            }

            try
            {
                floxUserService.deleteUserByIdWithAssociations(id);

                return ResponseEntity.ok().body("User deleted successfully");
            }
            catch (DataIntegrityViolationException e)
            {
                return ResponseEntity.badRequest()
                        .body("Cannot delete user: User is associated with existing projects. " +
                                "Remove project associations first.");
            }
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/user/dashboard")
    public ResponseEntity<?> viewUserDashboard ()
    {
        try
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String userEmail = authentication.getName();

            FloxUser floxUser = floxUserService.findUserByEmail(userEmail);

            if (floxUser == null)
            {
                System.out.println("\n\njackal\n" );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            List<ProjectDTO> projectDTOS = projectService.findProjectsByUsersEmail(userEmail);

            if (projectDTOS.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(projectDTOS);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
        }
    }
}
