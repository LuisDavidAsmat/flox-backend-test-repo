package com.nocountry.floxbackend.repositories;

import com.nocountry.floxbackend.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepo extends JpaRepository<Project, Long>
{

}
