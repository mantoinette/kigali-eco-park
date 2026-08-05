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
            SyzygiumGuineenseData.applyMetadata(tree, apiPublicBaseUrl);
            if (needsImageRefresh(tree)) {
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
            FicusOvataData.refreshExisting(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    FicusOvataData.SLUG,
                    FicusOvataData.SCIENTIFIC_NAME,
                    FicusOvataData.imageSources()
            );
            if (!images.isEmpty()) {
                // Clear then flush orphans before insert — avoids duplicate-key races on refresh
                tree.getImages().clear();
                treeRepository.saveAndFlush(tree);
                FicusOvataData.attachImages(tree, images);
            }
            treeRepository.save(tree);
        }
    }

    /** Keep Umugote + Umurehe published; unpublish any other seeded leftovers. */
    private void keepOnlyPublishedParkTrees(TreeRepository treeRepository) {
        treeRepository.findAll().stream()
                .filter(t -> !SyzygiumGuineenseData.SLUG.equals(t.getSlug())
                        && !FicusOvataData.SLUG.equals(t.getSlug()))
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
        );
    }
}
