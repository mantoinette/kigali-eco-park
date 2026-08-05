package com.kigali.ecopark.dto;

public record SiteStatsDto(
        long treesDocumented,
        long treesGoal,
        long speciesDocumented,
        long speciesGoal,
        long totalVisitors,
        int languagesSupported
) {}
