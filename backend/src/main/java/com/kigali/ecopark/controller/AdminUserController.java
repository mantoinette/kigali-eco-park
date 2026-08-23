package com.kigali.ecopark.controller;

import com.kigali.ecopark.dto.AdminUserDtos.AdminUserDto;
import com.kigali.ecopark.dto.AdminUserDtos.CreateUserRequest;
import com.kigali.ecopark.dto.AdminUserDtos.UpdateUserRequest;
import com.kigali.ecopark.service.AdminUserService;
import com.kigali.ecopark.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final AuthService authService;

    public AdminUserController(AdminUserService adminUserService, AuthService authService) {
        this.adminUserService = adminUserService;
        this.authService = authService;
    }

    @GetMapping
    public List<AdminUserDto> list(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        return adminUserService.listAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminUserDto create(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody CreateUserRequest request
    ) {
        authService.requireAdmin(authorization);
        return adminUserService.create(request);
    }

    @PatchMapping("/{id}")
    public AdminUserDto update(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        authService.requireAdmin(authorization);
        return adminUserService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        adminUserService.delete(id);
    }
}
