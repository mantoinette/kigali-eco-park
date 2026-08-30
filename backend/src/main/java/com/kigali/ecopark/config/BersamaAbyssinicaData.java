package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Bersama abyssinica (Umurera / Umurerabana) — TREE-023.
 * Native Melianthaceae tree of East and Central African forests.
 */
public final class BersamaAbyssinicaData {

    public static final String SLUG = "bersama-abyssinica";
    public static final String SCIENTIFIC_NAME = "Bersama abyssinica";
    public static final String QR_CODE_ID = "TREE-023";
    public static final String FAMILY = "Melianthaceae";
    public static final String TYPICAL_HEIGHT = "4–15 m";
    public static final String ORIGIN = "Tropical and subtropical Africa — native in Rwanda and the Great Lakes region";
    public static final String AGE_ESTIMATE = "Approx. 10–30 years (park specimen)";
    public static final double LATITUDE = -1.9678;
    public static final double LONGITUDE = 30.1105;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-023";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-023";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Bersama_abyssinica";

    private BersamaAbyssinicaData() {}

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
        tree.getCategories().addAll(List.of("MEDICINAL", "SHADE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(23);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=1280&h=720&q=80",
                        "Umurera — forest tree in natural habitat",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://images.unsplash.com/photo-1511497584788-876760111969?auto=format&fit=crop&w=1280&h=720&q=80",
                        "Umurera — woodland canopy",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://images.unsplash.com/photo-1426604966848-d7ad83d69b7b?auto=format&fit=crop&w=1280&h=720&q=80",
                        "Umurera — forest landscape",
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
        TreeTranslation t = base(tree, "en", "African bersama");
        t.setShortDescription(
                "Bersama abyssinica — African bersama, known in Kinyarwanda as Umurera (also Umurerabana). " +
                "A native forest tree valued in traditional medicine, agroforestry and woodland restoration."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umurera; also Umurerabana or Umukaka.",
                "Family: Melianthaceae.",
                "Found in secondary forest and forest galleries across Rwanda.",
                "Used in traditional medicine — all parts are toxic if misused.",
                "Provides firewood, charcoal, timber and live fencing in agroforestry.",
                "Fast-growing native species suitable for scattered cropland plantings."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Melianthaceae",
                "Scientific name: Bersama abyssinica Fresen.",
                "Common name: African bersama",
                "Local name (Kinyarwanda): Umurera",
                "Typical height: 4–15 m",
                "Distribution: Tropical and subtropical Africa, including Rwanda",
                "Park ID: TREE-023"
        ));
        t.setDescription(
                "Bersama abyssinica is a medium-sized tree of the family Melianthaceae, native to tropical and " +
                "subtropical Africa. In Rwanda it is known as Umurera or Umurerabana.\n\n" +
                "The species grows in secondary forest, forest galleries and disturbed woodland. It is valued for " +
                "shade, live fencing, firewood, charcoal and construction timber, and features in traditional " +
                "medicine — though plant parts are toxic and require expert knowledge.\n\n" +
                "At Kigali Eco-Park this TREE-023 specimen is presented as Umurera, matching local naming and " +
                "documented uses."
        );
        t.setUses(
                "Traditional medicine: Used in ethnobotanical practice for various ailments — toxic if misused.\n\n" +
                "Wood products: Firewood, charcoal and timber for construction.\n\n" +
                "Agroforestry: Live fencing, shade and scattered cropland plantings."
        );
        t.setEcologicalImportance(
                "Contributes to secondary forest structure and forest-gallery vegetation in Rwanda; supports " +
                "understorey shade and woodland restoration on disturbed land."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Shade, fencing, fuelwood, timber and traditional medicine.\n\n" +
                "For wildlife: Forest-edge habitat and canopy cover in secondary woodland."
        );
        t.setCommonAreas(
                "Secondary forests and forest galleries in Rwanda, Burundi and neighbouring East and Central " +
                "African countries."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umurera (TREE-023) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: Umurera · Umurerabana · Umukaka · Melianthaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umurera");
        t.setShortDescription(
                "Umurera (Bersama abyssinica) — igiti cy'igihugu cy'umuryango wa Melianthaceae. Gikoreshwa mu " +
                "buvuzi gakondo, ubwubatsi n'ubuhinzi bw'ibiti — ariko gifite uburozi niba bikoreshejwe nabi."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umurera (Umurerabana / Umukaka).",
                "Ukura mu mashyamba ya kabiri n'imigezi y'amasaka.",
                "Gikoreshwa mu buvuzi gakondo — gifite uburozi.",
                "Gitanga inkoni, umuriro, ibiti byo kubaka n'uruzitiro.",
                "Umuryango: Melianthaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Melianthaceae",
                "Izina ry'ubumenyi: Bersama abyssinica",
                "Izina ry'ikinyarwanda: Umurera",
                "Ikimenyetso: TREE-023"
        ));
        t.setDescription(
                "Bersama abyssinica ni Umurera — igiti cy'igihugu cy'u Rwanda. Ukura mu mashyamba ya kabiri, " +
                "mu migezi y'amasaka no mu bisenge by'ishyamba.\n\n" +
                "Akamaro: ubuvuzi gakondo (bikeneye ubumenyi), inkoni, umuriro, ibiti byo kubaka, uruzitiro " +
                "n'urugara. Ibice byose bifite uburozi niba bikoreshejwe nabi.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umurera (TREE-023) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Ubuvuzi gakondo: Bikoreshwa mu buvuzi — bikeneye ubumenyi kuko bifite uburozi.\n\n" +
                "Inkoni n'ibiti: Umuriro, ibiti byo kubaka n'uruzitiro.\n\n" +
                "Ubuhinzi bw'ibiti: Urugara n'uruzitiro mu mirima."
        );
        t.setEcologicalImportance(
                "Umurera ufasha gusana ishyamba rya kabiri n'imigezi y'amasaka; utanga urugara mu bisenge by'ishyamba."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Urugara, inkoni, ibiti, ubuvuzi gakondo n'uruzitiro.\n\n" +
                "Ku nyamaswa: Ubuturo mu bisenge by'ishyamba."
        );
        t.setCommonAreas(
                "Mu mashyamba ya kabiri n'imigezi y'amasaka y'u Rwanda n'ibihugu baturanye."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umurera (TREE-023) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umurera · Bersama abyssinica · Melianthaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Bersama d'Abyssinie");
        t.setShortDescription(
                "Bersama abyssinica — appelé Umurera en kinyarwanda. Arbre forestier indigène utilisé en " +
                "médecine traditionnelle et agroforesterie."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umurera (Umurerabana).",
                "Forêts secondaires et galeries forestières.",
                "Médecine traditionnelle — plante toxique si mal utilisée.",
                "Bois de feu, charbon de bois et bois d'œuvre.",
                "Famille : Melianthaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Melianthaceae",
                "Nom scientifique : Bersama abyssinica",
                "Nom local : Umurera",
                "Identifiant parc : TREE-023"
        ));
        t.setDescription(
                "Bersama abyssinica est un arbre de la famille Melianthaceae. Au Rwanda : Umurera.\n\n" +
                "Usages : médecine traditionnelle, bois de feu, charbon, bois d'œuvre et haies vives.\n\n" +
                "Au Kigali Eco-Park, cette fiche TREE-023 présente Umurera selon les usages locaux."
        );
        t.setUses(
                "Médecine traditionnelle : Usage ethnobotanique — toxique si mal dosé.\n\n" +
                "Bois : Feu de bois, charbon et construction.\n\n" +
                "Agroforesterie : Haies vives et ombrage."
        );
        t.setEcologicalImportance(
                "Arbre des forêts secondaires et galeries forestières ; ombrage et restauration boisée."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Ombrage, bois, médecine traditionnelle.\n\n" +
                "Pour la faune : Habitat en lisière forestière."
        );
        t.setCommonAreas(
                "Forêts secondaires du Rwanda, du Burundi et d'Afrique orientale et centrale."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umurera (TREE-023) pour photos, carte et médias.\n\n" +
                "Umurera · Bersama abyssinica · Melianthaceae."
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
