package com.nocountry.floxbackend.dtos;

import com.nocountry.floxbackend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO
{
    private String token;
    private String refreshToken;
    private Long userId;
    private String username;
    private String email;
    private UserRole userRole;
}
