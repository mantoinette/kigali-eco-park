package com.kigali.ecopark.controller;

import com.kigali.ecopark.dto.LanguageDto;
import com.kigali.ecopark.service.LanguageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<LanguageDto> getLanguages() {
        return languageService.getActiveLanguages();
    }
}
