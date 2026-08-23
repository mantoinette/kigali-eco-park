package com.kigali.ecopark.service;

import com.kigali.ecopark.dto.ContactRequestDtos.ContactRequestDto;
import com.kigali.ecopark.dto.ContactRequestDtos.ContactStatsDto;
import com.kigali.ecopark.dto.ContactRequestDtos.SubmitContactRequest;
import com.kigali.ecopark.dto.ContactRequestDtos.UpdateContactRequest;
import com.kigali.ecopark.entity.ContactRequest;
import com.kigali.ecopark.repository.ContactRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ContactRequestService {

    private static final Set<String> REQUEST_TYPES = Set.of(
            "QR_CODE_REQUEST",
            "TREE_INFORMATION",
            "GENERAL_INQUIRY",
            "PARTNERSHIP",
            "OTHER"
    );

    private static final Set<String> STATUSES = Set.of("NEW", "IN_PROGRESS", "RESOLVED");

    private final ContactRequestRepository contactRequestRepository;

    public ContactRequestService(ContactRequestRepository contactRequestRepository) {
        this.contactRequestRepository = contactRequestRepository;
    }

    @Transactional
    public ContactRequestDto submit(SubmitContactRequest request) {
        String requestType = normalizeRequestType(request.requestType());
        ContactRequest entity = new ContactRequest();
        entity.setFullName(request.fullName().trim());
        entity.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        entity.setPhone(blankToNull(request.phone()));
        entity.setRequestType(requestType);
        entity.setSubject(blankToNull(request.subject()));
        entity.setMessage(request.message().trim());
        entity.setTreeName(blankToNull(request.treeName()));
        entity.setStatus("NEW");
        return toDto(contactRequestRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<ContactRequestDto> listAll(String status, String requestType) {
        String normalizedStatus = blankToNull(status);
        String normalizedType = requestType != null && !requestType.isBlank()
                ? normalizeRequestType(requestType)
                : null;
        if (normalizedStatus != null && !STATUSES.contains(normalizedStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status filter");
        }
        return contactRequestRepository.findFiltered(normalizedStatus, normalizedType).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContactRequestDto getById(Long id) {
        return toDto(findEntity(id));
    }

    @Transactional(readOnly = true)
    public ContactStatsDto stats() {
        List<ContactRequest> all = contactRequestRepository.findAllByOrderByCreatedAtDesc();
        long qr = all.stream().filter(c -> "QR_CODE_REQUEST".equals(c.getRequestType())).count();
        return new ContactStatsDto(
                all.size(),
                all.stream().filter(c -> "NEW".equals(c.getStatus())).count(),
                all.stream().filter(c -> "IN_PROGRESS".equals(c.getStatus())).count(),
                all.stream().filter(c -> "RESOLVED".equals(c.getStatus())).count(),
                qr
        );
    }

    @Transactional
    public ContactRequestDto update(Long id, UpdateContactRequest request) {
        ContactRequest entity = findEntity(id);
        if (request.status() != null && !request.status().isBlank()) {
            String status = request.status().trim().toUpperCase(Locale.ROOT);
            if (!STATUSES.contains(status)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
            }
            entity.setStatus(status);
        }
        if (request.linkedTreeSlug() != null) {
            entity.setLinkedTreeSlug(blankToNull(request.linkedTreeSlug()));
        }
        if (request.adminNotes() != null) {
            entity.setAdminNotes(blankToNull(request.adminNotes()));
        }
        return toDto(contactRequestRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!contactRequestRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact request not found");
        }
        contactRequestRepository.deleteById(id);
    }

    private ContactRequest findEntity(Long id) {
        return contactRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact request not found"));
    }

    private String normalizeRequestType(String requestType) {
        String normalized = requestType.trim().toUpperCase(Locale.ROOT);
        if (!REQUEST_TYPES.contains(normalized)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request type");
        }
        return normalized;
    }

    private String blankToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private ContactRequestDto toDto(ContactRequest entity) {
        return new ContactRequestDto(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getRequestType(),
                entity.getSubject(),
                entity.getMessage(),
                entity.getTreeName(),
                entity.getStatus(),
                entity.getLinkedTreeSlug(),
                entity.getAdminNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
