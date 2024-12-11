package com.nocountry.floxbackend.controllers;

import com.nocountry.floxbackend.dtos.AuthResponseDTO;
import com.nocountry.floxbackend.dtos.LoginRequestDTO;
import com.nocountry.floxbackend.dtos.RefreshTokenRequestDTO;
import com.nocountry.floxbackend.dtos.RegisterRequestDTO;
import com.nocountry.floxbackend.services.FloxAuthService;
import com.nocountry.floxbackend.services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController
{
    private final FloxAuthService floxAuthService;
    private final TokenBlacklistService tokenBlacklistService;

    public UserController(FloxAuthService floxAuthService, TokenBlacklistService tokenBlacklistService) {
        this.floxAuthService = floxAuthService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @GetMapping("/")
    public String greet()
    {
        return "Welcome to main page ";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginRequestDTO request
    ) {
        return ResponseEntity.ok(floxAuthService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody RegisterRequestDTO request
    ) {
        return ResponseEntity.ok(floxAuthService.register(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(
            @RequestBody RefreshTokenRequestDTO request
    ) {
        return ResponseEntity.ok(floxAuthService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String tokenHeader)
    {

        System.out.println(tokenHeader);
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("Logged out successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid token.");
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminDashboard()
    { return ResponseEntity.ok("Welcome to the admin dashboard!"); }

    @GetMapping("/user/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> getUserProfile()
    { return ResponseEntity.ok("Welcome to your profile!, user"); }

    @GetMapping("/how")
    public String howAreYou(HttpServletRequest request)
    {
        return "How are you page. " + request.getSession().getId();
    }
}
