package com.kigali.ecopark.dto;

import java.util.List;

public record TreeSummaryDto(
        Long id,
        String scientificName,
        String slug,
        String qrCodeId,
        String commonName,
        String shortDescription,
        String family,
        String nativeStatus,
        List<String> categories,
        String primaryImageUrl,
        Integer displayOrder
) {}
