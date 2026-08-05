package com.kigali.ecopark.controller;

import com.kigali.ecopark.dto.SiteStatsDto;
import com.kigali.ecopark.dto.TreeDetailDto;
import com.kigali.ecopark.dto.TreeFilterOptionsDto;
import com.kigali.ecopark.dto.TreeMapMarkerDto;
import com.kigali.ecopark.dto.TreePageDto;
import com.kigali.ecopark.dto.TreeSummaryDto;
import com.kigali.ecopark.service.TreeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trees")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping
    public List<TreeSummaryDto> getAllTrees(@RequestParam(defaultValue = "en") String lang) {
        return treeService.getAllTrees(lang);
    }

    /** Paginated Explore Trees catalog — search + filters, safe for 200+ trees. */
    @GetMapping("/catalog")
    public TreePageDto browseCatalog(
            @RequestParam(defaultValue = "en") String lang,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String nativeStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return treeService.browseCatalog(lang, q, family, category, nativeStatus, page, size);
    }

    @GetMapping("/filters")
    public TreeFilterOptionsDto getFilterOptions() {
        return treeService.getFilterOptions();
    }

    @GetMapping("/search")
    public List<TreeSummaryDto> searchTrees(
            @RequestParam String q,
            @RequestParam(defaultValue = "en") String lang
    ) {
        return treeService.searchTrees(q, lang);
    }

    @GetMapping("/map")
    public List<TreeMapMarkerDto> getMapMarkers(@RequestParam(defaultValue = "en") String lang) {
        return treeService.getMapMarkers(lang);
    }

    @GetMapping("/stats")
    public SiteStatsDto getSiteStats() {
        return treeService.getSiteStats();
    }

    @GetMapping("/{id}")
    public TreeDetailDto getTreeById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang
    ) {
        return treeService.getTreeById(id, lang);
    }

    @GetMapping("/slug/{slug}")
    public TreeDetailDto getTreeBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "en") String lang
    ) {
        return treeService.getTreeBySlug(slug, lang);
    }

    @GetMapping("/qr/{qrCodeId}")
    public TreeDetailDto getTreeByQrCode(
            @PathVariable String qrCodeId,
            @RequestParam(defaultValue = "en") String lang
    ) {
        return treeService.getTreeByQrCodeId(qrCodeId, lang);
    }
}
