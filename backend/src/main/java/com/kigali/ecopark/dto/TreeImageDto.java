package com.kigali.ecopark.dto;

public record TreeImageDto(
        Long id,
        String url,
        String caption,
        boolean primary,
        Integer displayOrder
) {}
