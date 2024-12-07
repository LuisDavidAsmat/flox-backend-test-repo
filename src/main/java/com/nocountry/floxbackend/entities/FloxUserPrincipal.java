package com.nocountry.floxbackend.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class FloxUserPrincipal implements UserDetails
{
    private final FloxUser floxUser;

    public FloxUserPrincipal(FloxUser floxUser)
    {
        this.floxUser = floxUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Collections.singleton(new SimpleGrantedAuthority(floxUser.getUserRole().name()));
    }

    @Override
    public String getPassword() {
        return floxUser.getPassword();
    }

    @Override
    public String getUsername()
    {

        return floxUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
