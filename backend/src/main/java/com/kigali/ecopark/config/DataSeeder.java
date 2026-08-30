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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    CommandLineRunner seedData(
            LanguageRepository languageRepository,
            TreeRepository treeRepository,
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            TreeImageAcquisitionService imageAcquisitionService,
            PlatformTransactionManager transactionManager,
            DataSource dataSource,
            com.kigali.ecopark.service.QrCodeService qrCodeService,
            @Value("${app.api.public-base-url:http://localhost:8082}") String apiPublicBaseUrl
    ) {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        return args -> {
            dropScientificNameUniqueConstraint(dataSource);
            seedLanguagesIfNeeded(languageRepository);
            seedAdminUser(userAccountRepository, passwordEncoder);
            seedSafely(tx, "TREE-001", () ->
                    seedSyzygiumGuineense(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-002", () ->
                    seedFicusOvata(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-003", () ->
                    seedAeschynomeneElaphroxylon(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-004", () ->
                    seedAlbiziaVersicolor(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-005", () ->
                    seedBambusaVulgaris(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-006", () ->
                    seedErythrinaAbyssinica(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-007", () ->
                    seedOleaEuropaeaSubspAfricana(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-008", () ->
                    seedSenegaliaPolyacanthaCampylacantha(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-009", () ->
                    seedEntadaAbyssinica(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-010", () ->
                    seedPhragmitesMauritianus(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-011", () ->
                    seedMaesaLanceolata(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-012", () ->
                    seedSenegaliaPolyacanthaRuganambuga(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "free-TREE-013", () -> freeRetiredDuplicateTree013(treeRepository));
            seedSafely(tx, "TREE-013", () ->
                    seedElaeisGuineensis(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-014", () ->
                    seedChrysophyllumGorungosanum(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-015", () ->
                    seedPhoenixReclinata(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-016", () ->
                    seedMillettiaLaurentii(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-017", () ->
                    seedFicusThonningii(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-018", () ->
                    seedTremaOrientalis(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-019", () ->
                    seedNewtoniaBuchananii(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-020", () ->
                    seedBlighiaUnijugata(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-021", () ->
                    seedCrotonMegalocarpus(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-022", () ->
                    seedEntadaAbyssinica022(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "TREE-023", () ->
                    seedBersamaAbyssinica(treeRepository, imageAcquisitionService, apiPublicBaseUrl));
            seedSafely(tx, "publish-park-trees", () -> keepOnlyPublishedParkTrees(treeRepository));
            seedSafely(tx, "qr-access-tokens", qrCodeService::ensureAccessTokensForAllPublished);
        };
    }

    private void seedSafely(TransactionTemplate tx, String label, Runnable work) {
        try {
            tx.executeWithoutResult(status -> work.run());
        } catch (Exception e) {
            log.error("Tree seed failed for {}: {}", label, e.getMessage(), e);
        }
    }

    /** Allow multiple park specimens of the same species (e.g. TREE-008 and TREE-012). */
    private void dropScientificNameUniqueConstraint(DataSource dataSource) {
        String sql = """
                DO $$
                DECLARE r RECORD;
                BEGIN
                  FOR r IN
                    SELECT c.conname
                    FROM pg_constraint c
                    JOIN pg_class t ON c.conrelid = t.oid
                    JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY (c.conkey)
                    WHERE t.relname = 'trees'
                      AND c.contype = 'u'
                      AND a.attname = 'scientific_name'
                      AND array_length(c.conkey, 1) = 1
                  LOOP
                    EXECUTE format('ALTER TABLE trees DROP CONSTRAINT %I', r.conname);
                  END LOOP;
                END $$;
                """;
        try (var conn = dataSource.getConnection(); var st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            log.warn("Could not drop scientific_name unique constraint: {}", e.getMessage());
        }
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
        // Always keep the known admin account usable after deploys/DB resets.
        UserAccount admin = userAccountRepository.findByEmailIgnoreCase("admin@treescan.rw")
                .or(() -> userAccountRepository.findByEmailIgnoreCase("admin@ecopark.rw"))
                .orElseGet(UserAccount::new);
        admin.setFullName("Tree Scan Administrator");
        admin.setEmail("admin@treescan.rw");
        admin.setPasswordHash(passwordEncoder.encode("123456"));
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

    void seedOleaEuropaeaSubspAfricana(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(OleaEuropaeaSubspAfricanaData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            OleaEuropaeaSubspAfricanaData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    OleaEuropaeaSubspAfricanaData.SLUG,
                    OleaEuropaeaSubspAfricanaData.SCIENTIFIC_NAME,
                    OleaEuropaeaSubspAfricanaData.imageSources()
            );
            OleaEuropaeaSubspAfricanaData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            OleaEuropaeaSubspAfricanaData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, OleaEuropaeaSubspAfricanaData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, OleaEuropaeaSubspAfricanaData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        OleaEuropaeaSubspAfricanaData.SLUG,
                        OleaEuropaeaSubspAfricanaData.SCIENTIFIC_NAME,
                        OleaEuropaeaSubspAfricanaData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    OleaEuropaeaSubspAfricanaData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedSenegaliaPolyacanthaCampylacantha(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(SenegaliaPolyacanthaCampylacanthaData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            SenegaliaPolyacanthaCampylacanthaData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    SenegaliaPolyacanthaCampylacanthaData.SLUG,
                    SenegaliaPolyacanthaCampylacanthaData.SCIENTIFIC_NAME,
                    SenegaliaPolyacanthaCampylacanthaData.imageSources()
            );
            SenegaliaPolyacanthaCampylacanthaData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            SenegaliaPolyacanthaCampylacanthaData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, SenegaliaPolyacanthaCampylacanthaData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, SenegaliaPolyacanthaCampylacanthaData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        SenegaliaPolyacanthaCampylacanthaData.SLUG,
                        SenegaliaPolyacanthaCampylacanthaData.SCIENTIFIC_NAME,
                        SenegaliaPolyacanthaCampylacanthaData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    SenegaliaPolyacanthaCampylacanthaData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedEntadaAbyssinica(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(EntadaAbyssinicaData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            EntadaAbyssinicaData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    EntadaAbyssinicaData.SLUG,
                    EntadaAbyssinicaData.SCIENTIFIC_NAME,
                    EntadaAbyssinicaData.imageSources()
            );
            EntadaAbyssinicaData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            EntadaAbyssinicaData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, EntadaAbyssinicaData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, EntadaAbyssinicaData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        EntadaAbyssinicaData.SLUG,
                        EntadaAbyssinicaData.SCIENTIFIC_NAME,
                        EntadaAbyssinicaData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    EntadaAbyssinicaData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedPhragmitesMauritianus(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(PhragmitesMauritianusData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            PhragmitesMauritianusData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    PhragmitesMauritianusData.SLUG,
                    PhragmitesMauritianusData.SCIENTIFIC_NAME,
                    PhragmitesMauritianusData.imageSources()
            );
            PhragmitesMauritianusData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            PhragmitesMauritianusData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, PhragmitesMauritianusData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, PhragmitesMauritianusData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        PhragmitesMauritianusData.SLUG,
                        PhragmitesMauritianusData.SCIENTIFIC_NAME,
                        PhragmitesMauritianusData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    PhragmitesMauritianusData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedMaesaLanceolata(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(MaesaLanceolataData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            MaesaLanceolataData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    MaesaLanceolataData.SLUG,
                    MaesaLanceolataData.SCIENTIFIC_NAME,
                    MaesaLanceolataData.imageSources()
            );
            MaesaLanceolataData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            MaesaLanceolataData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, MaesaLanceolataData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, MaesaLanceolataData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        MaesaLanceolataData.SLUG,
                        MaesaLanceolataData.SCIENTIFIC_NAME,
                        MaesaLanceolataData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    MaesaLanceolataData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedSenegaliaPolyacanthaRuganambuga(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(SenegaliaPolyacanthaRuganambugaData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            SenegaliaPolyacanthaRuganambugaData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    SenegaliaPolyacanthaRuganambugaData.SLUG,
                    SenegaliaPolyacanthaRuganambugaData.SCIENTIFIC_NAME,
                    SenegaliaPolyacanthaRuganambugaData.imageSources()
            );
            SenegaliaPolyacanthaRuganambugaData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            SenegaliaPolyacanthaRuganambugaData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, SenegaliaPolyacanthaRuganambugaData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, SenegaliaPolyacanthaRuganambugaData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        SenegaliaPolyacanthaRuganambugaData.SLUG,
                        SenegaliaPolyacanthaRuganambugaData.SCIENTIFIC_NAME,
                        SenegaliaPolyacanthaRuganambugaData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    SenegaliaPolyacanthaRuganambugaData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    /**
     * TREE-013 was previously a duplicate of TREE-008. Reuse that ID for Ikigazi
     * by freeing the retired duplicate row first (qr_code_id is unique).
     */
    private void freeRetiredDuplicateTree013(TreeRepository treeRepository) {
        treeRepository.findByQrCodeIdWithDetails("TREE-013").ifPresent(tree -> {
            if (ElaeisGuineensisData.SLUG.equals(tree.getSlug())
                    || ChrysophyllumGorungosanumData.SLUG.equals(tree.getSlug())) {
                return;
            }
            tree.setPublished(false);
            tree.setQrCodeId("TREE-013-RETIRED");
            treeRepository.saveAndFlush(tree);
            log.info("Freed TREE-013 from retired duplicate {}.", tree.getSlug());
        });
        treeRepository.findBySlugWithDetails("senegalia-polyacantha-tree-013").ifPresent(tree -> {
            tree.setPublished(false);
            if ("TREE-013".equalsIgnoreCase(tree.getQrCodeId())) {
                tree.setQrCodeId("TREE-013-RETIRED");
            }
            treeRepository.saveAndFlush(tree);
        });
    }

    void seedElaeisGuineensis(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(ElaeisGuineensisData.SLUG);
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            ElaeisGuineensisData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    ElaeisGuineensisData.SLUG,
                    ElaeisGuineensisData.SCIENTIFIC_NAME,
                    ElaeisGuineensisData.imageSources()
            );
            ElaeisGuineensisData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            ElaeisGuineensisData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, ElaeisGuineensisData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, ElaeisGuineensisData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        ElaeisGuineensisData.SLUG,
                        ElaeisGuineensisData.SCIENTIFIC_NAME,
                        ElaeisGuineensisData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    ElaeisGuineensisData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedChrysophyllumGorungosanum(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(ChrysophyllumGorungosanumData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(ChrysophyllumGorungosanumData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            ChrysophyllumGorungosanumData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    ChrysophyllumGorungosanumData.SLUG,
                    ChrysophyllumGorungosanumData.SCIENTIFIC_NAME,
                    ChrysophyllumGorungosanumData.imageSources()
            );
            ChrysophyllumGorungosanumData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            ChrysophyllumGorungosanumData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, ChrysophyllumGorungosanumData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, ChrysophyllumGorungosanumData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        ChrysophyllumGorungosanumData.SLUG,
                        ChrysophyllumGorungosanumData.SCIENTIFIC_NAME,
                        ChrysophyllumGorungosanumData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    ChrysophyllumGorungosanumData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedPhoenixReclinata(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(PhoenixReclinataData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(PhoenixReclinataData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            PhoenixReclinataData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    PhoenixReclinataData.SLUG,
                    PhoenixReclinataData.SCIENTIFIC_NAME,
                    PhoenixReclinataData.imageSources()
            );
            PhoenixReclinataData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            PhoenixReclinataData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, PhoenixReclinataData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, PhoenixReclinataData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        PhoenixReclinataData.SLUG,
                        PhoenixReclinataData.SCIENTIFIC_NAME,
                        PhoenixReclinataData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    PhoenixReclinataData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedMillettiaLaurentii(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(MillettiaLaurentiiData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(MillettiaLaurentiiData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            MillettiaLaurentiiData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    MillettiaLaurentiiData.SLUG,
                    MillettiaLaurentiiData.SCIENTIFIC_NAME,
                    MillettiaLaurentiiData.imageSources()
            );
            MillettiaLaurentiiData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            MillettiaLaurentiiData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, MillettiaLaurentiiData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, MillettiaLaurentiiData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        MillettiaLaurentiiData.SLUG,
                        MillettiaLaurentiiData.SCIENTIFIC_NAME,
                        MillettiaLaurentiiData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    MillettiaLaurentiiData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedFicusThonningii(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(FicusThonningiiData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(FicusThonningiiData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            FicusThonningiiData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    FicusThonningiiData.SLUG,
                    FicusThonningiiData.SCIENTIFIC_NAME,
                    FicusThonningiiData.imageSources()
            );
            FicusThonningiiData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            FicusThonningiiData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, FicusThonningiiData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, FicusThonningiiData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        FicusThonningiiData.SLUG,
                        FicusThonningiiData.SCIENTIFIC_NAME,
                        FicusThonningiiData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    FicusThonningiiData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedTremaOrientalis(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(TremaOrientalisData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(TremaOrientalisData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            TremaOrientalisData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    TremaOrientalisData.SLUG,
                    TremaOrientalisData.SCIENTIFIC_NAME,
                    TremaOrientalisData.imageSources()
            );
            TremaOrientalisData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            TremaOrientalisData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, TremaOrientalisData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, TremaOrientalisData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        TremaOrientalisData.SLUG,
                        TremaOrientalisData.SCIENTIFIC_NAME,
                        TremaOrientalisData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    TremaOrientalisData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedNewtoniaBuchananii(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(NewtoniaBuchananiiData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(NewtoniaBuchananiiData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            NewtoniaBuchananiiData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    NewtoniaBuchananiiData.SLUG,
                    NewtoniaBuchananiiData.SCIENTIFIC_NAME,
                    NewtoniaBuchananiiData.imageSources()
            );
            NewtoniaBuchananiiData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            NewtoniaBuchananiiData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, NewtoniaBuchananiiData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, NewtoniaBuchananiiData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        NewtoniaBuchananiiData.SLUG,
                        NewtoniaBuchananiiData.SCIENTIFIC_NAME,
                        NewtoniaBuchananiiData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    NewtoniaBuchananiiData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedBlighiaUnijugata(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(BlighiaUnijugataData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(BlighiaUnijugataData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            BlighiaUnijugataData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    BlighiaUnijugataData.SLUG,
                    BlighiaUnijugataData.SCIENTIFIC_NAME,
                    BlighiaUnijugataData.imageSources()
            );
            BlighiaUnijugataData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            BlighiaUnijugataData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, BlighiaUnijugataData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, BlighiaUnijugataData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        BlighiaUnijugataData.SLUG,
                        BlighiaUnijugataData.SCIENTIFIC_NAME,
                        BlighiaUnijugataData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    BlighiaUnijugataData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedCrotonMegalocarpus(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(CrotonMegalocarpusData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(CrotonMegalocarpusData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            CrotonMegalocarpusData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    CrotonMegalocarpusData.SLUG,
                    CrotonMegalocarpusData.SCIENTIFIC_NAME,
                    CrotonMegalocarpusData.imageSources()
            );
            CrotonMegalocarpusData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            CrotonMegalocarpusData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, CrotonMegalocarpusData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, CrotonMegalocarpusData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        CrotonMegalocarpusData.SLUG,
                        CrotonMegalocarpusData.SCIENTIFIC_NAME,
                        CrotonMegalocarpusData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    CrotonMegalocarpusData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedBersamaAbyssinica(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(BersamaAbyssinicaData.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(BersamaAbyssinicaData.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            BersamaAbyssinicaData.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    BersamaAbyssinicaData.SLUG,
                    BersamaAbyssinicaData.SCIENTIFIC_NAME,
                    BersamaAbyssinicaData.imageSources()
            );
            BersamaAbyssinicaData.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            BersamaAbyssinicaData.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, BersamaAbyssinicaData.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, BersamaAbyssinicaData.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        BersamaAbyssinicaData.SLUG,
                        BersamaAbyssinicaData.SCIENTIFIC_NAME,
                        BersamaAbyssinicaData.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    BersamaAbyssinicaData.attachImages(tree, images);
                }
            }
            treeRepository.save(tree);
        }
    }

    void seedEntadaAbyssinica022(
            TreeRepository treeRepository,
            TreeImageAcquisitionService imageAcquisitionService,
            String apiPublicBaseUrl
    ) {
        var existing = treeRepository.findBySlugWithDetails(EntadaAbyssinica022Data.SLUG)
                .or(() -> treeRepository.findByQrCodeIdWithDetails(EntadaAbyssinica022Data.QR_CODE_ID));
        if (existing.isEmpty()) {
            Tree tree = new Tree();
            EntadaAbyssinica022Data.applyTo(tree, apiPublicBaseUrl);
            List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                    EntadaAbyssinica022Data.SLUG,
                    EntadaAbyssinica022Data.SCIENTIFIC_NAME,
                    EntadaAbyssinica022Data.imageSources()
            );
            EntadaAbyssinica022Data.attachImages(tree, images);
            treeRepository.save(tree);
        } else {
            Tree tree = existing.get();
            EntadaAbyssinica022Data.refreshExisting(tree, apiPublicBaseUrl);
            if (needsMediaUrlRefresh(tree, EntadaAbyssinica022Data.AUDIO_BASE_PATH)
                    || needsImageRefresh(tree)
                    || hasWrongSpeciesImages(tree, EntadaAbyssinica022Data.SLUG)) {
                List<TreeImageAcquisitionService.AcquiredImage> images = imageAcquisitionService.acquireImages(
                        EntadaAbyssinica022Data.SLUG,
                        EntadaAbyssinica022Data.SCIENTIFIC_NAME,
                        EntadaAbyssinica022Data.imageSources()
                );
                if (!images.isEmpty()) {
                    tree.getImages().clear();
                    treeRepository.saveAndFlush(tree);
                    EntadaAbyssinica022Data.attachImages(tree, images);
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
                        && !ErythrinaAbyssinicaData.SLUG.equals(t.getSlug())
                        && !OleaEuropaeaSubspAfricanaData.SLUG.equals(t.getSlug())
                        && !SenegaliaPolyacanthaCampylacanthaData.SLUG.equals(t.getSlug())
                        && !EntadaAbyssinicaData.SLUG.equals(t.getSlug())
                        && !PhragmitesMauritianusData.SLUG.equals(t.getSlug())
                        && !MaesaLanceolataData.SLUG.equals(t.getSlug())
                        && !SenegaliaPolyacanthaRuganambugaData.SLUG.equals(t.getSlug())
                        && !ElaeisGuineensisData.SLUG.equals(t.getSlug())
                        && !ChrysophyllumGorungosanumData.SLUG.equals(t.getSlug())
                        && !PhoenixReclinataData.SLUG.equals(t.getSlug())
                        && !MillettiaLaurentiiData.SLUG.equals(t.getSlug())
                        && !FicusThonningiiData.SLUG.equals(t.getSlug())
                        && !TremaOrientalisData.SLUG.equals(t.getSlug())
                        && !NewtoniaBuchananiiData.SLUG.equals(t.getSlug())
                        && !BlighiaUnijugataData.SLUG.equals(t.getSlug())
                        && !CrotonMegalocarpusData.SLUG.equals(t.getSlug())
                        && !EntadaAbyssinica022Data.SLUG.equals(t.getSlug())
                        && !BersamaAbyssinicaData.SLUG.equals(t.getSlug()))
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
                        || url.contains("bambusa") || url.contains("erythrina") || url.contains("olea")
                        || url.contains("acacia_polyacantha") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (SyzygiumGuineenseData.SLUG.equals(slug)) {
                return url.contains("ficus_ovata") || url.contains("ficus-ovata") || url.contains("aeschynomene")
                        || url.contains("albizia") || url.contains("bambusa") || url.contains("erythrina")
                        || url.contains("olea") || url.contains("acacia_polyacantha") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (AeschynomeneElaphroxylonData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("albizia") || url.contains("bambusa") || url.contains("erythrina")
                        || url.contains("olea") || url.contains("acacia_polyacantha") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (AlbiziaVersicolorData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("bambusa") || url.contains("erythrina")
                        || url.contains("olea") || url.contains("acacia_polyacantha") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (BambusaVulgarisData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("erythrina")
                        || url.contains("olea") || url.contains("acacia_polyacantha") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (ErythrinaAbyssinicaData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("olea") || url.contains("acacia_polyacantha") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (OleaEuropaeaSubspAfricanaData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("acacia_polyacantha") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (SenegaliaPolyacanthaCampylacanthaData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (EntadaAbyssinicaData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (PhragmitesMauritianusData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("maesa_lanceolata");
            }
            if (MaesaLanceolataData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus");
            }
            if (SenegaliaPolyacanthaRuganambugaData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("entada_abyssinica")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata");
            }
            if (ElaeisGuineensisData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata");
            }
            if (ChrysophyllumGorungosanumData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata") || url.contains("elaeis")
                        || url.contains("vachellia") || url.contains("acacia_abyssinica") || url.contains("flat-top")
                        || url.contains("phoenix");
            }
            if (PhoenixReclinataData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata") || url.contains("elaeis")
                        || url.contains("chrysophyllum") || url.contains("143740") || url.contains("vachellia")
                        || url.contains("milllaur") || url.contains("wenge");
            }
            if (MillettiaLaurentiiData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata") || url.contains("elaeis")
                        || url.contains("chrysophyllum") || url.contains("phoenix") || url.contains("143740");
            }
            if (FicusThonningiiData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata") || url.contains("elaeis")
                        || url.contains("chrysophyllum") || url.contains("phoenix") || url.contains("143740")
                        || url.contains("milllaur") || url.contains("wenge")
                        || url.contains("trema_orientalis") || url.contains("126400");
            }
            if (TremaOrientalisData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata") || url.contains("elaeis")
                        || url.contains("chrysophyllum") || url.contains("phoenix") || url.contains("143740")
                        || url.contains("milllaur") || url.contains("wenge")
                        || url.contains("ficus_thonningii") || url.contains("mulemba")
                        || url.contains("126400") || url.contains("newtonia");
            }
            if (NewtoniaBuchananiiData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata") || url.contains("elaeis")
                        || url.contains("chrysophyllum") || url.contains("phoenix") || url.contains("143740")
                        || url.contains("milllaur") || url.contains("wenge")
                        || url.contains("ficus_thonningii") || url.contains("mulemba")
                        || url.contains("trema_orientalis") || url.contains("blighia") || url.contains("137480");
            }
            if (BlighiaUnijugataData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata") || url.contains("elaeis")
                        || url.contains("chrysophyllum") || url.contains("phoenix") || url.contains("143740")
                        || url.contains("milllaur") || url.contains("wenge")
                        || url.contains("ficus_thonningii") || url.contains("mulemba")
                        || url.contains("trema_orientalis") || url.contains("126400") || url.contains("newtonia")
                        || url.contains("croton");
            }
            if (CrotonMegalocarpusData.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("entada_abyssinica") || url.contains("phragmites_mauritianus")
                        || url.contains("maesa_lanceolata") || url.contains("elaeis")
                        || url.contains("chrysophyllum") || url.contains("phoenix") || url.contains("143740")
                        || url.contains("milllaur") || url.contains("wenge")
                        || url.contains("ficus_thonningii") || url.contains("mulemba")
                        || url.contains("trema_orientalis") || url.contains("126400") || url.contains("newtonia")
                        || url.contains("blighia") || url.contains("137480");
            }
            if (EntadaAbyssinica022Data.SLUG.equals(slug)) {
                return url.contains("syzygium") || url.contains("ficus_ovata") || url.contains("ficus-ovata")
                        || url.contains("aeschynomene") || url.contains("albizia") || url.contains("bambusa")
                        || url.contains("erythrina") || url.contains("olea") || url.contains("acacia_polyacantha")
                        || url.contains("phragmites_mauritianus") || url.contains("maesa_lanceolata")
                        || url.contains("elaeis") || url.contains("chrysophyllum") || url.contains("phoenix")
                        || url.contains("143740") || url.contains("milllaur") || url.contains("wenge")
                        || url.contains("ficus_thonningii") || url.contains("mulemba")
                        || url.contains("trema_orientalis") || url.contains("126400") || url.contains("newtonia")
                        || url.contains("blighia") || url.contains("137480") || url.contains("croton");
            }
            return false;
        });
    }
}
