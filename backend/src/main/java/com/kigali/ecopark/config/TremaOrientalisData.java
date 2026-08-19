package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Trema orientalis (Umudobori / pigeon wood) — TREE-018.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class TremaOrientalisData {

    public static final String SLUG = "trema-orientalis";
    public static final String SCIENTIFIC_NAME = "Trema orientalis";
    public static final String QR_CODE_ID = "TREE-018";
    public static final String FAMILY = "Cannabaceae (Hemp family)";
    public static final String TYPICAL_HEIGHT = "Pioneer tree to about 15 m";
    public static final String ORIGIN = "Tropical and subtropical Africa and Asia";
    public static final String AGE_ESTIMATE = "Approx. 10–30 years (park specimen)";
    public static final double LATITUDE = -1.9676;
    public static final double LONGITUDE = 30.1106;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-018";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-018";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Trema_orientalis";

    private TremaOrientalisData() {}

    public static void applyTo(Tree tree, String apiPublicBaseUrl) {
        applyMetadata(tree, apiPublicBaseUrl);
        tree.getTranslations().clear();
        tree.getTranslations().add(english(tree));
        tree.getTranslations().add(kinyarwanda(tree));
        tree.getTranslations().add(french(tree));
    }

    public static void refreshExisting(Tree tree, String apiPublicBaseUrl) {
        applyMetadata(tree, apiPublicBaseUrl);
        upsertTranslation(tree, english(tree));
        upsertTranslation(tree, kinyarwanda(tree));
        upsertTranslation(tree, french(tree));
    }

    private static void upsertTranslation(Tree tree, TreeTranslation fresh) {
        for (TreeTranslation existing : tree.getTranslations()) {
            if (fresh.getLanguageCode().equalsIgnoreCase(existing.getLanguageCode())) {
                copyContent(fresh, existing);
                return;
            }
        }
        tree.getTranslations().add(fresh);
    }

    private static void copyContent(TreeTranslation from, TreeTranslation to) {
        to.setCommonName(from.getCommonName());
        to.setShortDescription(from.getShortDescription());
        to.setInterestingFacts(from.getInterestingFacts());
        to.setQuickFacts(from.getQuickFacts());
        to.setDescription(from.getDescription());
        to.setUses(from.getUses());
        to.setEcologicalImportance(from.getEcologicalImportance());
        to.setBenefitsToPeopleAndWildlife(from.getBenefitsToPeopleAndWildlife());
        to.setCommonAreas(from.getCommonAreas());
        to.setAdditionalInfo(from.getAdditionalInfo());
    }

    public static void applyMetadata(Tree tree, String apiPublicBaseUrl) {
        tree.setScientificName(SCIENTIFIC_NAME);
        tree.setSlug(SLUG);
        tree.setQrCodeId(QR_CODE_ID);
        tree.setFamily(FAMILY);
        tree.setTypicalHeight(TYPICAL_HEIGHT);
        tree.setOrigin(ORIGIN);
        tree.setAgeEstimate(AGE_ESTIMATE);
        tree.setLatitude(LATITUDE);
        tree.setLongitude(LONGITUDE);
        tree.setNativeStatus("NATIVE");
        tree.getCategories().clear();
        tree.getCategories().addAll(List.of("MEDICINAL", "TIMBER", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(18);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/2/26/Starr_070321-5915_Trema_orientalis.jpg",
                        "Trema orientalis — pigeon wood (Umudobori)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/2/24/Trema_orientalis_6.jpg",
                        "Gunpowder tree habit and foliage",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/a/a2/Trema_orientalis_4.jpg",
                        "Trema orientalis leaves and branches",
                        false,
                        3
                )
        );
    }

    public static void attachImages(Tree tree, List<TreeImageAcquisitionService.AcquiredImage> acquiredImages) {
        tree.getImages().clear();
        boolean first = true;
        for (TreeImageAcquisitionService.AcquiredImage acquired : acquiredImages) {
            tree.getImages().add(image(
                    tree,
                    acquired.publicUrl(),
                    acquired.caption(),
                    first || acquired.primary(),
                    acquired.displayOrder()
            ));
            first = false;
        }
    }

    private static TreeTranslation english(Tree tree) {
        TreeTranslation t = base(tree, "en", "Pigeon wood");
        t.setShortDescription(
                "Trema orientalis — pigeon wood or gunpowder tree, known in Kinyarwanda as Umudobori. " +
                "A fast-growing pioneer used for firewood; leaves and bark used in traditional medicine for respiratory ailments."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umudobori (also Umugwamporo / Umurwamporo).",
                "Also called pigeon wood or gunpowder tree.",
                "Fast-growing pioneer species.",
                "Firewood is a major local use.",
                "Leaves and bark used for coughs, sore throat, asthma and bronchitis.",
                "Family: Cannabaceae (hemp family)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Cannabaceae (Hemp family)",
                "Scientific name: Trema orientalis (L.) Blume",
                "Common name: Pigeon wood · Gunpowder tree",
                "Local name (Kinyarwanda): Umudobori",
                "Typical height: pioneer tree to about 15 m",
                "Distribution: tropical and subtropical Africa and Asia",
                "Park ID: TREE-018"
        ));
        t.setDescription(
                "Trema orientalis is a widespread pioneer tree of the family Cannabaceae. In Rwanda it is known " +
                "as Umudobori (also Umugwamporo or Umurwamporo). It colonises disturbed ground and grows quickly.\n\n" +
                "The wood is widely used as firewood. In traditional medicine, leaves and bark are used to treat coughs, " +
                "sore throat, asthma and bronchitis.\n\n" +
                "At Kigali Eco-Park this TREE-018 specimen is presented as Umudobori / pigeon wood, matching local naming " +
                "and documented uses."
        );
        t.setUses(
                "Firewood: Widely used for cooking and heating.\n\n" +
                "Traditional medicine: Leaves and bark for coughs, sore throat, asthma and bronchitis."
        );
        t.setEcologicalImportance(
                "A pioneer species that stabilises disturbed soils and provides early cover; seeds and foliage support birds " +
                "and insects in secondary woodland."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Firewood and traditional medicine for respiratory complaints.\n\n" +
                "For wildlife: Seeds, cover and pioneer habitat for birds and insects."
        );
        t.setCommonAreas(
                "Common in tropical and subtropical Africa and Asia, including Rwanda — forest edges, fallow land and villages."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umudobori (TREE-018) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: pigeon wood · gunpowder tree · Umudobori · Cannabaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umudobori");
        t.setShortDescription(
                "Umudobori (Trema orientalis / pigeon wood) — igiti cy'inkwi zo gucana. " +
                "Amababi n'igishishwa cy'igiti bikoreshwa mu buvuzi gakondo bw'indwara z'ubuhumekero."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umudobori (Umugwamporo / Umurwamporo).",
                "Inkwi zo gucana.",
                "Amababi n'igishishwa cy'igiti bivura inkorora.",
                "Bikoreshwa mu kuvura asima n'indwara z'ubuhumekero.",
                "Umuryango: Cannabaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Cannabaceae",
                "Izina ry'ubumenyi: Trema orientalis",
                "Izina ry'ikinyarwanda: Umudobori",
                "Izina ry'icyongereza: Pigeon wood",
                "Uburebure: igiti cyihuta gukura kigeza metero 15",
                "Ikimenyetso: TREE-018"
        ));
        t.setDescription(
                "Trema orientalis ni Umudobori — igiti cy'umuryango wa Cannabaceae gikunze gukura vuba mu " +
                "bibanza byangiritse.\n\n" +
                "Inkwi zo gucana ni akamaro kanini. Mu buvuzi gakondo, amababi n'igishishwa cy'igiti bikoreshwa " +
                "mu kuvura inkorora, kubabara mu mihogo, asima n'indwara z'ubuhumekero.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umudobori / pigeon wood (TREE-018) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Inkwi zo gucana.\n\n" +
                "Ubuvuzi gakondo:\n" +
                "• Amababi n'igishishwa cy'igiti bivura inkorora, kubabara mu mihogo, asima " +
                "n'indwara z'ubuhumekero (bronchiite)."
        );
        t.setEcologicalImportance(
                "Umudobori ni igiti cy'imbere mu gukosora ubutaka n' gutanga ubuturo ku nyamaswa n'inyoni."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Inkwi zo gucana n'ubuvuzi gakondo bw'ubuhumekero.\n\n" +
                "Ku nyamaswa: Imbuto, ubusitani bw'imbere n'ubuturo."
        );
        t.setCommonAreas(
                "Gikunze mu Rwanda no mu Afurika y'ubushyuhe — ku mpera z'imyaka, mu bibanza byangiritse n'imidugudu."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umudobori (TREE-018) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umudobori · Trema orientalis · Pigeon wood · Cannabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Charbonnier");
        t.setShortDescription(
                "Trema orientalis — charbonnier ou arbre à poudre, appelé Umudobori en kinyarwanda. " +
                "Bois de chauffe ; feuilles et écorce pour toux, mal de gorge, asthme et bronchite."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umudobori.",
                "Bois de chauffe.",
                "Médecine traditionnelle respiratoire.",
                "Espèce pionnière à croissance rapide.",
                "Famille : Cannabaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Cannabaceae",
                "Nom scientifique : Trema orientalis",
                "Nom commun : Charbonnier",
                "Nom local : Umudobori",
                "Hauteur : jusqu'à environ 15 m",
                "Identifiant parc : TREE-018"
        ));
        t.setDescription(
                "Trema orientalis est un arbre pionnier répandu. Au Rwanda : Umudobori.\n\n" +
                "Usages : bois de chauffe ; médecine traditionnelle pour les voies respiratoires.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umudobori selon les usages locaux."
        );
        t.setUses(
                "Bois de chauffe.\n\n" +
                "Médecine traditionnelle : feuilles et écorce pour toux, mal de gorge, asthme et bronchite."
        );
        t.setEcologicalImportance(
                "Espèce pionnière stabilisant les sols perturbés ; graines et couvert pour la faune."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Bois de chauffe et médecine traditionnelle.\n\n" +
                "Pour la faune : Graines et habitat pionnier."
        );
        t.setCommonAreas(
                "Afrique tropicale et subtropicale, y compris le Rwanda."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umudobori pour photos, carte et médias.\n\n" +
                "Umudobori · Trema orientalis · Charbonnier · Cannabaceae."
        );
        return t;
    }

    private static TreeTranslation base(Tree tree, String lang, String commonName) {
        TreeTranslation t = new TreeTranslation();
        t.setTree(tree);
        t.setLanguageCode(lang);
        t.setCommonName(commonName);
        return t;
    }

    private static TreeImage image(Tree tree, String url, String caption, boolean primary, int order) {
        TreeImage img = new TreeImage();
        img.setTree(tree);
        img.setUrl(url);
        img.setCaption(caption);
        img.setPrimary(primary);
        img.setDisplayOrder(order);
        return img;
    }
}
