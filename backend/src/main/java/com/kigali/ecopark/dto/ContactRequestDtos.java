package com.kigali.ecopark.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class ContactRequestDtos {

    public record SubmitContactRequest(
            @NotBlank @Size(min = 2, max = 120) String fullName,
            @NotBlank @Email String email,
            @Size(max = 40) String phone,
            @NotBlank @Size(max = 40) String requestType,
            @Size(max = 200) String subject,
            @NotBlank @Size(min = 10, max = 5000) String message,
            @Size(max = 200) String treeName
    ) {}

    public record ContactRequestDto(
            Long id,
            String fullName,
            String email,
            String phone,
            String requestType,
            String subject,
            String message,
            String treeName,
            String status,
            String linkedTreeSlug,
            String adminNotes,
            Instant createdAt,
            Instant updatedAt
    ) {}

    public record UpdateContactRequest(
            @Size(max = 20) String status,
            @Size(max = 120) String linkedTreeSlug,
            @Size(max = 5000) String adminNotes
    ) {}

    public record ContactStatsDto(
            long total,
            long newCount,
            long inProgressCount,
            long resolvedCount,
            long qrRequestCount
    ) {}
}
