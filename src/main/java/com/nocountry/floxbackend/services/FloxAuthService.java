package com.nocountry.floxbackend.services;


import com.nocountry.floxbackend.config.jwt.JwtService;
import com.nocountry.floxbackend.dtos.AuthResponseDTO;
import com.nocountry.floxbackend.dtos.LoginRequestDTO;
import com.nocountry.floxbackend.dtos.RegisterRequestDTO;
import com.nocountry.floxbackend.entities.FloxUser;
import com.nocountry.floxbackend.repositories.FloxUserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FloxAuthService
{
    private final FloxUserRepo floxUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final FloxUserDetailsService floxUserDetailsService;

    public FloxAuthService(FloxUserRepo userRepository, FloxUserRepo floxUserRepo,
                           PasswordEncoder passwordEncoder, JwtService jwtService,
                           AuthenticationManager authenticationManager, FloxUserDetailsService floxUserDetailsService) {
        this.floxUserRepo = floxUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.floxUserDetailsService = floxUserDetailsService;
    }

    public AuthResponseDTO register(RegisterRequestDTO request)
    {



        // Check if user already exists
        if (floxUserRepo.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("Email already in use");
        }

        // Create new user
        FloxUser floxUser = new FloxUser();

        floxUser.setUsername(request.getUsername());
        floxUser.setEmail(request.getEmail());
        floxUser.setPassword(passwordEncoder.encode(request.getPassword()));
        floxUser.setUserRole(request.getUserRole());

        FloxUser savedUser = floxUserRepo.save(floxUser);

        // Generate JWT
        String token = jwtService.generateToken(floxUser.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(savedUser.getUserId())
                .refreshToken(jwtService.generateRefreshToken(floxUser.getEmail()))
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .userRole(savedUser.getUserRole())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request)
    {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Find user
        FloxUser floxUser = floxUserRepo.findByEmail(request.getEmail());

        // Generate JWT
        String token = jwtService.generateToken(floxUser.getEmail());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(floxUser.getUserId())
                .refreshToken(jwtService.generateRefreshToken(floxUser.getEmail()))
                .username(floxUser.getUsername())
                .email(floxUser.getEmail())
                .userRole(floxUser.getUserRole())
                .build();
    }

    public AuthResponseDTO refreshToken(String refreshToken) {
        // Validate refresh token
        try {
            String email = jwtService.getEmailFromToken(refreshToken);
            UserDetails userDetails = floxUserDetailsService.loadUserByUsername(email);

            // Validate refresh token
            if (jwtService.validateRefreshToken(refreshToken, userDetails))
            {
                // Generate new access token
                String newAccessToken = jwtService.generateToken(email);

                // Optionally generate a new refresh token
                String newRefreshToken = jwtService.generateRefreshToken(email);

                return AuthResponseDTO.builder()
                        .token(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();
            }
            else
            {
                throw new RuntimeException("Invalid refresh token");
            }
        }
        catch (Exception e)
        {
            System.out.println("Refresh Token Error: " + e.getMessage());
            throw new RuntimeException("Invalid refresh token");
        }
    }
}
