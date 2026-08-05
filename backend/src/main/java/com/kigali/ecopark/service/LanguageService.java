package com.kigali.ecopark.service;

import com.kigali.ecopark.dto.LanguageDto;
import com.kigali.ecopark.repository.LanguageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<LanguageDto> getActiveLanguages() {
        return languageRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(lang -> new LanguageDto(lang.getCode(), lang.getName()))
                .toList();
    }
}
