package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Language;
import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.UserAccount;
import com.kigali.ecopark.repository.LanguageRepository;
import com.kigali.ecopark.repository.TreeRepository;
import com.kigali.ecopark.repository.UserAccountRepository;
import com.kigali.ecopark.service.TreeImageAcquisitionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            LanguageRepository languageRepository,
            TreeRepository treeRepository,
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            TreeImageAcquisitionService imageAcquisitionService,
            PlatformTransactionManager transactionManager,
            @Value("${app.api.public-base-url:http://localhost:8082}") String apiPublicBaseUrl
    ) {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return args -> {
            seedLanguagesIfNeeded(languageRepository);
            seedAdminUser(userAccountRepository, passwordEncoder);
            tx.executeWithoutResult(status ->
                    seedSyzygiumGuineense(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            tx.executeWithoutResult(status ->
                    seedFicusOvata(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            tx.executeWithoutResult(status ->
                    seedAeschynomeneElaphroxylon(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            tx.executeWithoutResult(status ->
                    seedAlbiziaVersicolor(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            tx.executeWithoutResult(status ->
                    seedBambusaVulgaris(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            tx.executeWithoutResult(status ->
                    seedErythrinaAbyssinica(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            tx.executeWithoutResult(status -> keepOnlyPublishedParkTrees(treeRepository));
        };
    }

    private void seedLanguagesIfNeeded(LanguageRepository languageRepository) {
        if (languageRepository.count() > 0) {
            return;
        }
        languageRepository.save(createLanguage("en", "English"));
        languageRepository.save(createLanguage("rw", "Kinyarwanda"));
        languageRepository.save(createLanguage("fr", "Français"));
    }

    private Language createLanguage(String code, String name) {
        Language language = new Language();
        language.setCode(code);
        language.setName(name);
        language.setActive(true);
        return language;
    }

    private void seedAdminUser(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        if (userAccountRepository.existsByEmailIgnoreCase("admin@ecopark.rw")) {
            return;
        }
        UserAccount admin = new UserAccount();
        admin.setFullName("Park Administrator");
        admin.setEmail("admin@ecopark.rw");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");
        userAccountRepository.save(admin);
    }

    void seedSyzygiumGuineense(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findPublishedBySlugWithDetails(SyzygiumGuineenseData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            SyzygiumGuineenseData.applyTo(tree, apiPublicBaseUrl);

            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    SyzygiumGuineenseData.SLUG,
                    SyzygiumGuineenseData.SCIENTIFIC_NAME,
                    SyzygiumGuineenseData.imageSources()
            );
            SyzygiumGuineenseData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            // Always refresh translations so name fixes (e.g. Umugote) apply on redeploy.
            SyzygiumGuineenseData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, SyzygiumGuineenseData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, SyzygiumGuineenseData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        SyzygiumGuineenseData.SLUG,
                        SyzygiumGuineenseData.SCIENTIFIC_NAME,
                        SyzygiumGuineenseData.imageSources()
                );
                SyzygiumGuineenseData.attachImages(tree, images);
            }
            treeRepository.save(tree);
        }
    }

    void seedFicusOvata(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(FicusOvataData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            FicusOvataData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    FicusOvataData.SLUG,
                    FicusOvataData.SCIENTIFIC_NAME,
                    FicusOvataData.imageSources()
            );
            FicusOvataData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            // Always rewrite audio/video to TREE-002-* so Umurehe cannot keep stale Umugote URLs.
            FicusOvataData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsImageRefresh(tree) || hasWrongSpeciesImages(tree, FicusOvataData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        FicusOvataData.SLUG,
                        FicusOvataData.SCIENTIFIC_NAME,
                        FicusOvataData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    FicusOvataData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedAeschynomeneElaphroxylon(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(AeschynomeneElaphroxylonData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            AeschynomeneElaphroxylonData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    AeschynomeneElaphroxylonData.SLUG,
                    AeschynomeneElaphroxylonData.SCIENTIFIC_NAME,
                    AeschynomeneElaphroxylonData.imageSources()
            );
            AeschynomeneElaphroxylonData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            AeschynomeneElaphroxylonData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsImageRefresh(tree) || hasWrongSpeciesImages(tree, AeschynomeneElaphroxylonData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        AeschynomeneElaphroxylonData.SLUG,
                        AeschynomeneElaphroxylonData.SCIENTIFIC_NAME,
                        AeschynomeneElaphroxylonData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    AeschynomeneElaphroxylonData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedAlbiziaVersicolor(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(AlbiziaVersicolorData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            AlbiziaVersicolorData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    AlbiziaVersicolorData.SLUG,
                    AlbiziaVersicolorData.SCIENTIFIC_NAME,
                    AlbiziaVersicolorData.imageSources()
            );
            AlbiziaVersicolorData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            AlbiziaVersicolorData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, AlbiziaVersicolorData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, AlbiziaVersicolorData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        AlbiziaVersicolorData.SLUG,
                        AlbiziaVersicolorData.SCIENTIFIC_NAME,
                        AlbiziaVersicolorData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    AlbiziaVersicolorData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedBambusaVulgaris(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(BambusaVulgarisData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            BambusaVulgarisData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    BambusaVulgarisData.SLUG,
                    BambusaVulgarisData.SCIENTIFIC_NAME,
                    BambusaVulgarisData.imageSources()
            );
            BambusaVulgarisData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            BambusaVulgarisData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, BambusaVulgarisData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, BambusaVulgarisData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        BambusaVulgarisData.SLUG,
                        BambusaVulgarisData.SCIENTIFIC_NAME,
                        BambusaVulgarisData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    BambusaVulgarisData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedErythrinaAbyssinica(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(ErythrinaAbyssinicaData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            ErythrinaAbyssinicaData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    ErythrinaAbyssinicaData.SLUG,
                    ErythrinaAbyssinicaData.SCIENTIFIC_NAME,
                    ErythrinaAbyssinicaData.imageSources()
            );
            ErythrinaAbyssinicaData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            ErythrinaAbyssinicaData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, ErythrinaAbyssinicaData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, ErythrinaAbyssinicaData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        ErythrinaAbyssinicaData.SLUG,
                        ErythrinaAbyssinicaData.SCIENTIFIC_NAME,
                        ErythrinaAbyssinicaData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    ErythrinaAbyssinicaData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    /** Keep published park guide trees; unpublish any other seeded leftovers. */
    private void keepOnlyPublishedParkTrees(TreeRepository treeRepository) {
        treeRepository.findAll().stream()
                .filter(t -> !SyzygiumGuineenseData.SLUG.equals(t.getSlug())
                        && !FicusOvataData.SLUG.equals(t.getSlug())
                        && !AeschynomeneElaphroxylonData.SLUG.equals(t.getSlug())
                        && !AlbiziaVersicolorData.SLUG.equals(t.getSlug())
                        && !BambusaVulgarisData.SLUG.equals(t.getSlug())
                        && !ErythrinaAbyssinicaData.SLUG.equals(t.getSlug()))
                .forEach(t -> {
                    t.setPublished(false);
                    treeRepository.save(t);
                });
    }

    private boolean needsImageRefresh(Tree tree) {
        if (tree.getImages() == null || tree.getImages().isEmpty()) {
            return true;
        }
        return tree.getImages().stream().anyMatch(img ->
                img.getUrl() == null
                        || img.getUrl().isBlank()
                        || img.getUrl().contains("localhost")
                        || img.getUrl().contains("127.0.0.1")
                        || img.getUrl().contains("/wiki/Special:FilePath")
        );
    }

    /** Re-point DB media URLs after deploy when bundled files change (e.g. TREE-002). */
    private boolean needsMediaUrlRefresh(Tree tree, String expectedPathFragment) {
        String audio = tree.getAudioUrl();
        return audio == null || !audio.contains(expectedPathFragment);
    }

    /** Prevent cross-tree gallery mix-ups (e.g. Syzygium photos on Ficus). */
    private boolean hasWrongSpeciesImages(Tree tree, String slug) {
        if (tree.getImages() == null || tree.getImages().isEmpty()) {
            return false;
        }
        return tree.getImages().stream().anyMatch(img -> {
            String url = img.getUrl() == null ? "" : img.getUrl().toLowerCase();
            if (FicusOvataData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("aeschynomene") || url.contains("albizia")
                        || url.contains("bambusa") || url.contains("erythrina");
            }
            if (SyzygiumGuineenseData.SLUG.equals(slug)) {
                return url.contains("ficus_ovata") || url.contains("ficus-ovata") || url.contains("aeschynomene")
                        || url.contains("albizia") || url.contains("bambusa") || url.contains("erythrina");
            }
            if (AeschynomeneElaphroxylonData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("albizia") || url.contains("bambusa") || url.contains("erythrina");
            }
            if (AlbiziaVersicolorData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("bambusa") || url.contains("erythrina");
            }
            if (BambusaVulgarisData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("erythrina");
            }
            if (ErythrinaAbyssinicaData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa");
            }
            return false;
        });
    }
}
