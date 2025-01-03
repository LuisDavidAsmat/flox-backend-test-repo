package com.nocountry.floxbackend.dtos;


import com.nocountry.floxbackend.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO
{
    private String username;
    private String email;

    @NotBlank
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    private UserRole userRole;
}
