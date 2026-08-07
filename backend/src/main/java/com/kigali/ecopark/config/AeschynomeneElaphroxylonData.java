package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Aeschynomene elaphroxylon (Ambatch / Umburu / pith-tree) — TREE-003.
 * Content aligned with Wikipedia species profile:
 * https://en.wikipedia.org/wiki/Aeschynomene_elaphroxylon
 */
public final class AeschynomeneElaphroxylonData {

    public static final String SLUG = "aeschynomene-elaphroxylon";
    public static final String SCIENTIFIC_NAME = "Aeschynomene elaphroxylon";
    public static final String QR_CODE_ID = "TREE-003";
    public static final String FAMILY = "Fabaceae (Legume / pea family)";
    public static final String TYPICAL_HEIGHT = "2–9 m (up to 12 m)";
    public static final String ORIGIN = "Tropical Africa — Senegal to Ethiopia, south to Mozambique, Malawi, Zimbabwe and Angola; also Madagascar";
    public static final String AGE_ESTIMATE = "Approx. 5–20 years (fast-growing wetland specimen)";
    public static final double LATITUDE = -1.9692;
    public static final double LONGITUDE = 30.1055;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-003";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-003";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Aeschynomene_elaphroxylon";

    private AeschynomeneElaphroxylonData() {}

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
        tree.getCategories().addAll(List.of("TIMBER", "WILDLIFE", "SHADE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(3);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/0/08/Aeschynomene_elaphroxylon_GS352.png/1280px-Aeschynomene_elaphroxylon_GS352.png",
                        "Ambatch (Aeschynomene elaphroxylon) — botanical illustration",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://commons.wikimedia.org/wiki/Special:FilePath/Aeschynomene_elaphroxylon_-_Andrebagara,_bord_du_Lac_Alaotra,_Ambatondrazaka_District,_Madagascar_22_Nov_2005_03.jpg",
                        "Ambatch stand along Lake Alaotra shoreline, Madagascar",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://commons.wikimedia.org/wiki/Special:FilePath/Aeschynomene_elaphroxylon_-_Andrebagara,_bord_du_Lac_Alaotra,_Ambatondrazaka_District,_Madagascar_22_Nov_2005_-_flwr_06.jpg",
                        "Yellow-orange ambatch flowers with spiny stems",
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
        TreeTranslation t = base(tree, "en", "Ambatch");
        t.setShortDescription(
                "Ambatch (Aeschynomene elaphroxylon) — also called pith-tree, balsa wood tree, or umburu. " +
                "A semi-aquatic African legume that grows in swamps and lakes with extremely lightweight, " +
                "rot-resistant wood."
        );
        t.setInterestingFacts(String.join("\n",
                "Common names: ambatch, pith-tree, balsa wood tree, umburu; locally also boboffee / bofoffe.",
                "Grows as a freshwater mangrove — often in 1–2 m of standing water.",
                "Wood is lighter than cork, spongy, and remarkably rot-resistant.",
                "Can form floating mats or drifting islands of vegetation.",
                "Hosts nitrogen-fixing Bradyrhizobium bacteria in stem nodules as well as roots.",
                "IUCN status: Least Concern."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae (Legume / pea family)",
                "Scientific name: Aeschynomene elaphroxylon (Guill. & Perr.) Taub.",
                "Common name: Ambatch / pith-tree",
                "Local names: Umburu, boboffee (Ethiopia), bilor / billeur (Senegal)",
                "Typical height: 2–9 m (exceptionally to about 12 m)",
                "Trunk: Swollen, conical, sometimes spiny; up to ~50 cm diameter",
                "Flowers: Large yellow to orange blooms (about 3–4.5 cm)",
                "Habitat: Freshwater swamps, lake shores, river margins (70–1,850 m elevation)",
                "Range: Tropical Africa and Madagascar"
        ));
        t.setDescription(
                "Aeschynomene elaphroxylon is an evergreen, semi-aquatic shrub or small tree of the legume family " +
                "Fabaceae. It is unusual among trees because it commonly grows in water — a freshwater mangrove " +
                "of Tropical African swamps, lakes and rivers.\n\n" +
                "The trunk is swollen and somewhat conical, often with spines, and the wood is spongy and " +
                "extremely lightweight, helping the plant stay afloat. Branches that touch water or mud produce " +
                "dense adventitious roots. Specialised root types (macrorhizae and brachyrhizae) form tangled " +
                "networks in shallow water and waterlogged mud.\n\n" +
                "Leaves are compound with many small leaflets. Flowers are relatively large and showy — yellow " +
                "to orange — and the twisted spiral pods hold dark seeds. At Kigali Eco-Park the species is " +
                "presented as Ambatch / Umburu, matching the Wikipedia profile of this remarkable wetland tree."
        );
        t.setUses(
                "Fishing & boats: Extremely light, rot-resistant wood is traditionally cut into floats for " +
                "fishing nets. Trunks are lashed into rafts, canoes and small punt boats (for example around " +
                "Lake Chad and Lake Ziway).\n\n" +
                "Crafts: Locally used for stools, sun screens, and historically for sandals in parts of Ghana.\n\n" +
                "Ornamental: Larger flowers than many aquatic Aeschynomene species; sometimes planted for display.\n\n" +
                "Science & heritage: Pollen and remains in lake sediments help reconstruct past African lake " +
                "levels and climates. Note: dense stands can hinder fishing access while sheltering wildlife."
        );
        t.setEcologicalImportance(
                "Ambatch rapidly colonises freshwater wetlands and often forms dense monospecific stands along " +
                "shores and in shallow water. Its root mats stabilise fluctuating shorelines and create habitat " +
                "structure for aquatic and shoreline wildlife.\n\n" +
                "As a nitrogen-fixing legume with specialised Bradyrhizobium partnerships, it contributes " +
                "nitrogen to wetland systems. Seeds disperse by wind and water and can remain viable for years " +
                "in waterlogged banks."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Lightweight timber for floats, rafts and craft; ornamental value; cultural names " +
                "across African wetlands.\n\n" +
                "For wildlife: Dense stands shelter fish and other animals; specialised seed predators such as " +
                "Bruchidius weevils are associated with the species."
        );
        t.setCommonAreas(
                "Indigenous across most of tropical Africa — from West Africa (including Senegal) east to " +
                "Ethiopia and south to Mozambique, Malawi, northern Zimbabwe and Angola. Also in Madagascar " +
                "(possibly naturalised), notably around Lake Alaotra.\n\n" +
                "Typical habitats: swamps, lake shores and river margins in 1–2 m of freshwater, at roughly " +
                "70–1,850 m elevation. Also recorded naturalised at Lake Okeechobee, Florida."
        );
        t.setAdditionalInfo(
                "Species reference (Wikipedia): " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Ambatch / Umburu label for photos, map, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: ambatch, pith-tree, balsa wood tree, umburu, Nile pith tree, boboffee.\n" +
                "Family: Fabaceae. Conservation: IUCN Least Concern."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umuburu");
        t.setShortDescription(
                "Umuburu (Aeschynomene elaphroxylon / Ambatch) — igiti cyangwa igishyitsi gikura mu mazi " +
                "n'ibishanga by'Afurika, gifite ibiti byoroshye cyane kandi bitabora vuba."
        );
        t.setInterestingFacts(String.join("\n",
                "Amazina: ambatch, pith-tree, umburu / umuburu.",
                "Gikura nk'igiti cy'amazi y'inyanja y'inyanja — mu mazi y'inyanja y'inyanja.",
                "Ibiti byoroshye kurusha cork, ariko biramba.",
                "Bishobora gukora udutsiko two kugenda hejuru y'amazi.",
                "Dufite bakteriya zifasha gufata azote.",
                "IUCN: Least Concern."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae",
                "Izina ry'ubumenyi: Aeschynomene elaphroxylon",
                "Izina ry'icyongereza: Ambatch / pith-tree",
                "Izina ry'ikinyarwanda: Umuburu",
                "Uburebure: metero 2–9 (kugeza ~12)",
                "Imizi: Imizi myinshi yo mu mazi n'ubutaka bw'amazi",
                "Indabo: Umuhondo / orange, zinini",
                "Aho zikura: Ibishanga, imigezi, imbibi z'amazi"
        ));
        t.setDescription(
                "Aeschynomene elaphroxylon (Ambatch) ni igiti cy'umuryango wa Fabaceae. Mu Rwanda twagira " +
                "tukacyita Umuburu. Gikunda gukura mu mazi y'inyanja y'inyanja, mu bishanga n'imbibi z'imigezi.\n\n" +
                "Umubumbe wacyo ushobora kuba munini kandi ufite amahwa. Ibiti byacyo byoroshye cyane — " +
                "bikagufasha kuguma hejuru y'amazi. Imizi ikura ku ishami n'umubumbe igakora uruzitiro mu mazi.\n\n" +
                "Mu Kigali Eco-Park iki giti cyerekana Ambatch nkuko byanditswe kuri Wikipedia, hamwe n'izina " +
                "ry'aho Umuburu."
        );
        t.setUses(
                "Ubucukuzi n'ubwato: Ibiti byoroshye bikoreshwa nk'ibyo kugira netsi zo gufata amafi; " +
                "imibumbe ikoreshwa mu gukora amato n'udutsiko.\n\n" +
                "Ubucukuzi: Intebe, ibipfunyika by'izuba, n'ibindi.\n\n" +
                "Ubwiza: Indabo zinini zishobora guterwa ku bwiza.\n\n" +
                "Iburira: Aho byiyongera cyane bishobora kugora abavuzi b'amafi, ariko bifasha inyamaswa."
        );
        t.setEcologicalImportance(
                "Umuburu ufasha mu kubungabunga imbibi z'amazi n'ibishanga. Imizi yacyo ifasha mu guhamya " +
                "ubutaka n'ubuzima bw'inyoni n'amafi. Nk'igiti cya legume, gifasha gufata azote mu bidukikije."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Ibiti byo gukora floats n'amato, ubukorikori, n'ubwiza.\n\n" +
                "Ku nyamaswa: Ahantu ho kwihisha mu mats y'imizi."
        );
        t.setCommonAreas(
                "Iboneka mu Afurika y'ubuturo — kuva Senegal kugeza Etiyopiya n'i Mozambique. Hano no muri " +
                "Madagascar. Imiterere: ibishanga, imbibi z'amazi, imigezi (metero 70–1,850)."
        );
        t.setAdditionalInfo(
                "Inkomoko (Wikipedia): " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umuburu kugira ngo ubone amafoto n'amajwi.\n\n" +
                "Ambatch · Aeschynomene elaphroxylon · Umuburu · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Ambatch");
        t.setShortDescription(
                "Ambatch (Aeschynomene elaphroxylon) — aussi appelé arbre à moelle ou umburu. Légumineuse " +
                "semi-aquatique d'Afrique tropicale, à bois extrêmement léger et résistant à la pourriture."
        );
        t.setInterestingFacts(String.join("\n",
                "Noms : ambatch, pith-tree, umburu, boboffee.",
                "Pousse comme une mangrove d'eau douce — souvent dans 1–2 m d'eau.",
                "Bois plus léger que le liège, spongieux et durable.",
                "Peut former des radeaux ou îles flottantes de végétation.",
                "Fixe l'azote grâce à Bradyrhizobium (nodules caulinaires et racinaires).",
                "Statut UICN : Préoccupation mineure."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae",
                "Nom scientifique : Aeschynomene elaphroxylon",
                "Nom commun : Ambatch",
                "Nom local : Umburu / Umuburu",
                "Hauteur : 2–9 m (jusqu'à ~12 m)",
                "Tronc : Renflé, parfois épineux",
                "Fleurs : Jaunes à orangées, assez grandes",
                "Habitat : Marais, lacs et rivières d'eau douce"
        ));
        t.setDescription(
                "Aeschynomene elaphroxylon est un arbuste ou petit arbre sempervirent semi-aquatique de la " +
                "famille des Fabacées. Il pousse dans les marais, lacs et rivières d'Afrique tropicale, à la " +
                "manière d'une mangrove d'eau douce.\n\n" +
                "Le tronc est renflé, parfois épineux ; le bois spongieux et très léger aide la plante à " +
                "flotter. Des racines adventives abondantes forment des enchevêtrements dans l'eau peu profonde.\n\n" +
                "Au Kigali Eco-Park, cette fiche suit le profil Wikipedia « Aeschynomene elaphroxylon » avec " +
                "le nom Ambatch / Umburu."
        );
        t.setUses(
                "Pêche et bateaux : Bois pour flotteurs de filets ; troncs liés en radeaux et petites embarcations.\n\n" +
                "Artisanat : Tabourets, écrans solaires ; autrefois sandales dans certaines régions.\n\n" +
                "Ornement : Grandes fleurs jaunes à orangées.\n\n" +
                "Note : Les peuplements denses peuvent gêner la pêche tout en abritant la faune."
        );
        t.setEcologicalImportance(
                "Colonise rapidement les zones humides d'eau douce et forme des peuplements denses. Les " +
                "racines stabilisent les berges et créent des habitats. En tant que légumineuse fixatrice " +
                "d'azote, elle enrichit les écosystèmes riverains."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Flotteurs, radeaux, artisanat et valeur ornementale.\n\n" +
                "Pour la faune : Abrish dans les enchevêtrements de racines."
        );
        t.setCommonAreas(
                "Afrique tropicale — du Sénégal à l'Éthiopie et vers le Mozambique et l'Angola ; aussi à " +
                "Madagascar. Habitats : marais, lacs et rivières entre environ 70 et 1 850 m."
        );
        t.setAdditionalInfo(
                "Référence Wikipedia : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Ambatch / Umburu pour photos, carte et médias.\n\n" +
                "Ambatch · Aeschynomene elaphroxylon · Umburu · Fabaceae · UICN LC."
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
