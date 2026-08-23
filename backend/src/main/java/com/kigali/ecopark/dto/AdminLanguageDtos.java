package com.kigali.ecopark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public final class AdminLanguageDtos {

    private AdminLanguageDtos() {}

    public record AdminLanguageDto(
            String code,
            String name,
            boolean active,
            Instant createdAt
    ) {}

    public record CreateLanguageRequest(
            @NotBlank @Size(max = 10) String code,
            @NotBlank @Size(max = 100) String name,
            boolean active
    ) {}

    public record UpdateLanguageRequest(
            @Size(max = 100) String name,
            Boolean active
    ) {}
}
