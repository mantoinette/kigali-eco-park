package com.kigali.ecopark.dto;

public record QrCodeResponseDto(
        String treeId,
        String slug,
        String scientificName,
        String url,
        String qrCodeBase64
) {}
