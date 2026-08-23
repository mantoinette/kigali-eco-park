package com.kigali.ecopark.service;

import com.kigali.ecopark.dto.AdminTreeDtos.AdminTreeSummaryDto;
import com.kigali.ecopark.dto.AdminTreeDtos.CreateTreeRequest;
import com.kigali.ecopark.dto.AdminTreeDtos.TreeTranslationInput;
import com.kigali.ecopark.dto.AdminTreeDtos.UpdateTreeRequest;
import com.kigali.ecopark.dto.TreeDetailDto;
import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.repository.TreeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class AdminTreeService {

    private static final String DEFAULT_LANGUAGE = "en";

    private final TreeRepository treeRepository;
    private final TreeService treeService;

    public AdminTreeService(TreeRepository treeRepository, TreeService treeService) {
        this.treeRepository = treeRepository;
        this.treeService = treeService;
    }

    @Transactional(readOnly = true)
    public List<AdminTreeSummaryDto> listAll(String languageCode) {
        String lang = normalizeLanguage(languageCode);
        return treeRepository.findAllWithDetails().stream()
                .map(tree -> toSummary(tree, lang))
                .toList();
    }

    @Transactional(readOnly = true)
    public TreeDetailDto getById(Long id, String languageCode) {
        Tree tree = treeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));
        return treeService.getTreeDetailForAdmin(tree, normalizeLanguage(languageCode));
    }

    @Transactional
    public TreeDetailDto create(CreateTreeRequest request) {
        String slug = normalizeSlug(request.slug());
        if (treeRepository.existsBySlug(slug)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already in use");
        }

        String qrCodeId = resolveQrCodeId(request.qrCodeId());
        if (treeRepository.existsByQrCodeId(qrCodeId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Park ID already in use");
        }

        TreeTranslationInput translation = request.translation();
        if (translation == null || translation.commonName() == null || translation.commonName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "English common name is required");
        }

        Tree tree = new Tree();
        tree.setScientificName(request.scientificName().trim());
        tree.setSlug(slug);
        tree.setQrCodeId(qrCodeId);
        tree.setQrAccessToken(generateAccessToken());
        tree.setFamily(blankToNull(request.family()));
        tree.setNativeStatus(blankToNull(request.nativeStatus()) == null ? "UNKNOWN" : request.nativeStatus().trim().toUpperCase(Locale.ROOT));
        tree.setCategories(request.categories() == null ? new LinkedHashSet<>() : new LinkedHashSet<>(request.categories()));
        tree.setTypicalHeight(blankToNull(request.typicalHeight()));
        tree.setOrigin(blankToNull(request.origin()));
        tree.setLatitude(request.latitude());
        tree.setLongitude(request.longitude());
        tree.setAudioUrl(blankToNull(request.audioUrl()));
        tree.setVideoUrl(blankToNull(request.videoUrl()));
        tree.setDisplayOrder(request.displayOrder() != null ? request.displayOrder() : nextDisplayOrder());
        tree.setPublished(request.published());

        applyTranslation(tree, translation);

        Tree saved = treeRepository.save(tree);
        return treeService.getTreeDetailForAdmin(saved, normalizeLanguage(translation.languageCode()));
    }

    @Transactional
    public TreeDetailDto update(Long id, UpdateTreeRequest request) {
        Tree tree = treeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));

        if (request.slug() != null && !request.slug().isBlank()) {
            String slug = normalizeSlug(request.slug());
            if (!slug.equalsIgnoreCase(tree.getSlug()) && treeRepository.existsBySlug(slug)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug already in use");
            }
            tree.setSlug(slug);
        }

        if (request.qrCodeId() != null && !request.qrCodeId().isBlank()) {
            String qrCodeId = request.qrCodeId().trim().toUpperCase(Locale.ROOT);
            if (!qrCodeId.equalsIgnoreCase(tree.getQrCodeId()) && treeRepository.existsByQrCodeId(qrCodeId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Park ID already in use");
            }
            tree.setQrCodeId(qrCodeId);
        }

        if (request.scientificName() != null && !request.scientificName().isBlank()) {
            tree.setScientificName(request.scientificName().trim());
        }
        if (request.family() != null) {
            tree.setFamily(blankToNull(request.family()));
        }
        if (request.nativeStatus() != null && !request.nativeStatus().isBlank()) {
            tree.setNativeStatus(request.nativeStatus().trim().toUpperCase(Locale.ROOT));
        }
        if (request.categories() != null) {
            tree.setCategories(new LinkedHashSet<>(request.categories()));
        }
        if (request.typicalHeight() != null) {
            tree.setTypicalHeight(blankToNull(request.typicalHeight()));
        }
        if (request.origin() != null) {
            tree.setOrigin(blankToNull(request.origin()));
        }
        if (request.latitude() != null) {
            tree.setLatitude(request.latitude());
        }
        if (request.longitude() != null) {
            tree.setLongitude(request.longitude());
        }
        if (request.audioUrl() != null) {
            tree.setAudioUrl(blankToNull(request.audioUrl()));
        }
        if (request.videoUrl() != null) {
            tree.setVideoUrl(blankToNull(request.videoUrl()));
        }
        if (request.displayOrder() != null) {
            tree.setDisplayOrder(request.displayOrder());
        }
        if (request.published() != null) {
            tree.setPublished(request.published());
        }

        if (request.translation() != null) {
            applyTranslation(tree, request.translation());
        }

        if (tree.getQrAccessToken() == null || tree.getQrAccessToken().isBlank()) {
            tree.setQrAccessToken(generateAccessToken());
        }

        Tree saved = treeRepository.save(tree);
        String lang = request.translation() != null
                ? normalizeLanguage(request.translation().languageCode())
                : DEFAULT_LANGUAGE;
        return treeService.getTreeDetailForAdmin(saved, lang);
    }

    @Transactional
    public void delete(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));
        treeRepository.delete(tree);
    }

    private void applyTranslation(Tree tree, TreeTranslationInput input) {
        String lang = normalizeLanguage(input.languageCode());
        TreeTranslation translation = tree.getTranslations().stream()
                .filter(t -> t.getLanguageCode().equalsIgnoreCase(lang))
                .findFirst()
                .orElseGet(() -> {
                    TreeTranslation created = new TreeTranslation();
                    created.setTree(tree);
                    created.setLanguageCode(lang);
                    tree.getTranslations().add(created);
                    return created;
                });

        translation.setCommonName(input.commonName().trim());
        if (input.shortDescription() != null) {
            translation.setShortDescription(blankToNull(input.shortDescription()));
        }
        if (input.description() != null) {
            translation.setDescription(blankToNull(input.description()));
        }
    }

    private AdminTreeSummaryDto toSummary(Tree tree, String languageCode) {
        String commonName = tree.getTranslations().stream()
                .filter(t -> t.getLanguageCode().equalsIgnoreCase(languageCode))
                .map(TreeTranslation::getCommonName)
                .findFirst()
                .or(() -> tree.getTranslations().stream()
                        .filter(t -> t.getLanguageCode().equalsIgnoreCase(DEFAULT_LANGUAGE))
                        .map(TreeTranslation::getCommonName)
                        .findFirst())
                .orElse(tree.getScientificName());

        return new AdminTreeSummaryDto(
                tree.getId(),
                tree.getScientificName(),
                tree.getSlug(),
                tree.getQrCodeId(),
                commonName,
                tree.getFamily(),
                tree.isPublished(),
                tree.getDisplayOrder()
        );
    }

    private int nextDisplayOrder() {
        Integer max = treeRepository.findMaxDisplayOrder();
        return (max == null ? 0 : max) + 1;
    }

    private String resolveQrCodeId(String requested) {
        if (requested != null && !requested.isBlank()) {
            return requested.trim().toUpperCase(Locale.ROOT);
        }
        int next = nextDisplayOrder();
        return String.format("TREE-%03d", next);
    }

    private String generateAccessToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String normalizeSlug(String slug) {
        return slug.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", "-");
    }

    private String normalizeLanguage(String languageCode) {
        if (languageCode == null || languageCode.isBlank()) {
            return DEFAULT_LANGUAGE;
        }
        return languageCode.toLowerCase(Locale.ROOT);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
