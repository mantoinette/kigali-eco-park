package com.kigali.ecopark.dto;

import java.util.List;

public record TreeDetailDto(
        Long id,
        String scientificName,
        String slug,
        String qrCodeId,
        String family,
        String typicalHeight,
        String origin,
        String ageEstimate,
        Double latitude,
        Double longitude,
        String audioUrl,
        String videoUrl,
        String languageCode,
        String commonName,
        String shortDescription,
        String quickFacts,
        String description,
        String uses,
        String ecologicalImportance,
        String benefitsToPeopleAndWildlife,
        String commonAreas,
        String additionalInfo,
        String interestingFacts,
        List<TreeImageDto> images,
        List<String> availableLanguages
) {}
