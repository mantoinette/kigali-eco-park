package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Millettia laurentii (Umuyogoro / wenge / faux ebony) — TREE-016.
 * Content aligned with park ethnobotanical notes and IUCN species profile.
 */
public final class MillettiaLaurentiiData {

    public static final String SLUG = "millettia-laurentii";
    public static final String SCIENTIFIC_NAME = "Millettia laurentii";
    public static final String QR_CODE_ID = "TREE-016";
    public static final String FAMILY = "Fabaceae (Legume family)";
    public static final String TYPICAL_HEIGHT = "Large forest tree to about 30 m";
    public static final String ORIGIN = "Central and West Africa — Congo basin forests";
    public static final String AGE_ESTIMATE = "Approx. 30–80 years (park specimen)";
    public static final double LATITUDE = -1.9672;
    public static final double LONGITUDE = 30.1102;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-016";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-016";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Millettia_laurentii";

    private MillettiaLaurentiiData() {}

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
        tree.getCategories().addAll(List.of("TIMBER", "MEDICINAL", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(16);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/e/ee/MillLaur.jpg",
                        "Millettia laurentii — wenge tree (Umuyogoro)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/d/d9/Wenge01.jpg",
                        "Wenge wood — dark timber prized for furniture",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/a/ad/Wengefurn.jpg",
                        "Wenge timber used in fine furniture",
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
        TreeTranslation t = base(tree, "en", "Faux ebony");
        t.setShortDescription(
                "Millettia laurentii — faux ebony or African rosewood, known in Kinyarwanda as Umuyogoro. " +
                "A prized timber tree producing dark wenge wood; IUCN Red List endangered. Bark used in traditional medicine."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umuyogoro.",
                "Also called wenge or African rosewood.",
                "Timber is dark-coloured and highly valued as wenge or faux ebony.",
                "Listed as Endangered on the IUCN Red List.",
                "Bark decoction used for liver complaints; bark has insecticidal properties.",
                "Family: Fabaceae (legume family)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae (Legume family)",
                "Scientific name: Millettia laurentii De Wild.",
                "Common name: Faux ebony · Wenge · African rosewood",
                "Local name (Kinyarwanda): Umuyogoro",
                "Typical height: large forest tree to about 30 m",
                "Conservation: IUCN Red List — Endangered",
                "Park ID: TREE-016"
        ));
        t.setDescription(
                "Millettia laurentii is a large African legume tree whose dark timber is traded as wenge, " +
                "African rosewood or faux ebony. In Rwanda it is known as Umuyogoro.\n\n" +
                "The wood is prized for fine furniture and flooring. The species is Endangered on the IUCN Red List " +
                "because of over-harvesting. In traditional medicine, a bark decoction is used for liver complaints; " +
                "the bark also has insecticidal properties.\n\n" +
                "At Kigali Eco-Park this TREE-016 specimen is presented as Umuyogoro / faux ebony, matching local naming " +
                "and documented uses."
        );
        t.setUses(
                "Timber: Dark-coloured wood marketed as wenge, African rosewood or faux ebony — prized for furniture and joinery.\n\n" +
                "Traditional medicine: Bark decoction to treat liver complaints.\n\n" +
                "Other: Bark has insecticidal properties; also planted as an ornamental shade tree."
        );
        t.setEcologicalImportance(
                "A native forest canopy tree of the Congo basin. Populations are threatened by illegal logging; " +
                "sustainable use and replanting help protect remaining stands."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: High-value timber, traditional medicine, ornamental planting.\n\n" +
                "For wildlife: Forest canopy habitat and nectar for pollinators."
        );
        t.setCommonAreas(
                "Native to moist forests of Central and West Africa, including the Congo basin; planted as ornamental in Rwanda."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umuyogoro (TREE-016) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: wenge · African rosewood · faux ebony · Umuyogoro · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umuyogoro");
        t.setShortDescription(
                "Umuyogoro (Millettia laurentii / faux ebony) — igiti cy'imbaho nziza (Wenge). " +
                "Ni igiti cy'umulimbo; bark ikoreshwa mu buvuzi gakondo."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umuyogoro.",
                "Imbaho nziza — Wenge / faux ebony.",
                "Igiti cy'umulimbo.",
                "Bark ikivura indwara z'umwijima.",
                "Umuryango: Fabaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae",
                "Izina ry'ubumenyi: Millettia laurentii",
                "Izina ry'ikinyarwanda: Umuyogoro",
                "Izina ry'icyongereza: Faux ebony · Wenge",
                "Uburebure: igiti kinini kigeza metero 30",
                "Ikimenyetso: TREE-016"
        ));
        t.setDescription(
                "Millettia laurentii ni Umuyogoro — igiti kinini cy'umuryango wa Fabaceae. Imbaho yacyo ifite " +
                "ibara ryijimye rishobora gukoreshwa nk'Wenge cyangwa faux ebony.\n\n" +
                "Ni igiti cy'umulimbo. Mu buvuzi gakondo, bark ikoreshwa mu kuvura indwara z'umwijima, diabete, " +
                "umuriro, indwara z'uruhu n'ibindi.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umuyogoro / faux ebony (TREE-016) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Imbaho nziza (Wenge).\n" +
                "• Igiti cy'umulimbo.\n\n" +
                "Ubuvuzi gakondo:\n" +
                "• Bark ikivura indwara z'umwijima, diabete, umuriro, indwara z'uruhu n'ibindi."
        );
        t.setEcologicalImportance(
                "Umuyogoro ni igiti cy'ishyamba gikomeye; uko ibiti byo mu ishyamba bikomeje kugabanuka, " +
                "birakeneye kurindwa."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Imbaho nziza, ubuvuzi gakondo, igiti cy'umulimbo.\n\n" +
                "Ku nyamaswa: Ahantu heza mu ishyamba n'amashurwe."
        );
        t.setCommonAreas(
                "Cyavuye mu ishyamba rya Afurika y'Epfo n'Uburengerazuba; gihingwa nk'igiti cy'umulimbo mu Rwanda."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umuyogoro (TREE-016) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umuyogoro · Millettia laurentii · Wenge · faux ebony · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Faux ébène");
        t.setShortDescription(
                "Millettia laurentii — faux ébène ou bois de rose africain, appelé Umuyogoro en kinyarwanda. " +
                "Bois wenge très recherché ; espèce en danger (UICN). Écorce utilisée en médecine traditionnelle."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umuyogoro.",
                "Bois wenge ou faux ébène.",
                "En danger sur la Liste rouge de l'UICN.",
                "Décoction d'écorce pour les troubles du foie.",
                "Famille : Fabaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae",
                "Nom scientifique : Millettia laurentii",
                "Nom commun : Faux ébène · Wenge",
                "Nom local : Umuyogoro",
                "Hauteur : grand arbre forestier jusqu'à 30 m",
                "Identifiant parc : TREE-016"
        ));
        t.setDescription(
                "Millettia laurentii produit le bois wenge, très prisé pour l'ébénisterie. Au Rwanda : Umuyogoro.\n\n" +
                "Usages : bois de valeur, médecine traditionnelle (foie), arbre d'ornement.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umuyogoro selon les usages locaux."
        );
        t.setUses(
                "Bois wenge / faux ébène pour meubles et parquets.\n\n" +
                "Médecine : décoction d'écorce pour les affections du foie.\n\n" +
                "Propriétés insecticides de l'écorce ; arbre d'ornement."
        );
        t.setEcologicalImportance(
                "Arbre de canopée des forêts du bassin du Congo ; menacé par l'exploitation forestière."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Bois précieux, médecine traditionnelle.\n\n" +
                "Pour la faune : Habitat forestier."
        );
        t.setCommonAreas(
                "Forêts d'Afrique centrale et occidentale ; planté comme ornement au Rwanda."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umuyogoro pour photos, carte et médias.\n\n" +
                "Umuyogoro · Millettia laurentii · Wenge · Fabaceae."
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
