package com.nocountry.floxbackend.services;

import com.nocountry.floxbackend.entities.FloxUser;
import com.nocountry.floxbackend.entities.FloxUserPrincipal;
import com.nocountry.floxbackend.repositories.FloxUserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FloxUserDetailsService implements UserDetailsService
{
    private final FloxUserRepo floxUserRepo;

    public FloxUserDetailsService(FloxUserRepo floxUserRepo)
    {
        this.floxUserRepo = floxUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        FloxUser floxUser = floxUserRepo.findByEmail(email);

        if (floxUser == null)
        {
            throw new UsernameNotFoundException("User was not found.");
        }

        return new FloxUserPrincipal(floxUser);
    }
}
