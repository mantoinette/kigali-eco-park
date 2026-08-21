package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Croton megalocarpus (Umunege / croton tree) — TREE-021.
 * Content aligned with park ethnobotanical notes for Rwanda highland forests.
 */
public final class CrotonMegalocarpusData {

    public static final String SLUG = "croton-megalocarpus";
    public static final String SCIENTIFIC_NAME = "Croton megalocarpus";
    public static final String QR_CODE_ID = "TREE-021";
    public static final String FAMILY = "Euphorbiaceae (Spurge family)";
    public static final String TYPICAL_HEIGHT = "Medium to large forest tree";
    public static final String ORIGIN = "East & Central African highlands";
    public static final String AGE_ESTIMATE = "Approx. 20–50 years (park specimen)";
    public static final double LATITUDE = -1.9682;
    public static final double LONGITUDE = 30.1112;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-021";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-021";
    public static final String REFERENCE_URL =
            "https://powo.science.kew.org/taxon/urn:lsid:ipni.org:names:342991-1";

    private CrotonMegalocarpusData() {}

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
        tree.getCategories().addAll(List.of("SHADE", "MEDICINAL", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(21);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/4/4e/Croton_megalocarpus1.jpg",
                        "Croton megalocarpus — flowering tree (Umunege)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/2/20/Croton_megalocarpus2.jpg",
                        "Croton megalocarpus flowers and leaves",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/c/c3/Croton_megalocarpus3.jpg",
                        "Croton megalocarpus foliage detail",
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
        TreeTranslation t = base(tree, "en", "Croton tree");
        t.setShortDescription(
                "Croton megalocarpus — croton tree, known in Kinyarwanda as Umunege. A highland forest tree of " +
                "Rwanda that provides shade, wind protection and soil conservation; nuts yield oil used as biofuel."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umunege.",
                "Also called croton tree or croto tree.",
                "Grows in highland forests such as Nyungwe and Gishwati.",
                "Provides shade for people and wildlife.",
                "Protects against wind and holds soil.",
                "Nuts produce oil used in engines as biofuel or biodiesel.",
                "The oil burns with lower air pollution than many fossil fuels.",
                "Family: Euphorbiaceae (spurge family)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Euphorbiaceae (Spurge family)",
                "Scientific name: Croton megalocarpus Hutch.",
                "Common name: Croton tree",
                "Local name (Kinyarwanda): Umunege",
                "Typical height: medium to large forest tree",
                "Distribution: East and Central African highlands, including Rwanda",
                "Park ID: TREE-021"
        ));
        t.setDescription(
                "Croton megalocarpus is a medium to large tree of the family Euphorbiaceae. In Rwanda it is known " +
                "as Umunege. It grows in highland forests of the country, including places such as Nyungwe and " +
                "Gishwati.\n\n" +
                "The tree is valued for shade, windbreaks and soil conservation. Its nuts yield oil that can be " +
                "processed for use in engines as biofuel or biodiesel, with comparatively clean combustion.\n\n" +
                "At Kigali Eco-Park this TREE-021 specimen is presented as Umunege / croton tree, matching local " +
                "naming and documented uses."
        );
        t.setUses(
                "Shade and shelter: Provides shade for people and animals; used as a windbreak.\n\n" +
                "Soil conservation: Roots and canopy help hold soil and reduce erosion.\n\n" +
                "Biofuel: Nuts are pressed for oil used in motors as biofuel or biodiesel."
        );
        t.setEcologicalImportance(
                "A characteristic tree of moist highland forests in East and Central Africa. Canopy shade and " +
                "wind protection support understorey plants, livestock and wildlife; soil-binding roots reduce erosion."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Shade, wind protection, soil conservation and nut oil for biofuel.\n\n" +
                "For wildlife: Shade and habitat in highland forest edges and park plantings."
        );
        t.setCommonAreas(
                "Highland forests of Rwanda (including Nyungwe and Gishwati) and neighbouring East and Central " +
                "African countries."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umunege (TREE-021) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: croton tree · croto tree · Umunege · Euphorbiaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umunege");
        t.setShortDescription(
                "Umunege (Croton megalocarpus) — igiti kiba mu mashyamba y'u Rwanda yo mu misozi miremire nka " +
                "Nyungwe na Gishwati. Kitanga urugara, kirinda umuyaga, gifata ubutaka; imbuto zivamo amavuta yo " +
                "muri moteri."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umunege.",
                "Kiba mu mashyamba yo mu misozi miremire nka Nyungwe na Gishwati.",
                "Kigira urugara abantu n'inyamaswa byagamamo izuba.",
                "Kirinda umuyaga kandi gifata ubutaka neza.",
                "Imbuto zivamo amavuta akoreshwa muri za moteri.",
                "Ayo mavuta ntahumanya ikirere nka amavuta ya petroli.",
                "Umuryango: Euphorbiaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Euphorbiaceae",
                "Izina ry'ubumenyi: Croton megalocarpus",
                "Izina ry'ikinyarwanda: Umunege",
                "Izina ry'icyongereza: Croton tree",
                "Aho kiboneka: Mashyamba yo mu misozi miremire y'u Rwanda n'Afurika y'Iburasirazuba",
                "Ikimenyetso: TREE-021"
        ));
        t.setDescription(
                "Croton megalocarpus ni Umunege — igiti cy'umuryango wa Euphorbiaceae. Ni igiti kiba mu mashyamba " +
                "y'u Rwanda yo mu misozi miremire nka Nyungwe, Gishwati n'ahandi hameze nk'aho.\n\n" +
                "Akamaro: kigira urugara abantu n'inyamaswa byagamamo izuba. Kirinda umuyaga, gifata ubutaka neza. " +
                "Imbuto zivamo amavuta akoreshwa muri za moteri; ayo mavuta ntahumanya ikirere.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umunege / croton tree (TREE-021) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Urugara ku bantu n'inyamaswa.\n" +
                "• Kurinda umuyaga no gufata ubutaka.\n\n" +
                "Amavuta:\n" +
                "• Imbuto zivamo amavuta yo gukoresha muri za moteri (biofuel).\n" +
                "• Ntahumanya ikirere."
        );
        t.setEcologicalImportance(
                "Umunege ukura mu mashyamba yo mu misozi miremire; utanga urugara, urinda umuyaga kandi ufasha " +
                "gufata ubutaka kugira ngo butarenze."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Urugara, kurinda umuyaga, gufata ubutaka n'amavuta yo muri moteri.\n\n" +
                "Ku nyamaswa: Urugara n'ubuturo mu nkengero z'ishyamba."
        );
        t.setCommonAreas(
                "Mu mashyamba yo mu misozi miremire y'u Rwanda (Nyungwe, Gishwati n'ahandi) no mu bihugu baturanye."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umunege (TREE-021) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umunege · Croton megalocarpus · Croton tree · Euphorbiaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Croton à gros fruits");
        t.setShortDescription(
                "Croton megalocarpus — appelé Umunege en kinyarwanda. Arbre des forêts de montagne du Rwanda ; " +
                "ombre, brise-vent, conservation des sols ; huile des noix utilisée comme biocarburant."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umunege.",
                "Forêts de montagne (Nyungwe, Gishwati).",
                "Ombre, protection contre le vent et fixation des sols.",
                "Huile des noix pour moteurs (biocarburant).",
                "Famille : Euphorbiaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Euphorbiaceae",
                "Nom scientifique : Croton megalocarpus",
                "Nom commun : Croton tree",
                "Nom local : Umunege",
                "Identifiant parc : TREE-021"
        ));
        t.setDescription(
                "Croton megalocarpus est un arbre des forêts de montagne. Au Rwanda : Umunege.\n\n" +
                "Usages : ombre, brise-vent, conservation des sols ; huile des noix comme biocarburant.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umunege selon les usages locaux."
        );
        t.setUses(
                "Ombre et abri : Ombre pour les personnes et les animaux ; brise-vent.\n\n" +
                "Conservation des sols : Racines et canopée limitent l'érosion.\n\n" +
                "Biocarburant : Huile des noix pour moteurs."
        );
        t.setEcologicalImportance(
                "Arbre des forêts humides d'altitude en Afrique orientale et centrale ; ombre et fixation des sols."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Ombre, brise-vent, sols et biocarburant.\n\n" +
                "Pour la faune : Ombre et habitat."
        );
        t.setCommonAreas(
                "Forêts de montagne du Rwanda (Nyungwe, Gishwati) et pays voisins."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umunege pour photos, carte et médias.\n\n" +
                "Umunege · Croton megalocarpus · Croton tree · Euphorbiaceae."
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
