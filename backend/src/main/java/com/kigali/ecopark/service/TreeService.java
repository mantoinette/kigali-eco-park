package com.kigali.ecopark.service;

import com.kigali.ecopark.dto.SiteStatsDto;
import com.kigali.ecopark.dto.TreeDetailDto;
import com.kigali.ecopark.dto.TreeFilterOptionsDto;
import com.kigali.ecopark.dto.TreeImageDto;
import com.kigali.ecopark.dto.TreeMapMarkerDto;
import com.kigali.ecopark.dto.TreePageDto;
import com.kigali.ecopark.dto.TreeSummaryDto;
import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.repository.TreeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TreeService {

    private static final String DEFAULT_LANGUAGE = "en";
    private static final int DEFAULT_PAGE_SIZE = 12;
    private static final int MAX_PAGE_SIZE = 48;

    private final TreeRepository treeRepository;

    public TreeService(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    public List<TreeSummaryDto> getAllTrees(String languageCode) {
        String lang = normalizeLanguage(languageCode);
        return treeRepository.findAllPublishedWithDetails().stream()
                .map(tree -> toSummary(tree, lang))
                .toList();
    }

    public TreePageDto browseCatalog(
            String languageCode,
            String query,
            String family,
            String category,
            String nativeStatus,
            int page,
            int size,
            String sortKey
    ) {
        String lang = normalizeLanguage(languageCode);
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);

        PageRequest pageable = PageRequest.of(safePage, safeSize, catalogSort(sortKey));

        Page<Tree> result = treeRepository.findCatalog(
                blankToNull(query),
                blankToNull(family),
                blankToNull(nativeStatus),
                blankToNull(category),
                pageable
        );

        List<TreeSummaryDto> content = result.getContent().stream()
                .map(tree -> toSummary(tree, lang))
                .toList();

        return new TreePageDto(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );
    }

    public TreeFilterOptionsDto getFilterOptions() {
        return new TreeFilterOptionsDto(
                treeRepository.findDistinctFamilies(),
                treeRepository.findDistinctCategories(),
                treeRepository.findDistinctNativeStatuses()
        );
    }

    public List<TreeSummaryDto> searchTrees(String query, String languageCode) {
        if (query == null || query.isBlank()) {
            return getAllTrees(languageCode);
        }
        String lang = normalizeLanguage(languageCode);
        return treeRepository.searchPublished(query.trim()).stream()
                .map(tree -> toSummary(tree, lang))
                .toList();
    }

    public List<TreeMapMarkerDto> getMapMarkers(String languageCode) {
        String lang = normalizeLanguage(languageCode);
        return treeRepository.findAllPublishedWithDetails().stream()
                .filter(tree -> tree.getLatitude() != null && tree.getLongitude() != null)
                .map(tree -> toMapMarker(tree, lang))
                .toList();
    }

    public SiteStatsDto getSiteStats() {
        long documented = treeRepository.findAllPublishedWithDetails().size();
        long species = treeRepository.findAllPublishedWithDetails().stream()
                .map(Tree::getScientificName)
                .distinct()
                .count();
        return new SiteStatsDto(documented, 22000, species, 45, 5000, 3);
    }

    public TreeDetailDto getTreeById(Long id, String languageCode) {
        Tree tree = treeRepository.findPublishedByIdWithDetails(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));
        return toDetail(tree, normalizeLanguage(languageCode));
    }

    public TreeDetailDto getTreeBySlug(String slug, String languageCode) {
        Tree tree = treeRepository.findPublishedBySlugWithDetails(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));
        return toDetail(tree, normalizeLanguage(languageCode));
    }

    public TreeDetailDto getTreeByQrCodeId(String qrCodeId, String languageCode) {
        String code = qrCodeId == null ? "" : qrCodeId.trim();
        Tree tree = treeRepository.findPublishedByQrCodeIdWithDetails(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));
        return toDetail(tree, normalizeLanguage(languageCode));
    }

    public TreeDetailDto getTreeByAccessToken(String accessToken, String languageCode) {
        String token = accessToken == null ? "" : accessToken.trim();
        Tree tree = treeRepository.findPublishedByAccessTokenWithDetails(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));
        return toDetail(tree, normalizeLanguage(languageCode));
    }

    /** Admin: build detail DTO from a loaded tree (includes unpublished). */
    public TreeDetailDto getTreeDetailForAdmin(Tree tree, String languageCode) {
        return toDetail(tree, normalizeLanguage(languageCode));
    }

    private TreeSummaryDto toSummary(Tree tree, String languageCode) {
        TreeTranslation translation = resolveTranslation(tree, languageCode);
        String primaryImage = tree.getImages().stream()
                .filter(TreeImage::isPrimary)
                .findFirst()
                .or(() -> tree.getImages().stream().findFirst())
                .map(TreeImage::getUrl)
                .orElse(null);

        List<String> categories = tree.getCategories() == null
                ? List.of()
                : new ArrayList<>(tree.getCategories()).stream().sorted().toList();

        return new TreeSummaryDto(
                tree.getId(),
                tree.getScientificName(),
                tree.getSlug(),
                tree.getQrCodeId(),
                translation.getCommonName(),
                translation.getShortDescription(),
                tree.getFamily(),
                tree.getNativeStatus() == null ? "UNKNOWN" : tree.getNativeStatus(),
                categories,
                primaryImage,
                tree.getDisplayOrder()
        );
    }

    private TreeMapMarkerDto toMapMarker(Tree tree, String languageCode) {
        TreeTranslation translation = resolveTranslation(tree, languageCode);
        String primaryImage = tree.getImages().stream()
                .filter(TreeImage::isPrimary)
                .findFirst()
                .or(() -> tree.getImages().stream().findFirst())
                .map(TreeImage::getUrl)
                .orElse(null);

        return new TreeMapMarkerDto(
                tree.getId(),
                tree.getSlug(),
                tree.getQrCodeId(),
                translation.getCommonName(),
                tree.getScientificName(),
                tree.getFamily(),
                tree.getLatitude(),
                tree.getLongitude(),
                primaryImage
        );
    }

    private TreeDetailDto toDetail(Tree tree, String languageCode) {
        TreeTranslation translation = resolveTranslation(tree, languageCode);
        List<TreeImageDto> images = tree.getImages().stream()
                .sorted(Comparator.comparing(TreeImage::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .map(img -> new TreeImageDto(img.getId(), img.getUrl(), img.getCaption(), img.isPrimary(), img.getDisplayOrder()))
                .toList();

        List<String> availableLanguages = tree.getTranslations().stream()
                .map(TreeTranslation::getLanguageCode)
                .sorted()
                .toList();

        return new TreeDetailDto(
                tree.getId(),
                tree.getScientificName(),
                tree.getSlug(),
                tree.getQrCodeId(),
                tree.getFamily(),
                tree.getTypicalHeight(),
                tree.getOrigin(),
                tree.getAgeEstimate(),
                tree.getLatitude(),
                tree.getLongitude(),
                tree.getAudioUrl(),
                tree.getVideoUrl(),
                tree.isPublished(),
                translation.getLanguageCode(),
                translation.getCommonName(),
                translation.getShortDescription(),
                translation.getQuickFacts(),
                translation.getDescription(),
                translation.getUses(),
                translation.getEcologicalImportance(),
                translation.getBenefitsToPeopleAndWildlife(),
                translation.getCommonAreas(),
                translation.getAdditionalInfo(),
                translation.getInterestingFacts(),
                images,
                availableLanguages
        );
    }

    private TreeTranslation resolveTranslation(Tree tree, String languageCode) {
        return tree.getTranslations().stream()
                .filter(t -> t.getLanguageCode().equalsIgnoreCase(languageCode))
                .findFirst()
                .or(() -> tree.getTranslations().stream()
                        .filter(t -> t.getLanguageCode().equalsIgnoreCase(DEFAULT_LANGUAGE))
                        .findFirst())
                .or(() -> tree.getTranslations().stream().findFirst())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No translation available"));
    }

    private Sort catalogSort(String sortKey) {
        String key = sortKey == null ? "" : sortKey.trim().toLowerCase(Locale.ROOT);
        if ("az".equals(key)) {
            return Sort.by(Sort.Order.asc("scientificName"));
        }
        if ("za".equals(key)) {
            return Sort.by(Sort.Order.desc("scientificName"));
        }
        return Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.asc("scientificName"));
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
