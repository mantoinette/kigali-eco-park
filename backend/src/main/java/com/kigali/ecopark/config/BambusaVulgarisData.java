package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Bambusa vulgaris (Umugano / Common bamboo) — TREE-005.
 * Content aligned with park ethnobotanical notes and species profile for common bamboo.
 */
public final class BambusaVulgarisData {

    public static final String SLUG = "bambusa-vulgaris";
    public static final String SCIENTIFIC_NAME = "Bambusa vulgaris";
    public static final String QR_CODE_ID = "TREE-005";
    public static final String FAMILY = "Poaceae (Grass family)";
    public static final String TYPICAL_HEIGHT = "10–20 m (culms)";
    public static final String ORIGIN = "Tropical Asia; widely cultivated and naturalised in tropical Africa including Rwanda";
    public static final String AGE_ESTIMATE = "Approx. 5–25 years (clump-forming park specimen)";
    public static final double LATITUDE = -1.9698;
    public static final double LONGITUDE = 30.1068;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-005";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-005";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Bambusa_vulgaris";

    private BambusaVulgarisData() {}

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
        tree.setNativeStatus("INTRODUCED");
        tree.getCategories().clear();
        tree.getCategories().addAll(List.of("TIMBER", "FIBRE", "SHADE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(5);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Golden_Bamboo%28Bambusa_vulgaris%29_in_Hong_Kong.jpg/1280px-Golden_Bamboo%28Bambusa_vulgaris%29_in_Hong_Kong.jpg",
                        "Bambusa vulgaris — common bamboo culms and foliage",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Bambusa_vulgaris_%28Dominica%29.jpg/1280px-Bambusa_vulgaris_%28Dominica%29.jpg",
                        "Common bamboo stand (Bambusa vulgaris)",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Bambusa_vulgaris_-_Bamboo_Tree.jpg/1280px-Bambusa_vulgaris_-_Bamboo_Tree.jpg",
                        "Tall bamboo culms of Bambusa vulgaris",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Bambusa_vulgaris_44.jpg/1280px-Bambusa_vulgaris_44.jpg",
                        "Close view of Bambusa vulgaris stems and leaves",
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
        TreeTranslation t = base(tree, "en", "Common bamboo");
        t.setShortDescription(
                "Bambusa vulgaris — common bamboo, known in Kinyarwanda as Umugano. " +
                "A tall clumping bamboo of the grass family, long used in Rwanda for construction, " +
                "fencing, crafts and erosion control along riverbanks."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umugano (traditional / gakondo).",
                "A giant woody grass — not a true tree, but managed as a multipurpose woody plant.",
                "Culms grow in dense clumps and may reach 10–20 m tall.",
                "Leaves are used as livestock fodder; stems for building and basketry.",
                "Planted to control soil erosion (isuri) on riverbanks and slopes.",
                "Widely cultivated across tropical Africa and Asia."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Poaceae (Grass family)",
                "Scientific name: Bambusa vulgaris Schrad. ex J.C.Wendl.",
                "Common name: Common bamboo",
                "Local name (Kinyarwanda): Umugano",
                "Typical height: 10–20 m (culms)",
                "Growth form: Clumping bamboo with hollow woody stems",
                "Habitat: Moist soils, farms, river margins and park plantings",
                "Status in Rwanda: Long-established traditional (gakondo) cultivated bamboo"
        ));
        t.setDescription(
                "Bambusa vulgaris is the common bamboo — a fast-growing, clump-forming woody grass " +
                "in the family Poaceae. In Rwanda it is known as Umugano and is treated as a traditional " +
                "(gakondo) useful plant in farms, hedges and riverside plantings.\n\n" +
                "The plant produces tall hollow culms with nodes, and dense evergreen foliage. Although " +
                "botanically a grass rather than a tree, it provides timber-like material, shade and " +
                "craft fibre that park visitors recognise as one of the most useful woody plants in " +
                "everyday Rwandan life.\n\n" +
                "At Kigali Eco-Park this species is presented as Umugano / common bamboo, matching " +
                "local naming and multipurpose ethnobotanical use."
        );
        t.setUses(
                "Construction & fencing: Culms used for building, poles, fencing (uruzitiro) and " +
                "lightweight structures.\n\n" +
                "Crafts & household: Split stems for basketry, furniture, musical instruments " +
                "(inkooko, inkangara, intaro and other craft items), and various household tools.\n\n" +
                "Boats, furniture & paper: Traditionally used in boat-making, furniture and paper production.\n\n" +
                "Fodder: Leaves fed to livestock.\n\n" +
                "Ornamental & landscape: Planted as an ornamental and living screen.\n\n" +
                "Erosion control: Used to fight soil erosion (isuri) along riverbanks and slopes."
        );
        t.setEcologicalImportance(
                "Bamboo clumps protect soil on slopes and river margins, reduce runoff and provide " +
                "cover for small wildlife. Dense stands also create microclimates of shade and humidity " +
                "useful in farm and park landscapes.\n\n" +
                "As a fast-growing renewable woody grass, it offers an alternative material to timber trees."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Building material, fences, crafts, fodder, ornamental plantings and " +
                "erosion control — a core traditional resource (gakondo) in Rwanda.\n\n" +
                "For wildlife: Shelter and habitat structure within dense culm clumps."
        );
        t.setCommonAreas(
                "Cultivated across tropical Africa and Asia; in Rwanda common around homes, farms, " +
                "hedges and riverbanks. Prefers moist, fertile soils and good sunlight."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umugano label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: common bamboo, Bambusa vulgaris · Umugano · Poaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umugano");
        t.setShortDescription(
                "Umugano (Bambusa vulgaris / Common bamboo) — umugano gakondo ukoreshwa mu Rwanda " +
                "mu kubaka, uruzitiro, ubukorikori n'kurwanya isuri."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umugano (gakondo).",
                "Ni icyatsi kinini gifite imirhi myiza — ntabwo ari igiti cy'ubumenyi, ariko gifite agaciro kinini.",
                "Umulimbo ushobora kugera metero 10–20.",
                "Amababi agaburirwa amatungo; umulimbo ukoreshwa mu kubaka n'ubukorikori.",
                "Uterwa kugira ngo urwanire isuri ku nkengero z'imigezi.",
                "Uyu mugano ni gakondo mu Rwanda."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Poaceae",
                "Izina ry'ubumenyi: Bambusa vulgaris",
                "Izina ry'ikinyarwanda: Umugano",
                "Izina ry'icyongereza: Common bamboo",
                "Uburebure: metero 10–20 (umulimbo)",
                "Imiterere: Umugano wiyongera mu matsinda",
                "Aho ukura: Imirima, imigezi, n'ahantu h'ubusitani"
        ));
        t.setDescription(
                "Bambusa vulgaris ni umugano usanzwe — icyatsi kinini cy'umuryango wa Poaceae. " +
                "Mu Rwanda twagira tukacyita Umugano, kandi ni gakondo mu mirima n'aho abantu baba.\n\n" +
                "Utera umulimbo muremure ufite ubusa bw'imbere n'amababi menshi. Nubwo mu bumenyi " +
                "atari igiti, ufite agaciro ko gukoreshwa nk'ibiti mu kubaka, uruzitiro n'ubukorikori.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umugano / common bamboo hakurikijwe amazina " +
                "n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Umulimbo, kubaka, gukora uruzitiro, kuboha ibikoresho byo mu rugo.\n" +
                "• Amababi bayagaburira amatungo.\n" +
                "• Amababi barayavuza. Hakorwamo ibikoresho binyuranye by'ubukorikori " +
                "(inkooko, inkangara, intaro, n'ibindi).\n" +
                "• Baranawubakisha kandi ni Nyaburanga.\n" +
                "• Bawukoresha kandi barwanya isuri ku nkengero z'imigezi.\n\n" +
                "Uyu mugano ni gakondo mu Rwanda."
        );
        t.setEcologicalImportance(
                "Umugano ufasha kurinda ubutaka ku nkengero z'imigezi no ku misozi — urwanira isuri. " +
                "Amatsinda y'umulimbo atanga igicucu n'ahantu ho kwihisha inyamaswa nto."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Kubaka, uruzitiro, ubukorikori, ibiribwa by'amatungo, n'kurwanya isuri — " +
                "ni umutungo gakondo.\n\n" +
                "Ku nyamaswa: Ahantu ho kwihisha mu matsinda y'umulimbo."
        );
        t.setCommonAreas(
                "Uboneka mu mirima, ku nzu, mu migezi n'ubusitani mu Rwanda n'Afurika y'ubushyuhe. " +
                "Ukunda ubutaka bw'amazi n'izuba."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umugano kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umugano · Bambusa vulgaris · Common bamboo · Poaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Bambou commun");
        t.setShortDescription(
                "Bambusa vulgaris — bambou commun, appelé Umugano en kinyarwanda. Bambou cespiteux " +
                "utilisé au Rwanda pour la construction, les clôtures, l'artisanat et la lutte contre l'érosion."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umugano (traditionnel / gakondo).",
                "Grande herbe ligneuse — pas un arbre au sens botanique, mais une ressource multipurpose.",
                "Chaumes souvent de 10–20 m de haut.",
                "Feuilles utilisées comme fourrage ; tiges pour construction et vannerie.",
                "Planté pour limiter l'érosion des berges.",
                "Largement cultivé en Afrique et en Asie tropicales."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Poaceae",
                "Nom scientifique : Bambusa vulgaris",
                "Nom commun : Bambou commun",
                "Nom local : Umugano",
                "Hauteur : 10–20 m (chaumes)",
                "Port : Bambou cespiteux à tiges creuses",
                "Habitat : Sols humides, fermes, berges et parcs"
        ));
        t.setDescription(
                "Bambusa vulgaris est le bambou commun, une herbe ligneuse à croissance rapide de la " +
                "famille des Poacées. Au Rwanda, on l'appelle Umugano ; c'est une plante traditionnelle " +
                "(gakondo) des fermes, haies et berges.\n\n" +
                "Il forme des touffes denses de chaumes creux et un feuillage persistant. Au Kigali " +
                "Eco-Park, cette fiche présente Umugano / bambou commun selon les usages locaux."
        );
        t.setUses(
                "Construction et clôtures : Chaumes pour bâtir, poteaux et clôtures.\n\n" +
                "Artisanat : Tiges fendues pour vannerie, meubles, instruments de musique et outils.\n\n" +
                "Bateaux, meubles et papier : Usages traditionnels variés.\n\n" +
                "Fourrage : Feuilles pour le bétail.\n\n" +
                "Ornement et érosion : Écran vivant ; lutte contre l'érosion des berges."
        );
        t.setEcologicalImportance(
                "Les touffes stabilisent les sols et les berges, réduisent le ruissellement et offrent " +
                "un abri à la petite faune. Ressource renouvelable rapide face au bois d'œuvre."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Matériau de construction, clôtures, artisanat, fourrage et " +
                "contrôle de l'érosion.\n\n" +
                "Pour la faune : Abri dans les touffes denses."
        );
        t.setCommonAreas(
                "Cultivé en Afrique et Asie tropicales ; au Rwanda près des maisons, fermes et rivières. " +
                "Préfère sols humides et bonne exposition."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umugano pour photos, carte et médias.\n\n" +
                "Umugano · Bambusa vulgaris · Bambou commun · Poaceae."
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
