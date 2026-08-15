package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Phragmites mauritianus (Imiseke / African reed grass) — TREE-010.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class PhragmitesMauritianusData {

    public static final String SLUG = "phragmites-mauritianus";
    public static final String SCIENTIFIC_NAME = "Phragmites mauritianus";
    public static final String QR_CODE_ID = "TREE-010";
    public static final String FAMILY = "Poaceae (Grass family)";
    public static final String TYPICAL_HEIGHT = "2–4 m (reed stands)";
    public static final String ORIGIN = "Tropical and subtropical Africa — wetlands, lakes and rivers; native in Rwanda including Lake Ruhondo region";
    public static final String AGE_ESTIMATE = "Approx. 2–10 years (perennial reed clumps)";
    public static final double LATITUDE = -1.9676;
    public static final double LONGITUDE = 30.1088;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-010";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-010";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Phragmites_mauritianus";

    private PhragmitesMauritianusData() {}

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
        tree.getCategories().addAll(List.of("CULTURAL", "FIBRE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(10);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/a/a7/Phragmites_mauritianus_107747958.jpg",
                        "Phragmites mauritianus — African reed grass (Imiseke)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/2/2b/Phragmites_mauritianus_15321503.jpg",
                        "Reed grass stand along wetland margin",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/9/9b/Phragmites_mauritianus_15285723.jpg",
                        "Tall reeds of Phragmites mauritianus",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/3/3d/Phragmites_mauritianus_104541499.jpg",
                        "Imiseke / reed grass foliage",
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
        TreeTranslation t = base(tree, "en", "Reed grass");
        t.setShortDescription(
                "Phragmites mauritianus — African reed grass, known in Kinyarwanda as Imiseke. A native wetland " +
                "grass used for house ceilings, fencing, musical instruments and stabilising river and lake banks."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Imiseke.",
                "Also called African reed — a tall perennial grass of wetlands and lake margins.",
                "Stems and leaves used for ceiling (plafond) and fence construction.",
                "Around Lake Ruhondo, communities make a musical instrument called Urusengo from the leaves.",
                "Planted and managed to protect river and lake banks from erosion.",
                "Important cultural plant in northern Rwanda."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Poaceae (Grass family)",
                "Scientific name: Phragmites mauritianus Kunth",
                "Common name: Reed grass · African reed",
                "Local name (Kinyarwanda): Imiseke",
                "Typical height: 2–4 m reed stands",
                "Habitat: River banks, lake shores, wetlands",
                "Cultural note: Urusengo musical instrument (Lake Ruhondo area)",
                "Status in Rwanda: Native wetland plant"
        ));
        t.setDescription(
                "Phragmites mauritianus is the African reed grass — a tall perennial grass native to wetlands, " +
                "rivers and lakes across tropical Africa. In Rwanda it is known as Imiseke and is widely used in " +
                "everyday construction and cultural life.\n\n" +
                "Reed stems are woven into ceilings and fences; leaves are used for craft and, in the north around " +
                "Lake Ruhondo, to make the traditional musical instrument Urusengo. Dense reed stands also stabilise " +
                "river and lake banks against erosion.\n\n" +
                "At Kigali Eco-Park this species is presented as Imiseke / reed grass, matching local naming and " +
                "documented uses."
        );
        t.setUses(
                "Bank stabilisation: Protects and stabilises river banks and lake shores.\n\n" +
                "Construction: Used to make ceilings (plafond) of houses and reed fences (urugo).\n\n" +
                "Cultural: In northern Rwanda around Lake Ruhondo, leaves are used to make the musical instrument Urusengo.\n\n" +
                "Craft & household: Flexible stems and leaves for weaving and building materials."
        );
        t.setEcologicalImportance(
                "Reed beds filter water, reduce bank erosion and provide habitat for birds, fish fry and wetland " +
                "insects. As a native wetland plant, Imiseke supports biodiversity along Rwanda's lakes and rivers."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Building material, fences, ceilings, musical instruments and erosion control.\n\n" +
                "For wildlife: Wetland habitat structure, shelter and nesting sites for waterbirds."
        );
        t.setCommonAreas(
                "Native along lakes (including Lake Ruhondo), rivers and wetlands in Rwanda and tropical Africa. " +
                "Prefers moist soils and shallow water margins."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Imiseke label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: reed grass · African reed · Imiseke · Poaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Imiseke");
        t.setShortDescription(
                "Imiseke (Phragmites mauritianus / Reed grass) — icyatsi cy'igihugu cy'imigezi n'ibiyaga " +
                "gikoreshwa mu kubaka, imyuziki n'kurinda inkombe."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Imiseke.",
                "Ni icyatsi kinini cy'imigezi n'ibiyaga.",
                "Gukoreshwa mu gukora plafond y'inzu n'uruzitiro.",
                "Ku kiyaga cya Ruhondo bakoramo Urusengo — igikinisho cy'umuziki.",
                "Irinda inkombe z'imigezi n'ibiyaga."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Poaceae",
                "Izina ry'ubumenyi: Phragmites mauritianus",
                "Izina ry'ikinyarwanda: Imiseke",
                "Izina ry'icyongereza: Reed grass",
                "Uburebure: metero 2–4 (imiseke)",
                "Aho ukura: Imigezi, ibiyaga, n'ibishanga",
                "Imyuziki: Urusengo (Ruhondo)"
        ));
        t.setDescription(
                "Phragmites mauritianus ni Imiseke — icyatsi cy'igihugu cy'umuryango wa Poaceae. Mu Rwanda " +
                "gikoreshwa cyane mu bucuruzi n'imico.\n\n" +
                "Imiseke ikoreshwa mu gukora plafond y'inzu, urugo rw'imiseke, no kurinda inkombe z'imigezi " +
                "n'ibiyaga. Abaturage bo ku nkengero z'ikiyaga cya Ruhondo bakoramo Urusengo — igikinisho cy'umuziki.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Imiseke / reed grass hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Gukora plafond y'imiseke — gushyirwa hejuru y'inzu.\n" +
                "• Gukora urugo rw'imiseke — uruzitiro.\n" +
                "• Abaturage bo ku nkengero z'ikiyaga cya Ruhondo bakoramo Urusengo (igikinisho cy'umuziki).\n" +
                "• Imiseke irinda inkombe z'imigezi cyangwa ibiyaga."
        );
        t.setEcologicalImportance(
                "Imiseke ifasha kurinda inkombe z'imigezi n'ibiyaga, itanga ahantu ho kwihisha inyoni " +
                "n'ibinyabuzima by'ibishanga."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Kubaka, uruzitiro, imyuziki (Urusengo), no kurinda inkombe.\n\n" +
                "Ku nyamaswa: Ahantu ho kwihisha inyoni n'ibinyabuzima by'amazi."
        );
        t.setCommonAreas(
                "Uboneka ku nkengero z'ibiyaga (nka Ruhondo), imigezi n'ibishanga mu Rwanda n'Afurika y'ubushyuhe."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Imiseke kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Imiseke · Phragmites mauritianus · Reed grass · Poaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Roseau africain");
        t.setShortDescription(
                "Phragmites mauritianus — roseau africain, appelé Imiseke en kinyarwanda. Grande graminée " +
                "des zones humides utilisée pour plafonds, clôtures, instruments de musique et stabilisation des berges."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Imiseke.",
                "Roseau des lacs et rivières d'Afrique tropicale.",
                "Plafonds et clôtures en roseau.",
                "Autour du lac Ruhondo : instrument Urusengo fabriqué avec les feuilles.",
                "Protection des berges contre l'érosion."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Poaceae",
                "Nom scientifique : Phragmites mauritianus",
                "Nom commun : Roseau africain",
                "Nom local : Imiseke",
                "Hauteur : 2–4 m",
                "Habitat : Berges, lacs, zones humides"
        ));
        t.setDescription(
                "Phragmites mauritianus est le roseau africain — grande graminée indigène des zones humides. " +
                "Au Rwanda, on l'appelle Imiseke.\n\n" +
                "Usages : plafonds, clôtures, instrument Urusengo (lac Ruhondo), stabilisation des berges.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Imiseke selon les usages locaux."
        );
        t.setUses(
                "Stabilisation des berges de rivières et lacs.\n\n" +
                "Construction : plafonds et clôtures en roseau.\n\n" +
                "Culturel : Urusengo — instrument de musique (région du lac Ruhondo)."
        );
        t.setEcologicalImportance(
                "Filtre les eaux, réduit l'érosion et abrite oiseaux et faune des zones humides."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Matériau de construction, musique, protection des berges.\n\n" +
                "Pour la faune : Habitat des zones humides."
        );
        t.setCommonAreas(
                "Indigène au Rwanda le long des lacs, rivières et marais — notamment région de Ruhondo."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Imiseke pour photos, carte et médias.\n\n" +
                "Imiseke · Phragmites mauritianus · Roseau africain · Poaceae."
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
