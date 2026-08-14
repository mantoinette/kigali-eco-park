package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Senegalia polyacantha subsp. campylacantha (Umuharata / Falcon's claw acacia) — TREE-008.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class SenegaliaPolyacanthaCampylacanthaData {

    public static final String SLUG = "senegalia-polyacantha-campylacantha";
    public static final String SCIENTIFIC_NAME = "Senegalia polyacantha subsp. campylacantha";
    public static final String QR_CODE_ID = "TREE-008";
    public static final String FAMILY = "Fabaceae (Legume / pea family)";
    public static final String TYPICAL_HEIGHT = "8–15 m (up to 20 m)";
    public static final String ORIGIN = "Tropical and southern Africa — woodland pioneer from Sudan to South Africa; native in Rwanda and East Africa";
    public static final String AGE_ESTIMATE = "Approx. 10–30 years (park specimen)";
    public static final double LATITUDE = -1.9684;
    public static final double LONGITUDE = 30.1080;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-008";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-008";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Senegalia_polyacantha";

    private SenegaliaPolyacanthaCampylacanthaData() {}

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
        tree.setDisplayOrder(8);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/4/47/Acacia_polyacantha%2C_habitus%2C_Walter_Sisulu_NBT.jpg",
                        "Senegalia polyacantha — white thorn / falcon's claw acacia habit",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/8/82/Acacia_polyacantha%2C_blaar%2C_Walter_Sisulu_NBT.jpg",
                        "Bipinnate compound leaves of Umuharata",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/c/c6/1-Acacia_polyacantha_06.JPG",
                        "Acacia polyacantha — thorny woodland acacia",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/b/ba/1-Acacia_polyacantha_03.JPG",
                        "Falcon's claw acacia branch and foliage",
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
                "Senegalia polyacantha subsp. campylacantha — falcon's claw acacia or white thorn, known in " +
                "Kinyarwanda as Umuharata or Ruganambuga. A thorny woodland pioneer legume valued for nitrogen " +
                "fixation, traditional medicine and its distinctive hooked thorns."
        );
        t.setInterestingFacts(String.join("\n",
                "Local names (Kinyarwanda): Umuharata · Ruganambuga.",
                "Also called white thorn — formerly classified as Acacia polyacantha subsp. campylacantha.",
                "Pioneer species of woodland and forest margins in tropical Africa.",
                "Fixes nitrogen in the soil through root nodules.",
                "Leaves pounded, dried and ground for application to sores.",
                "Roots used in traditional treatment of snakebites."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae (Legume / pea family)",
                "Scientific name: Senegalia polyacantha subsp. campylacantha (A.Rich.) Kyal. & Boatwr.",
                "Synonym: Acacia polyacantha subsp. campylacantha",
                "Common name: Falcon's claw acacia · White thorn",
                "Local names (Kinyarwanda): Umuharata · Ruganambuga",
                "Typical height: 8–15 m",
                "Ecological role: Woodland pioneer; nitrogen fixation",
                "Status in Rwanda: Native indigenous tree"
        ));
        t.setDescription(
                "Senegalia polyacantha subsp. campylacantha is the falcon's claw acacia — a thorny legume tree " +
                "native to tropical and southern Africa. In Rwanda it is known as Umuharata or Ruganambuga and " +
                "is recognised as a pioneer of woodland vegetation.\n\n" +
                "The tree bears hooked white thorns, bipinnate leaves and cream-coloured flower spikes. As a " +
                "legume it enriches soil through nitrogen fixation. In ethnobotany, leaf preparations are applied " +
                "to sores and roots are used for snakebite treatment.\n\n" +
                "Roots are also recorded as emitting chemical compounds that repel snakes, crocodiles and rats " +
                "in local practice.\n\n" +
                "At Kigali Eco-Park this species is presented as Umuharata / falcon's claw acacia, matching " +
                "local naming and documented uses."
        );
        t.setUses(
                "Ecological: Pioneer of woodland forest; nitrogen fixation in soil.\n\n" +
                "Traditional medicine: Leaves pounded, dried, ground and applied to sores; roots used to treat snakebites.\n\n" +
                "Repellent: Roots recorded as releasing compounds that repel snakes, crocodiles and rats.\n\n" +
                "Woodland restoration: Early coloniser of disturbed ground and forest margins."
        );
        t.setEcologicalImportance(
                "As a nitrogen-fixing pioneer, this acacia helps restore degraded woodland and improves soil fertility " +
                "for other plants. Thorns and dense branching provide shelter for birds and small mammals; flowers " +
                "support pollinators in dry-season woodlands."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Traditional medicine for sores and snakebite; soil enrichment through nitrogen fixation.\n\n" +
                "For wildlife: Cover, nesting sites and nectar in woodland ecosystems."
        );
        t.setCommonAreas(
                "Native across tropical Africa from East Africa to South Africa. Found in woodland, bushveld and " +
                "forest margins; tolerates seasonal drought and poor soils."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umuharata label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: falcon's claw acacia · white thorn · Umuharata · Ruganambuga · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umuharata");
        t.setShortDescription(
                "Umuharata (Senegalia polyacantha subsp. campylacantha / Falcon's claw acacia) — igiti cy'igihugu " +
                "cy'amasaka gifite amara y'impanda n'akamaro mu buvuzi n'ifumbire y'ubutaka."
        );
        t.setInterestingFacts(String.join("\n",
                "Amazina y'ikinyarwanda: Umuharata · Ruganambuga.",
                "Ni igiti cy'imbere mu gusubiza amasaka.",
                "Gushyira nitrogen mu butaka — ifasha gufumbira ubutaka.",
                "Amababi akoreshwa mu kuvura ibisebe.",
                "Imizi ikoreshwa mu kuvura inzoka.",
                "Imizi yirukana inzoka n'imbeba."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae",
                "Izina ry'ubumenyi: Senegalia polyacantha subsp. campylacantha",
                "Izina ry'ikinyarwanda: Umuharata · Ruganambuga",
                "Izina ry'icyongereza: Falcon's claw acacia",
                "Uburebure: metero 8–15",
                "Akamaro k'ibidukikije: Gufumbira ubutaka (nitrogen)",
                "Aho ukura: Amasaka n'impera z'ibihanga mu Rwanda"
        ));
        t.setDescription(
                "Senegalia polyacantha subsp. campylacantha ni Umuharata — igiti cy'igihugu cy'umuryango wa Fabaceae. " +
                "Mu Rwanda twagira tukacyita Umuharata cyangwa Ruganambuga.\n\n" +
                "Ifite amara menshi y'impanda, amababi akomatanyije n'indabyo z'umweru. Ni igiti cy'imbere mu " +
                "gusubiza amasaka kandi gifasha gushyira nitrogen mu butaka.\n\n" +
                "Mu buvuzi gakondo, amababi akafashwa ku bisebe n'imizi ikoreshwa mu kuvura inzoka. Imizi " +
                "yirukanwa n'inzoka n'imbeba mu mico y'aho.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umuharata / falcon's claw acacia hakurikijwe amazina " +
                "n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Kugombora urumwe n'inzoka — imizi ikoreshwa mu kuvura inzoka.\n" +
                "• Gushyira nitrogen mu butaka — ifasha gufumbira ubutaka.\n" +
                "• Imizi yirukana inzoka n'imbeba.\n\n" +
                "Kuvura:\n" +
                "• Amababi akafashwa, akumurwa, akavunika akashyirwa ku bisebe.\n" +
                "• Imizi ikoreshwa mu kuvura inzoka."
        );
        t.setEcologicalImportance(
                "Umuharata ni igiti cy'imbere mu gusubiza amasaka — gushyira nitrogen mu butaka no gutanga " +
                "igicucu. Amara n'amababi menshi atanga ahantu ho kwihisha inyamaswa."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Ubuvuzi, ifumbire y'ubutaka, no kurinda inzoka n'imbeba.\n\n" +
                "Ku nyamaswa: Igicucu, indabyo n'ahantu ho kwihisha mu masaka."
        );
        t.setCommonAreas(
                "Uboneka mu Rwanda n'Afurika y'ubushyuhe — mu masaka, ku mpera z'ibihanga no mu bihugu " +
                "bikomeye."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umuharata kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umuharata · Ruganambuga · Senegalia polyacantha · Falcon's claw acacia · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Acacia à griffes de faucon");
        t.setShortDescription(
                "Senegalia polyacantha subsp. campylacantha — acacia à griffes de faucon, appelé Umuharata ou " +
                "Ruganambuga en kinyarwanda. Légumineuse épineuse pionnière des bois, fixatrice d'azote et " +
                "utilisée en médecine traditionnelle."
        );
        t.setInterestingFacts(String.join("\n",
                "Noms locaux : Umuharata · Ruganambuga.",
                "Anciennement Acacia polyacantha subsp. campylacantha.",
                "Espèce pionnière des lisières forestières.",
                "Fixation de l'azote dans le sol.",
                "Feuilles broyées pour les plaies ; racines contre morsures de serpent.",
                "Racines réputées repousser serpents et rongeurs."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae",
                "Nom scientifique : Senegalia polyacantha subsp. campylacantha",
                "Noms communs : Acacia à griffes de faucon · Épine blanche",
                "Noms locaux : Umuharata · Ruganambuga",
                "Hauteur : 8–15 m",
                "Rôle : Pionnier forestier ; fixation de l'azote"
        ));
        t.setDescription(
                "Senegalia polyacantha subsp. campylacantha est l'acacia à griffes de faucon — arbre épineux " +
                "indigène d'Afrique tropicale. Au Rwanda, on l'appelle Umuharata ou Ruganambuga.\n\n" +
                "Épines crochues, feuilles bipinnées et épis de fleurs crème. Légumineuse fixatrice d'azote " +
                "et pionnière des boisements.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umuharata selon les usages locaux."
        );
        t.setUses(
                "Écologique : Pionnier des bois ; fixation de l'azote.\n\n" +
                "Médecine traditionnelle : Feuilles broyées et séchées sur les plaies ; racines pour morsures de serpent.\n\n" +
                "Répellent : Racines repoussant serpents, crocodiles et rats selon l'ethnobotanique locale."
        );
        t.setEcologicalImportance(
                "Restauration des boisements et enrichissement du sol par fixation de l'azote ; abri et nectar " +
                "pour la faune des savanes boisées."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Médecine, fertilisation des sols.\n\n" +
                "Pour la faune : Couvert et pollinisation."
        );
        t.setCommonAreas(
                "Indigène du Rwanda et de l'Afrique tropicale — bois, lisières et brousse."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umuharata pour photos, carte et médias.\n\n" +
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
