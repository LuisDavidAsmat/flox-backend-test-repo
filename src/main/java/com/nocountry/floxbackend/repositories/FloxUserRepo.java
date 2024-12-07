package com.nocountry.floxbackend.repositories;


import com.nocountry.floxbackend.entities.FloxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FloxUserRepo extends JpaRepository<FloxUser, Long>
{
    FloxUser findByEmail(String email);
    Optional<FloxUser> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
