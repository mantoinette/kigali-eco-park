package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Erythrina abyssinica (Umuko / Umurinzi / Red hot poker tree) — TREE-006.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class ErythrinaAbyssinicaData {

    public static final String SLUG = "erythrina-abyssinica";
    public static final String SCIENTIFIC_NAME = "Erythrina abyssinica";
    public static final String QR_CODE_ID = "TREE-006";
    public static final String FAMILY = "Fabaceae (Legume / pea family)";
    public static final String TYPICAL_HEIGHT = "6–12 m (up to 15 m)";
    public static final String ORIGIN = "East and Central Africa — Ethiopia to South Africa; native in Rwanda and the Great Lakes region";
    public static final String AGE_ESTIMATE = "Approx. 8–25 years (park specimen)";
    public static final double LATITUDE = -1.9692;
    public static final double LONGITUDE = 30.1072;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-006";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-006";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Erythrina_abyssinica";

    private ErythrinaAbyssinicaData() {}

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
        tree.getCategories().addAll(List.of("MEDICINAL", "CULTURAL", "SHADE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(6);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/b/b8/MCBG_Erythrina_abyssinica.JPG",
                        "Erythrina abyssinica — red hot poker tree with spreading crown",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/b/bc/Erythrina_abyssinica_Tree.jpg",
                        "Umuko / coral tree in natural habitat",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/7/7f/Erythrina_abyssinica_1DS-II_4038.jpg",
                        "Scarlet flower spikes of Erythrina abyssinica",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/c/c2/Starr_080716-9302_Erythrina_abyssinica.jpg",
                        "Trifoliate leaves and branching habit",
                        false,
                        4
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
        TreeTranslation t = base(tree, "en", "Red hot poker tree");
        t.setShortDescription(
                "Erythrina abyssinica — red hot poker tree, known in Kinyarwanda as Umuko or Umurinzi. " +
                "A native coral tree of the legume family, valued for cultural ceremonies, traditional " +
                "medicine and fishing floats made from the corky bark."
        );
        t.setInterestingFacts(String.join("\n",
                "Local names (Kinyarwanda): Umuko; also Umurinzi.",
                "Also called Abyssinian coral tree — bright red flower spikes in the dry season.",
                "Used in Ryangombe religious and socio-cultural ceremonies.",
                "Stem bark used in traditional medicine for liver complaints.",
                "Corky bark (suber) used as floats in fishing.",
                "Also planted for shade and soil improvement (green manure)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae (Legume / pea family)",
                "Scientific name: Erythrina abyssinica Lam. ex DC.",
                "Common name: Red hot poker tree",
                "Local names (Kinyarwanda): Umuko · Umurinzi",
                "Typical height: 6–12 m",
                "Flowers: Scarlet red spikes — pollinated by sunbirds",
                "Habitat: Woodland, forest margins and park plantings",
                "Status in Rwanda: Native indigenous tree"
        ));
        t.setDescription(
                "Erythrina abyssinica is the red hot poker tree — a medium-sized deciduous legume " +
                "native to East and Central Africa. In Rwanda it is known as Umuko or Umurinzi and " +
                "is recognised for both cultural importance and practical everyday uses.\n\n" +
                "The tree produces distinctive scarlet flower spikes, trifoliate leaves and a spreading " +
                "crown that provides shade. Its bark is corky and lightweight, which makes it useful " +
                "as a fishing float. Stem-bark preparations are used in traditional medicine, " +
                "especially for liver complaints and related ailments recorded in local ethnobotany.\n\n" +
                "At Kigali Eco-Park this species is presented as Umuko / red hot poker tree, matching " +
                "local naming and documented uses."
        );
        t.setUses(
                "Socio-cultural: Used in religious ceremonies of Ryangombe and related traditional gatherings.\n\n" +
                "Traditional medicine: Stem bark used to treat liver complaints; also recorded for " +
                "intestinal worms, yaws, sores and malaria in local practice.\n\n" +
                "Fishing: Corky bark (suber) used as floats in fishing.\n\n" +
                "Agroforestry: Planted to enrich soil (green manure) and provide shade."
        );
        t.setEcologicalImportance(
                "Bright red flowers attract sunbirds and other nectar-feeding birds, supporting pollination " +
                "networks in woodland margins. Leaf fall contributes organic matter to soil. " +
                "As a native legume, it participates in nitrogen cycling where it grows with associated rhizobia."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Cultural ceremonies, traditional medicine, fishing floats, shade and soil improvement.\n\n" +
                "For wildlife: Nectar for sunbirds and insects; perches and shelter in the spreading crown."
        );
        t.setCommonAreas(
                "Native across East and Central Africa including Rwanda, Uganda, Kenya and Tanzania. " +
                "Found in woodland, forest edges, farms and park plantings; prefers well-drained soils."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umuko label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: red hot poker tree · Umuko · Umurinzi · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umuko");
        t.setShortDescription(
                "Umuko (Erythrina abyssinica / Red hot poker tree) — igiti cy'igihugu cy'Umuko cyangwa " +
                "Umurinzi, gifite akamaro mu mihango ya Ryangombe, ubuvuzi gakondo n'uburobyi."
        );
        t.setInterestingFacts(String.join("\n",
                "Amazina y'ikinyarwanda: Umuko; Umurinzi.",
                "Ifite indabyo z'umutuku — zituma bacyita red hot poker tree.",
                "Ikoreshwa mu mihango ya Ryangombe.",
                "Igishihwa cy'igiti gikoreshwa mu kuvura indwara z'umwijima.",
                "Ubusa bw'igishihwa bukoreshwa mu burobyi (indeberezi).",
                "Haterwa kugira ngo gufumbire ubutaka."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae",
                "Izina ry'ubumenyi: Erythrina abyssinica",
                "Izina ry'ikinyarwanda: Umuko · Umurinzi",
                "Izina ry'icyongereza: Red hot poker tree",
                "Uburebure: metero 6–12",
                "Indabyo: Umutuku — zishimishwa inyoni",
                "Aho ukura: Igihugu, imbibi z'amasaka n'ubusitani"
        ));
        t.setDescription(
                "Erythrina abyssinica ni Umuko — igiti cy'igihugu cy'umuryango wa Fabaceae. " +
                "Mu Rwanda twagira tukacyita Umuko cyangwa Umurinzi.\n\n" +
                "Ifite indabyo z'umutuku, amababi atatu ku giti, kandi gitanga igicucu. " +
                "Igishihwa cyacyo gikoreshwa mu buvuzi gakondo, cyane cyane indwara z'umwijima, " +
                "kandi ubusa bwacyo bukoreshwa mu burobyi.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umuko / red hot poker tree hakurikijwe " +
                "amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Kubandwa Ryangombe — mu mihango ya religiyo n'imico.\n" +
                "• Gufumbira ubutaka — gukoresha nk'ifumbire y'ubutaka.\n\n" +
                "Kuvura:\n" +
                "• Indwara z'umwijima — igishihwa cy'igiti gikoreshwa mu kuvura indwara z'umwijima.\n" +
                "• Inzoka zo munda, ibinyoro, ibisebe, umwijima na malariya — bikoreshwa mu buvuzi gakondo.\n" +
                "• Indeberezi mu burobyi — ubusa bw'igishihwa bukoreshwa nk'indeberezi mu burobyi."
        );
        t.setEcologicalImportance(
                "Indabyo z'umutuku zishimisha inyoni n'udukoko dutera ubwonko. Amababi agwa " +
                "afasha gufumbira ubutaka. Nk'igiti cy'igihugu cy'umuryango wa Fabaceae, gifasha " +
                "mu guhindura azote mu butaka."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Mihango, ubuvuzi, uburobyi, igicucu n'ifumbire y'ubutaka.\n\n" +
                "Ku nyamaswa: Ubwonko bw'inyoni n'ahantu ho kwihisha mu giti."
        );
        t.setCommonAreas(
                "Uboneka mu Rwanda n'Afurika y'Iburengerazuba n'Uburengerazuba — mu gihugu, " +
                "ku mpera z'amasaka, mu mirima n'ubusitani."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umuko kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umuko · Umurinzi · Erythrina abyssinica · Red hot poker tree · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Érythrine d'Abyssinie");
        t.setShortDescription(
                "Erythrina abyssinica — arbre flamme / érythrine d'Abyssinie, appelé Umuko ou Umurinzi " +
                "en kinyarwanda. Arbre indigène aux fleurs écarlates, utilisé dans les cérémonies " +
                "traditionnelles, la médecine et la pêche."
        );
        t.setInterestingFacts(String.join("\n",
                "Noms locaux : Umuko ; Umurinzi.",
                "Fleurs rouge vif en épis — arbre de corail d'Abyssinie.",
                "Utilisé dans les cérémonies religieuses de Ryangombe.",
                "Écorce de tige pour les affections du foie.",
                "Liège de l'écorce utilisé comme flotteur de pêche.",
                "Planté pour l'ombrage et l'enrichissement des sols."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae",
                "Nom scientifique : Erythrina abyssinica",
                "Nom commun : Érythrine d'Abyssinie / arbre flamme",
                "Noms locaux : Umuko · Umurinzi",
                "Hauteur : 6–12 m",
                "Fleurs : Épis écarlates",
                "Habitat : Lisières forestières et parcs"
        ));
        t.setDescription(
                "Erythrina abyssinica est l'érythrine d'Abyssinie — un légumineux indigène d'Afrique " +
                "de l'Est et centrale. Au Rwanda, on l'appelle Umuko ou Umurinzi.\n\n" +
                "Il porte des fleurs écarlate en épis, un feuillage trifolié et une couronne ombragée. " +
                "Son écorce liègeuse sert de flotteur ; l'écorce entre dans la pharmacopée traditionnelle, " +
                "notamment pour le foie.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umuko / érythrine d'Abyssinie selon les usages locaux."
        );
        t.setUses(
                "Socio-culturel : Cérémonies religieuses de Ryangombe.\n\n" +
                "Médecine traditionnelle : Écorce pour affections du foie ; usages locaux pour vers intestinaux, " +
                "framboesia, plaies et paludisme.\n\n" +
                "Pêche : Liège de l'écorce comme flotteur.\n\n" +
                "Agroforesterie : Engrais vert et ombrage."
        );
        t.setEcologicalImportance(
                "Les fleurs attirent les souimangas et pollinisateurs ; la chute des feuilles enrichit le sol. " +
                "Légumineuse native participant au cycle de l'azote."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Cérémonies, médecine, pêche, ombrage et fertilisation.\n\n" +
                "Pour la faune : Nectar pour oiseaux ; abri dans la couronne."
        );
        t.setCommonAreas(
                "Indigène au Rwanda et en Afrique orientale et centrale — lisières, fermes et parcs."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umuko pour photos, carte et médias.\n\n" +
                "Umuko · Umurinzi · Erythrina abyssinica · Fabaceae."
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
