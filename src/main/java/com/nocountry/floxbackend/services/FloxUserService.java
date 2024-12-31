package com.nocountry.floxbackend.services;


import com.nocountry.floxbackend.dtos.FloxUserDTO;
import com.nocountry.floxbackend.entities.FloxUser;
import com.nocountry.floxbackend.entities.Project;
import com.nocountry.floxbackend.entities.UserProjection;
import com.nocountry.floxbackend.repositories.FloxUserRepo;
import com.nocountry.floxbackend.repositories.ProjectRepo;
import com.nocountry.floxbackend.repositories.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.PhantomReference;
import java.util.List;
import java.util.Optional;

@Service
public class FloxUserService
{

    private final FloxUserRepo floxUserRepo;
    private final ProjectRepo projectRepo;
    private final TaskRepo taskRepo;

    public FloxUserService(FloxUserRepo floxUserRepo, ProjectRepo projectRepo, TaskRepo taskRepo)
    {
        this.floxUserRepo = floxUserRepo;
        this.projectRepo = projectRepo;
        this.taskRepo = taskRepo;
    }


    public List<UserProjection> findAllUserProjection()
    {

        return floxUserRepo.findAllUserProjection();
    }

    public UserProjection findUserProjectionById(Long id)
    {
        Optional<UserProjection> optionalUserProjection = floxUserRepo.findUserProjectionById(id);

        return optionalUserProjection.orElse(null);
    }

    public FloxUser findUserById(Long id)
    {
        return floxUserRepo.findById(id).orElse(null);
    }



    public FloxUserDTO updateUser(Long id, FloxUser newUserDetails)
    {
       Optional<FloxUser> optionalFloxUser = floxUserRepo.findById(id);

       if (optionalFloxUser.isEmpty())
       {
           return null;
       }

        FloxUser existingFloxUser = optionalFloxUser.get();

        existingFloxUser.setUserId(newUserDetails.getUserId());
        existingFloxUser.setUsername(newUserDetails.getUsername());
        existingFloxUser.setEmail(newUserDetails.getEmail());
        existingFloxUser.setUserRole(newUserDetails.getUserRole());

        FloxUser updateFloxUser  = floxUserRepo.save(existingFloxUser);

        return new FloxUserDTO(
                updateFloxUser.getUserId(),
                updateFloxUser.getUsername(),
                updateFloxUser.getEmail(),
                updateFloxUser.getUserRole()
        );
    }

    @Transactional
    public void deleteUserByIdWithAssociations(Long floxUserId)
    {
        FloxUser floxUser = floxUserRepo.findByIdWithAssociations(floxUserId)
                .orElse(null);

        if (floxUser == null)
        {
            return;
        }

        for (Project project : floxUser.getProjects())
        {
            project.getMembers().remove(floxUser);
        }

        for (Project createdProject : floxUser.getCreatedProjects())
        {
            if (createdProject.getMembers().isEmpty())
            {
                projectRepo.delete(createdProject);
            }
            else
            {
                createdProject.setCreator(createdProject.getMembers().iterator().next());
            }
        }

        taskRepo.deleteAll(floxUser.getAssignedTasks());

        floxUserRepo.delete(floxUser);
    }

    public FloxUser findUserByUsername(String username)
    {
        Optional<FloxUser> floxUser = floxUserRepo.findByUsername(username);

        return floxUser.orElse(null);
    }

    public FloxUser findUserByEmail(String userEmail)
    {

        return floxUserRepo.findByEmail(userEmail);
    }
}
