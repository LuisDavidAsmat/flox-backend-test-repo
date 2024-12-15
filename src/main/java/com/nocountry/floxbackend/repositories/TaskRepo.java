package com.nocountry.floxbackend.repositories;


import com.nocountry.floxbackend.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long>
{

    Task findUserByTitle(String Title);
}
