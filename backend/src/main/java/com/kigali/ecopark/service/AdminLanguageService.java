package com.kigali.ecopark.service;

import com.kigali.ecopark.dto.AdminLanguageDtos.AdminLanguageDto;
import com.kigali.ecopark.dto.AdminLanguageDtos.CreateLanguageRequest;
import com.kigali.ecopark.dto.AdminLanguageDtos.UpdateLanguageRequest;
import com.kigali.ecopark.entity.Language;
import com.kigali.ecopark.repository.LanguageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class AdminLanguageService {

    private static final Set<String> PROTECTED_CODES = Set.of("en", "rw", "fr");

    private final LanguageRepository languageRepository;

    public AdminLanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminLanguageDto> listAll() {
        return languageRepository.findAll().stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public AdminLanguageDto create(CreateLanguageRequest request) {
        String code = request.code().trim().toLowerCase(Locale.ROOT);
        if (languageRepository.existsById(code)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Language code already exists");
        }

        Language language = new Language();
        language.setCode(code);
        language.setName(request.name().trim());
        language.setActive(request.active());
        return toDto(languageRepository.save(language));
    }

    @Transactional
    public AdminLanguageDto update(String code, UpdateLanguageRequest request) {
        Language language = languageRepository.findById(code.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language not found"));

        if (request.name() != null && !request.name().isBlank()) {
            language.setName(request.name().trim());
        }
        if (request.active() != null) {
            language.setActive(request.active());
        }

        return toDto(languageRepository.save(language));
    }

    @Transactional
    public void delete(String code) {
        String normalized = code.toLowerCase(Locale.ROOT);
        if (PROTECTED_CODES.contains(normalized)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete a core language");
        }
        if (!languageRepository.existsById(normalized)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Language not found");
        }
        languageRepository.deleteById(normalized);
    }

    private AdminLanguageDto toDto(Language language) {
        return new AdminLanguageDto(
                language.getCode(),
                language.getName(),
                language.isActive(),
                language.getCreatedAt()
        );
    }
}
