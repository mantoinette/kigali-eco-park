package com.kigali.ecopark.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Maps legacy species filenames to unique per-tree media (TREE-001 … TREE-008).
 */
@RestController
public class MediaAliasController {

    @GetMapping("/media/audio/{fileName}")
    public ResponseEntity<Resource> audio(@PathVariable String fileName) {
        return media("audio", fileName, MediaType.valueOf("audio/mpeg"));
    }

    @GetMapping("/media/video/{fileName}")
    public ResponseEntity<Resource> video(@PathVariable String fileName) {
        return media("video", fileName, MediaType.valueOf("video/mp4"));
    }

    private ResponseEntity<Resource> media(String kind, String fileName, MediaType type) {
        String resolved = resolveCanonicalName(fileName);
        Resource resource = new ClassPathResource("static/media/" + kind + "/" + resolved);
        if (!resource.exists()) {
            resource = new ClassPathResource("static/media/" + kind + "/" + fileName);
        }
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(type)
                .header("Cache-Control", "no-store, max-age=0")
                .header("X-Tree-Media", resolved)
                .body(resource);
    }

    /** Old species names → unique park tree IDs. */
    static String resolveCanonicalName(String fileName) {
        String lower = fileName == null ? "" : fileName.toLowerCase();
        if (lower.startsWith("tree-001-") || lower.startsWith("tree-002-") || lower.startsWith("tree-003-")
                || lower.startsWith("tree-004-") || lower.startsWith("tree-005-") || lower.startsWith("tree-006-")
                || lower.startsWith("tree-007-") || lower.startsWith("tree-008-")) {
            return fileName;
        }
        if (lower.startsWith("syzygium-guineense-")) {
            return "TREE-001-" + fileName.substring("syzygium-guineense-".length());
        }
        if (lower.startsWith("ficus-ovata-v2-")) {
            return "TREE-002-" + fileName.substring("ficus-ovata-v2-".length());
        }
        if (lower.startsWith("ficus-ovata-")) {
            return "TREE-002-" + fileName.substring("ficus-ovata-".length());
        }
        if (lower.startsWith("aeschynomene-elaphroxylon-")) {
            return "TREE-003-" + fileName.substring("aeschynomene-elaphroxylon-".length());
        }
        if (lower.startsWith("albizia-versicolor-")) {
            return "TREE-004-" + fileName.substring("albizia-versicolor-".length());
        }
        if (lower.startsWith("bambusa-vulgaris-")) {
            return "TREE-005-" + fileName.substring("bambusa-vulgaris-".length());
        }
        if (lower.startsWith("erythrina-abyssinica-")) {
            return "TREE-006-" + fileName.substring("erythrina-abyssinica-".length());
        }
        if (lower.startsWith("olea-europaea-subsp-africana-")) {
            return "TREE-007-" + fileName.substring("olea-europaea-subsp-africana-".length());
        }
        if (lower.startsWith("senegalia-polyacantha-campylacantha-")) {
            return "TREE-008-" + fileName.substring("senegalia-polyacantha-campylacantha-".length());
        }
        return fileName;
    }
}
