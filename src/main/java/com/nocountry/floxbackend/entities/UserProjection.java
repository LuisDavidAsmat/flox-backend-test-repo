package com.nocountry.floxbackend.entities;

import com.nocountry.floxbackend.enums.UserRole;

public interface UserProjection
{
    Long getUserId();
    String getUsername();
    String getEmail();
    UserRole getUserRole();
}
