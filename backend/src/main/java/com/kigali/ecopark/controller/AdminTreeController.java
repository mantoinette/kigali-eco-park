package com.kigali.ecopark.controller;

import com.kigali.ecopark.dto.AdminTreeDtos.AdminTreeSummaryDto;
import com.kigali.ecopark.dto.AdminTreeDtos.CreateTreeRequest;
import com.kigali.ecopark.dto.AdminTreeDtos.UpdateTreeRequest;
import com.kigali.ecopark.dto.TreeDetailDto;
import com.kigali.ecopark.service.AdminTreeService;
import com.kigali.ecopark.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/trees")
public class AdminTreeController {

    private final AdminTreeService adminTreeService;
    private final AuthService authService;

    public AdminTreeController(AdminTreeService adminTreeService, AuthService authService) {
        this.adminTreeService = adminTreeService;
        this.authService = authService;
    }

    @GetMapping
    public List<AdminTreeSummaryDto> list(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(defaultValue = "en") String lang
    ) {
        authService.requireAdmin(authorization);
        return adminTreeService.listAll(lang);
    }

    @GetMapping("/{id}")
    public TreeDetailDto getOne(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(defaultValue = "en") String lang
    ) {
        authService.requireAdmin(authorization);
        return adminTreeService.getById(id, lang);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TreeDetailDto create(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody CreateTreeRequest request
    ) {
        authService.requireAdmin(authorization);
        return adminTreeService.create(request);
    }

    @PutMapping("/{id}")
    public TreeDetailDto update(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody UpdateTreeRequest request
    ) {
        authService.requireAdmin(authorization);
        return adminTreeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        adminTreeService.delete(id);
    }
}
