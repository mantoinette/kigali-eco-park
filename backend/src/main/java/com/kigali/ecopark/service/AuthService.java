package com.kigali.ecopark.service;

import com.kigali.ecopark.dto.AuthDtos.AuthResponse;
import com.kigali.ecopark.dto.AuthDtos.LoginRequest;
import com.kigali.ecopark.dto.AuthDtos.RegisterRequest;
import com.kigali.ecopark.entity.UserAccount;
import com.kigali.ecopark.repository.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (userAccountRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }

        UserAccount user = new UserAccount();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole("VISITOR");

        UserAccount saved = userAccountRepository.save(user);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        UserAccount user = userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return toResponse(user);
    }

    /** Admin-only login — rejects visitor accounts even with valid credentials. */
    @Transactional(readOnly = true)
    public AuthResponse loginAdmin(LoginRequest request) {
        AuthResponse response = login(request);
        if (!"ADMIN".equalsIgnoreCase(response.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Administrator access required");
        }
        return response;
    }

    @Transactional(readOnly = true)
    public AuthResponse me(String token) {
        Long userId = parseUserId(token);
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session"));
        return toResponse(user);
    }

    /** Require a valid Bearer session whose role is ADMIN. */
    @Transactional(readOnly = true)
    public void requireAdmin(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin sign-in required");
        }
        String token = authorization.substring("Bearer ".length()).trim();
        AuthResponse profile = me(token);
        if (!"ADMIN".equalsIgnoreCase(profile.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }

    private AuthResponse toResponse(UserAccount user) {
        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                createToken(user.getId())
        );
    }

    private String createToken(Long userId) {
        String raw = userId + ":" + UUID.randomUUID();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    private Long parseUserId(String token) {
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            String idPart = decoded.split(":", 2)[0];
            return Long.parseLong(idPart);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session");
        }
    }
}
