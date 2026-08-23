package com.kigali.ecopark.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public final class AdminUserDtos {

    private AdminUserDtos() {}

    public record AdminUserDto(
            Long id,
            String fullName,
            String email,
            String role,
            Instant createdAt
    ) {}

    public record CreateUserRequest(
            @NotBlank @Size(max = 120) String fullName,
            @NotBlank @Email @Size(max = 180) String email,
            @NotBlank @Size(min = 6, max = 100) String password,
            @NotBlank @Size(max = 30) String role
    ) {}

    public record UpdateUserRequest(
            @Size(max = 120) String fullName,
            @Size(max = 30) String role,
            @Size(min = 6, max = 100) String password
    ) {}
}
