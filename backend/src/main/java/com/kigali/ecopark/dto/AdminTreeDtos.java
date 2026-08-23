package com.kigali.ecopark.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public final class AdminTreeDtos {

    private AdminTreeDtos() {}

    public record TreeTranslationInput(
            @NotBlank @Size(max = 10) String languageCode,
            @NotBlank @Size(max = 200) String commonName,
            @Size(max = 5000) String shortDescription,
            @Size(max = 20000) String description
    ) {}

    public record CreateTreeRequest(
            @NotBlank @Size(max = 200) String scientificName,
            @NotBlank @Size(max = 200) String slug,
            @Size(max = 50) String qrCodeId,
            @Size(max = 100) String family,
            @Size(max = 20) String nativeStatus,
            Set<String> categories,
            @Size(max = 50) String typicalHeight,
            @Size(max = 120) String origin,
            Double latitude,
            Double longitude,
            @Size(max = 500) String audioUrl,
            @Size(max = 500) String videoUrl,
            Integer displayOrder,
            boolean published,
            @NotNull @Valid TreeTranslationInput translation
    ) {}

    public record UpdateTreeRequest(
            @Size(max = 200) String scientificName,
            @Size(max = 200) String slug,
            @Size(max = 50) String qrCodeId,
            @Size(max = 100) String family,
            @Size(max = 20) String nativeStatus,
            Set<String> categories,
            @Size(max = 50) String typicalHeight,
            @Size(max = 120) String origin,
            Double latitude,
            Double longitude,
            @Size(max = 500) String audioUrl,
            @Size(max = 500) String videoUrl,
            Integer displayOrder,
            Boolean published,
            TreeTranslationInput translation
    ) {}

    public record AdminTreeSummaryDto(
            Long id,
            String scientificName,
            String slug,
            String qrCodeId,
            String commonName,
            String family,
            boolean published,
            Integer displayOrder
    ) {}
}
