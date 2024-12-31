package com.nocountry.floxbackend.controllers;

import com.nocountry.floxbackend.dtos.AuthResponseDTO;
import com.nocountry.floxbackend.dtos.LoginRequestDTO;
import com.nocountry.floxbackend.dtos.RefreshTokenRequestDTO;
import com.nocountry.floxbackend.dtos.RegisterRequestDTO;
import com.nocountry.floxbackend.services.FloxAuthService;
import com.nocountry.floxbackend.services.FloxUserService;
import com.nocountry.floxbackend.services.ProjectService;
import com.nocountry.floxbackend.services.TokenBlacklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController
{
    private final FloxAuthService floxAuthService;
    private final TokenBlacklistService tokenBlacklistService;


    public AuthController(FloxAuthService floxAuthService,
                          TokenBlacklistService tokenBlacklistService
    )
    {
        this.floxAuthService = floxAuthService;
        this.tokenBlacklistService = tokenBlacklistService;
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
    )
    {
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
        if (tokenHeader != null && tokenHeader.startsWith("Bearer "))
        {
            String token = tokenHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);

            //return ResponseEntity.ok("Logged out successfully.");

            Map<String, String > response = new HashMap<>();
            response.put("message", "Logged out successfully");

            return ResponseEntity.ok(response);
        }
        //return ResponseEntity.badRequest().body("Invalid token.");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid token.");

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
