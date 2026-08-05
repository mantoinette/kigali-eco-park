package com.kigali.ecopark.dto;

import java.util.List;

public record TreePageDto(
        List<TreeSummaryDto> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {}
