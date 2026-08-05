package com.kigali.ecopark.dto;

public record TreeMapMarkerDto(
        Long id,
        String slug,
        String qrCodeId,
        String commonName,
        String scientificName,
        String family,
        Double latitude,
        Double longitude,
        String primaryImageUrl
) {}
