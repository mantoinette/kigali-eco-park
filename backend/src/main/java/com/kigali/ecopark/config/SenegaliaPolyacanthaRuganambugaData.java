package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Senegalia polyacantha subsp. campylacantha (Ruganambuga / Umuharata / Falcon's claw acacia) — TREE-012.
 * Second park specimen of the falcon's claw acacia; content aligned with park ethnobotanical notes.
 * Distinct from TREE-008 (Umuharata) so each QR label has its own guide page.
 */
public final class SenegaliaPolyacanthaRuganambugaData {

    public static final String SLUG = "senegalia-polyacantha-ruganambuga";
    public static final String SCIENTIFIC_NAME = "Senegalia polyacantha subsp. campylacantha";
    public static final String QR_CODE_ID = "TREE-012";
    public static final String FAMILY = "Fabaceae (Legume / pea family)";
    public static final String TYPICAL_HEIGHT = "8–15 m (up to 20 m)";
    public static final String ORIGIN = "Tropical and southern Africa — woodland pioneer; native in Rwanda and East Africa";
    public static final String AGE_ESTIMATE = "Approx. 10–30 years (park specimen)";
    public static final double LATITUDE = -1.9672;
    public static final double LONGITUDE = 30.1092;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-012";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-012";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Senegalia_polyacantha";

    private SenegaliaPolyacanthaRuganambugaData() {}

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
        tree.getCategories().addAll(List.of("MEDICINAL", "WILDLIFE", "SHADE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(12);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/b/ba/1-Acacia_polyacantha_03.JPG",
                        "Senegalia polyacantha — falcon's claw acacia (Ruganambuga)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/c/c6/1-Acacia_polyacantha_06.JPG",
                        "Thorny woodland acacia — Umuharata / Ruganambuga",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/4/47/Acacia_polyacantha%2C_habitus%2C_Walter_Sisulu_NBT.jpg",
                        "Habit of Acacia polyacantha / Senegalia polyacantha",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/8/82/Acacia_polyacantha%2C_blaar%2C_Walter_Sisulu_NBT.jpg",
                        "Compound leaves of falcon's claw acacia",
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
        TreeTranslation t = base(tree, "en", "Falcon's claw acacia");
        t.setShortDescription(
                "Senegalia polyacantha subsp. campylacantha — falcon's claw acacia, known in Kinyarwanda as " +
                "Umuharata or Ruganambuga. A woodland pioneer that fixes nitrogen; used for sores and snakebite; " +
                "roots said to repel snakes, crocodiles and rats."
        );
        t.setInterestingFacts(String.join("\n",
                "Local names (Kinyarwanda): Umuharata · Ruganambuga.",
                "Synonym: Acacia polyacantha subsp. campylacantha.",
                "Pioneer of woodland forest.",
                "Nitrogen fixation in the soil.",
                "Leaves pounded, dried and ground, then applied to sores.",
                "Roots used to treat snakebites.",
                "Roots emit compounds that repel animals including crocodiles, snakes and rats."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae",
                "Scientific name: Senegalia polyacantha subsp. campylacantha",
                "Synonym: Acacia polyacantha subsp. campylacantha",
                "Common name: Falcon's claw acacia",
                "Local names (Kinyarwanda): Umuharata · Ruganambuga",
                "Typical height: 8–15 m",
                "Ecological role: Woodland pioneer; nitrogen fixation",
                "Park ID: TREE-012"
        ));
        t.setDescription(
                "Senegalia polyacantha subsp. campylacantha (also written Acacia polyacantha subsp. campylacantha) " +
                "is the falcon's claw acacia — a thorny legume native to tropical Africa. In Rwanda it is called " +
                "Umuharata or Ruganambuga.\n\n" +
                "It is a pioneer of woodland forest and fixes nitrogen in the soil. Leaves are pounded, dried and " +
                "ground, then applied to sores; roots are used to treat snakebites. Roots are also recorded as " +
                "emitting chemical compounds that repel animals including crocodiles, snakes and rats.\n\n" +
                "At Kigali Eco-Park this TREE-012 specimen is presented as Ruganambuga / Umuharata / falcon's claw " +
                "acacia, matching local naming and documented uses."
        );
        t.setUses(
                "Ecological: Pioneer of the woodland forest; nitrogen fixation.\n\n" +
                "Medicinal: Leaves are pounded, dried, ground and then applied to sores. Roots are used to treat snakebites.\n\n" +
                "Repellent: Roots emit a chemical compound that repels animals including crocodiles, snakes and rats.\n\n" +
                "Kinyarwanda notes: Kugombora urumwe n'inzoka; gushyira nitrogen mu butaka; imizi yirukana inzoka n'imbeba."
        );
        t.setEcologicalImportance(
                "As a nitrogen-fixing pioneer, this acacia helps restore degraded woodland and improves soil " +
                "fertility. Thorns and branching provide shelter for birds and small mammals."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Traditional medicine for sores and snakebite; soil enrichment through nitrogen fixation.\n\n" +
                "For wildlife: Cover and nesting sites in woodland ecosystems."
        );
        t.setCommonAreas(
                "Native across tropical Africa. Found in woodland, bushveld and forest margins; tolerates seasonal " +
                "drought and poor soils."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Ruganambuga / Umuharata (TREE-012) label for photos, map " +
                "location, and multilingual audio/video.\n\n" +
                "Also known as: falcon's claw acacia · white thorn · Umuharata · Ruganambuga · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Ruganambuga");
        t.setShortDescription(
                "Ruganambuga / Umuharata (Senegalia polyacantha subsp. campylacantha / Falcon's claw acacia) — " +
                "igiti cy'igihugu cy'amasaka gifasha gushyira nitrogen mu butaka no mu buvuzi."
        );
        t.setInterestingFacts(String.join("\n",
                "Amazina y'ikinyarwanda: Umuharata · Ruganambuga.",
                "Ni igiti cy'imbere mu masaka.",
                "Gushyira nitrogen mu butaka.",
                "Kugombora urumwe n'inzoka.",
                "Imizi yirukana inzoka n'imbeba."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae",
                "Izina ry'ubumenyi: Senegalia polyacantha subsp. campylacantha",
                "Izina ry'ikinyarwanda: Umuharata · Ruganambuga",
                "Izina ry'icyongereza: Falcon's claw acacia",
                "Uburebure: metero 8–15",
                "Akamaro: Nitrogen mu butaka; ubuvuzi",
                "Ikimenyetso: TREE-012"
        ));
        t.setDescription(
                "Senegalia polyacantha subsp. campylacantha ni Umuharata cyangwa Ruganambuga — igiti cy'igihugu " +
                "cy'umuryango wa Fabaceae.\n\n" +
                "Akamaro: kugombora urumwe n'inzoka; gushyira nitrogen mu butaka; imizi yirukana inzoka n'imbeba.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Ruganambuga / Umuharata / falcon's claw acacia " +
                "(TREE-012) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Kugombora urumwe n'inzoka.\n" +
                "• Gushyira nitrogen mu butaka.\n" +
                "• Imizi yirukana inzoka n'imbeba.\n\n" +
                "Ubuvuzi: Amababi akafashwa ku bisebe; imizi ikoreshwa mu kuvura inzoka."
        );
        t.setEcologicalImportance(
                "Nk'igiti cy'imbere cy'amasaka gifasha gufumbira ubutaka no gutanga ahantu ku nyoni."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Ubuvuzi gakondo n'ifumbire y'ubutaka.\n\n" +
                "Ku nyamaswa: Ahantu ho kwihisha mu masaka."
        );
        t.setCommonAreas(
                "Uboneka mu Rwanda n'Afurika y'ubushyuhe — amasaka n'impera z'ibihanga."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Ruganambuga / Umuharata (TREE-012).\n\n" +
                "Umuharata · Ruganambuga · Senegalia polyacantha · Falcon's claw acacia · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Acacia à griffes de faucon");
        t.setShortDescription(
                "Senegalia polyacantha subsp. campylacantha — acacia à griffes de faucon, appelé Umuharata ou " +
                "Ruganambuga en kinyarwanda. Pionnier des bois, fixation d'azote, usages médicinaux et répulsifs."
        );
        t.setInterestingFacts(String.join("\n",
                "Noms locaux : Umuharata · Ruganambuga.",
                "Pionnier des forêts claires.",
                "Fixation d'azote.",
                "Feuilles pour les plaies ; racines contre les morsures de serpent.",
                "Racines réputées repousser crocodiles, serpents et rats."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae",
                "Nom scientifique : Senegalia polyacantha subsp. campylacantha",
                "Nom commun : Acacia à griffes de faucon",
                "Noms locaux : Umuharata · Ruganambuga",
                "Hauteur : 8–15 m",
                "Identifiant parc : TREE-012"
        ));
        t.setDescription(
                "Senegalia polyacantha subsp. campylacantha est l'acacia à griffes de faucon. Au Rwanda : " +
                "Umuharata ou Ruganambuga.\n\n" +
                "Usages : pionnier forestier, fixation d'azote, soins des plaies et morsures, composés répulsifs " +
                "des racines.\n\n" +
                "Au Kigali Eco-Park, le spécimen TREE-012 est présenté comme Ruganambuga / Umuharata."
        );
        t.setUses(
                "Écologie : pionnier des bois ; fixation d'azote.\n\n" +
                "Médicinal : feuilles broyées pour les plaies ; racines contre les morsures de serpent.\n\n" +
                "Répulsif : composés des racines contre crocodiles, serpents et rats."
        );
        t.setEcologicalImportance(
                "Améliore la fertilité des sols et offre abri à la faune des boisements."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Médecine traditionnelle et fertilité des sols.\n\n" +
                "Pour la faune : Abri dans les boisements."
        );
        t.setCommonAreas(
                "Indigène en Afrique tropicale — boisements et lisières."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Ruganambuga / Umuharata (TREE-012).\n\n" +
                "Umuharata · Ruganambuga · Senegalia polyacantha · Fabaceae."
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
