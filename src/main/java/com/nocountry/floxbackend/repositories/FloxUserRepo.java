package com.nocountry.floxbackend.repositories;


import com.nocountry.floxbackend.dtos.FloxUserDTO;
import com.nocountry.floxbackend.entities.FloxUser;
import com.nocountry.floxbackend.entities.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FloxUserRepo extends JpaRepository<FloxUser, Long>
{
    FloxUser findByEmail(String email);
    Optional<FloxUser> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query("SELECT u.userId AS userId, u.username AS username, u.email AS email, u.userRole AS userRole " +
            "FROM FloxUser u")
    List<UserProjection> findAllUserProjection();

    @Query("SELECT u.userId AS userId, u.username AS username, u.email AS email, u.userRole AS userRole " +
            "FROM FloxUser u WHERE u.username = :username")
    Optional<UserProjection> findUserProjectionByUsername(@Param("username") String username);

    @Query("SELECT u FROM FloxUser u LEFT JOIN FETCH u.projects WHERE u.id = :userId")
    Optional<FloxUser> findByIdWithAssociations(@Param("userId") Long floxUserId);

    @Query("SELECT u.userId AS userId, u.username AS username, u.email AS email, u.userRole AS userRole " +
    "FROM FloxUser u WHERE u.userId = :userId")
    Optional<UserProjection> findUserProjectionById(@Param("userId") Long userId);
}
