package com.kigali.ecopark.controller;

import com.kigali.ecopark.dto.AdminLanguageDtos.AdminLanguageDto;
import com.kigali.ecopark.dto.AdminLanguageDtos.CreateLanguageRequest;
import com.kigali.ecopark.dto.AdminLanguageDtos.UpdateLanguageRequest;
import com.kigali.ecopark.service.AdminLanguageService;
import com.kigali.ecopark.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/languages")
public class AdminLanguageController {

    private final AdminLanguageService adminLanguageService;
    private final AuthService authService;

    public AdminLanguageController(AdminLanguageService adminLanguageService, AuthService authService) {
        this.adminLanguageService = adminLanguageService;
        this.authService = authService;
    }

    @GetMapping
    public List<AdminLanguageDto> list(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        return adminLanguageService.listAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminLanguageDto create(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody CreateLanguageRequest request
    ) {
        authService.requireAdmin(authorization);
        return adminLanguageService.create(request);
    }

    @PatchMapping("/{code}")
    public AdminLanguageDto update(
            @PathVariable String code,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody UpdateLanguageRequest request
    ) {
        authService.requireAdmin(authorization);
        return adminLanguageService.update(code, request);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String code,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        adminLanguageService.delete(code);
    }
}
