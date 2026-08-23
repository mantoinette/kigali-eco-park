package com.kigali.ecopark.service;

import com.kigali.ecopark.dto.AdminUserDtos.AdminUserDto;
import com.kigali.ecopark.dto.AdminUserDtos.CreateUserRequest;
import com.kigali.ecopark.dto.AdminUserDtos.UpdateUserRequest;
import com.kigali.ecopark.entity.UserAccount;
import com.kigali.ecopark.repository.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class AdminUserService {

    private static final String PRIMARY_ADMIN_EMAIL = "admin@treescan.rw";

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<AdminUserDto> listAll() {
        return userAccountRepository.findAll().stream()
                .sorted((a, b) -> a.getEmail().compareToIgnoreCase(b.getEmail()))
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public AdminUserDto create(CreateUserRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (userAccountRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        String role = normalizeRole(request.role());
        UserAccount user = new UserAccount();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(role);

        return toDto(userAccountRepository.save(user));
    }

    @Transactional
    public AdminUserDto update(Long id, UpdateUserRequest request) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (request.fullName() != null && !request.fullName().isBlank()) {
            user.setFullName(request.fullName().trim());
        }
        if (request.role() != null && !request.role().isBlank()) {
            user.setRole(normalizeRole(request.role()));
        }
        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return toDto(userAccountRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (PRIMARY_ADMIN_EMAIL.equalsIgnoreCase(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete the primary administrator");
        }

        userAccountRepository.delete(user);
    }

    private String normalizeRole(String role) {
        String normalized = role.trim().toUpperCase(Locale.ROOT);
        if (!"ADMIN".equals(normalized) && !"VISITOR".equals(normalized)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role must be ADMIN or VISITOR");
        }
        return normalized;
    }

    private AdminUserDto toDto(UserAccount user) {
        return new AdminUserDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
