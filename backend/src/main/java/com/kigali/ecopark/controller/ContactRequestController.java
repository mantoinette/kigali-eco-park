package com.kigali.ecopark.controller;

import com.kigali.ecopark.dto.ContactRequestDtos.ContactRequestDto;
import com.kigali.ecopark.dto.ContactRequestDtos.ContactStatsDto;
import com.kigali.ecopark.dto.ContactRequestDtos.SubmitContactRequest;
import com.kigali.ecopark.dto.ContactRequestDtos.UpdateContactRequest;
import com.kigali.ecopark.service.AuthService;
import com.kigali.ecopark.service.ContactRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactRequestController {

    private final ContactRequestService contactRequestService;
    private final AuthService authService;

    public ContactRequestController(ContactRequestService contactRequestService, AuthService authService) {
        this.contactRequestService = contactRequestService;
        this.authService = authService;
    }

    /** Public: submit a contact or QR code request. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactRequestDto submit(@Valid @RequestBody SubmitContactRequest request) {
        return contactRequestService.submit(request);
    }

    /** Admin: list all contact requests with optional filters. */
    @GetMapping
    public List<ContactRequestDto> list(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String requestType
    ) {
        authService.requireAdmin(authorization);
        return contactRequestService.listAll(status, requestType);
    }

    @GetMapping("/stats")
    public ContactStatsDto stats(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        return contactRequestService.stats();
    }

    @GetMapping("/{id}")
    public ContactRequestDto getOne(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        return contactRequestService.getById(id);
    }

    @PatchMapping("/{id}")
    public ContactRequestDto update(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody UpdateContactRequest request
    ) {
        authService.requireAdmin(authorization);
        return contactRequestService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        contactRequestService.delete(id);
    }
}
