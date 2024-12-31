package com.nocountry.floxbackend.repositories;

import com.nocountry.floxbackend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long>
{

}
