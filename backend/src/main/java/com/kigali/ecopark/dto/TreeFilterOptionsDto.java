package com.kigali.ecopark.dto;

import java.util.List;

public record TreeFilterOptionsDto(
        List<String> families,
        List<String> categories,
        List<String> nativeStatuses
) {}
