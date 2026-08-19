package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Ficus thonningii (Umuvumu / wild fig) — TREE-017.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class FicusThonningiiData {

    public static final String SLUG = "ficus-thonningii";
    public static final String SCIENTIFIC_NAME = "Ficus thonningii";
    public static final String QR_CODE_ID = "TREE-017";
    public static final String FAMILY = "Moraceae (Fig / mulberry family)";
    public static final String TYPICAL_HEIGHT = "Medium tree to about 20 m";
    public static final String ORIGIN = "Tropical and subtropical Africa";
    public static final String AGE_ESTIMATE = "Approx. 15–50 years (park specimen)";
    public static final double LATITUDE = -1.9674;
    public static final double LONGITUDE = 30.1104;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-017";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-017";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Ficus_thonningii";

    private FicusThonningiiData() {}

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
        tree.getCategories().addAll(List.of("MEDICINAL", "TIMBER", "FIBRE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(17);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/c/c7/Mulemba.jpg",
                        "Ficus thonningii — wild fig tree (Umuvumu)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/8/8d/Ficus_thonningii_14zz.jpg",
                        "Wild fig habit and canopy",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/a/a7/Ficus_thonningii_fruits_MHNT.jpg",
                        "Ficus thonningii figs",
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
        TreeTranslation t = base(tree, "en", "Wild fig");
        t.setShortDescription(
                "Ficus thonningii — the wild fig, known in Kinyarwanda as Umuvumu. Planted around rural homes " +
                "and royal burial grounds; bark for inkanda cloth, bole for utensils, leaves for livestock fodder."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umuvumu.",
                "Also known as mulemba in parts of Africa.",
                "Planted around rural houses and royal burial grounds.",
                "Bark traditionally used for inkanda cloth.",
                "Leaves and figs used in traditional medicine.",
                "Family: Moraceae (fig family)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Moraceae (Fig / mulberry family)",
                "Scientific name: Ficus thonningii Blume",
                "Common name: Wild fig",
                "Local name (Kinyarwanda): Umuvumu",
                "Typical height: medium tree to about 20 m",
                "Distribution: tropical and subtropical Africa",
                "Park ID: TREE-017"
        ));
        t.setDescription(
                "Ficus thonningii is a widespread African wild fig of the family Moraceae. In Rwanda it is known " +
                "as Umuvumu. It is commonly planted around rural homesteads and royal burial grounds.\n\n" +
                "The bole was used to make utensils such as boats, mortars and troughs. Bark fibre was processed " +
                "into inkanda — a traditional cloth worn in Rwanda. Leaves provide fodder for livestock, especially " +
                "in the dry season.\n\n" +
                "In traditional medicine, leaves and figs are used to treat diarrhoea, gonorrhoea and diabetes mellitus.\n\n" +
                "At Kigali Eco-Park this TREE-017 specimen is presented as Umuvumu / wild fig, matching local naming " +
                "and documented uses."
        );
        t.setUses(
                "Planting: Around rural houses and royal burial grounds.\n\n" +
                "Craft & timber: Bole for boats, mortars and troughs; bark for inkanda cloth.\n\n" +
                "Livestock: Leaves used as fodder, especially in dry seasons.\n\n" +
                "Traditional medicine: Leaves and figs for diarrhoea, gonorrhoea and diabetes mellitus."
        );
        t.setEcologicalImportance(
                "Wild fig supports fig-wasp pollination and provides fruit and shade for birds and other wildlife " +
                "across savanna and woodland landscapes."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Inkanda cloth, utensils, fodder and traditional medicine.\n\n" +
                "For wildlife: Figs and canopy shelter for birds and mammals."
        );
        t.setCommonAreas(
                "Widespread in tropical and subtropical Africa, including Rwanda — savanna, woodland and around homesteads."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umuvumu (TREE-017) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: wild fig · mulemba · Umuvumu · Moraceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umuvumu");
        t.setShortDescription(
                "Umuvumu (Ficus thonningii / wild fig) — igiti cy'umuvumu gikunze guhingwa hafi y'amazu n'imisozi y'abami. " +
                "Amababi agaburira amatungo; igishishwa cy'igiti bakoramo inkanda."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umuvumu.",
                "Aho kiboneka: Afurika y'ubushyuhe n'ubut subtropical.",
                "Igishishwa cy'igiti bakoramo inkanda.",
                "Babazagamo imivure, amasekuru n'intebe.",
                "Amababi agaburira amatungo mu gihe cy'impeshi.",
                "Umuryango: Moraceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Moraceae",
                "Izina ry'ubumenyi: Ficus thonningii",
                "Izina ry'ikinyarwanda: Umuvumu",
                "Izina ry'icyongereza: Wild fig",
                "Aho kiboneka: Afurika y'ubushyuhe n'ubut subtropical",
                "Ikimenyetso: TREE-017"
        ));
        t.setDescription(
                "Ficus thonningii ni Umuvumu — igiti cy'umuryango wa Moraceae gikunze kuboneka mu Afurika y'ubushyuhe.\n\n" +
                "Igishishwa cy'igiti bagikoragamo inkanda. Babazagamo imivure (troughs), amasekuru (mortars) " +
                "ndetse n'intebe (stools). Amababi agaburira amatungo mu gihe cy'impeshi.\n\n" +
                "Mu buvuzi gakondo, bakoresha amababi n'imizi mu kuvura indwara zitandukanye.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umuvumu / wild fig (TREE-017) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Igishishwa cy'igiti bagikoragamo inkanda.\n" +
                "• Babazagamo imivure, amasekuru n'intebe.\n" +
                "• Amababi agaburira amatungo mu gihe cy'impeshi.\n\n" +
                "Ubuvuzi gakondo — icyo bavuza: amababi, imizi.\n" +
                "Indwara zivugwa: gucibwamo, mburugu, diabete, ibihushi, ibisebe, bronchiite, imuyoboro w'inkari."
        );
        t.setEcologicalImportance(
                "Umuvumu utanga imbuto (figs) n'ubuturo ku nyamaswa n'inyoni mu ishyamba n'ibishanga."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Inkanda, ibikoresho, ubuvuzi gakondo n'ibyo kugaburira amatungo.\n\n" +
                "Ku nyamaswa: Imbuto n'ubuturo mu gicucu."
        );
        t.setCommonAreas(
                "Mu Afurika y'ubushyuhe n'ubut subtropical, harimo n'u Rwanda — hafi y'amazu n'imisozi."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umuvumu (TREE-017) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umuvumu · Ficus thonningii · Wild fig · Moraceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Figuier sauvage");
        t.setShortDescription(
                "Ficus thonningii — figuier sauvage, appelé Umuvumu en kinyarwanda. Planté près des maisons rurales " +
                "et des sépultures royales ; écorce pour le tissu inkanda, feuilles pour le bétail."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umuvumu.",
                "Planté autour des maisons et des tombes royales.",
                "Écorce pour le tissu inkanda.",
                "Feuilles fourrage pour le bétail.",
                "Famille : Moraceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Moraceae",
                "Nom scientifique : Ficus thonningii",
                "Nom commun : Figuier sauvage",
                "Nom local : Umuvumu",
                "Hauteur : jusqu'à environ 20 m",
                "Identifiant parc : TREE-017"
        ));
        t.setDescription(
                "Ficus thonningii est un figuier sauvage africain. Au Rwanda : Umuvumu.\n\n" +
                "Usages : tissu inkanda, ustensiles, fourrage, médecine traditionnelle.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umuvumu selon les usages locaux."
        );
        t.setUses(
                "Plantation près des maisons et sépultures royales.\n\n" +
                "Artisanat : écorce pour inkanda ; tronc pour mortiers et auges.\n\n" +
                "Fourrage : feuilles pour le bétail en saison sèche.\n\n" +
                "Médecine : feuilles et figues pour diarrhée, gonorrhée et diabète."
        );
        t.setEcologicalImportance(
                "Fruits pour la faune ; pollinisation par les guêpes du figuier."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Tissu, artisanat, fourrage, médecine.\n\n" +
                "Pour la faune : Fruits et abri."
        );
        t.setCommonAreas(
                "Afrique tropicale et subtropicale, y compris le Rwanda."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umuvumu pour photos, carte et médias.\n\n" +
                "Umuvumu · Ficus thonningii · Figuier sauvage · Moraceae."
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
