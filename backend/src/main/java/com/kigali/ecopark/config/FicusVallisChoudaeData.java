package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Ficus vallis-choudae (Ikidoboli / false cape fig / Haroni fig) — TREE-022.
 * Content aligned with park ethnobotanical notes and Flora of Rwanda.
 */
public final class FicusVallisChoudaeData {

    public static final String SLUG = "ficus-vallis-choudae";
    public static final String SCIENTIFIC_NAME = "Ficus vallis-choudae";
    public static final String QR_CODE_ID = "TREE-022";
    public static final String FAMILY = "Moraceae (Fig / mulberry family)";
    public static final String TYPICAL_HEIGHT = "Up to about 20 m";
    public static final String ORIGIN = "Tropical Africa — native in Rwanda and the wider region";
    public static final String AGE_ESTIMATE = "Approx. 10–40 years (park specimen)";
    public static final double LATITUDE = -1.9684;
    public static final double LONGITUDE = 30.1114;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-022";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-022";
    public static final String REFERENCE_URL =
            "https://www.rwandaflora.com/speciesdata/species.php?species_id=120440";

    private FicusVallisChoudaeData() {}

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
        tree.getCategories().addAll(List.of("FOOD", "TIMBER", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(22);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=1280&h=720&q=80",
                        "Ikidoboli — African fig tree habitat",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://images.unsplash.com/photo-1511497584788-876760111969?auto=format&fit=crop&w=1280&h=720&q=80",
                        "Ikidoboli — woodland canopy",
                        false,
                        2
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
        TreeTranslation t = base(tree, "en", "False cape fig");
        t.setShortDescription(
                "Ficus vallis-choudae — false cape fig or Haroni fig, known in Kinyarwanda as Ikidoboli. " +
                "A native African fig of the Moraceae family; figs are edible and the bole is used as timber."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Ikidoboli.",
                "Also called false cape fig or Haroni fig.",
                "Family: Moraceae.",
                "Figs are edible for children and wildlife.",
                "The bole is used as timber.",
                "Recorded in the Flora of Rwanda."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Moraceae",
                "Scientific name: Ficus vallis-choudae Delile",
                "Common name: False cape fig / Haroni fig",
                "Local name (Kinyarwanda): Ikidoboli",
                "Typical height: up to about 20 m",
                "Park ID: TREE-022"
        ));
        t.setDescription(
                "Ficus vallis-choudae is a terrestrial fig tree of tropical Africa, including Rwanda. " +
                "In Kinyarwanda it is known as Ikidoboli; in English as false cape fig or Haroni fig.\n\n" +
                "Leaves are ovate to broadly ovate with conspicuous cream-green venation. Figs grow in leaf " +
                "axils and turn yellow to orange when ripe.\n\n" +
                "At Kigali Eco-Park this TREE-022 specimen is presented as Ikidoboli, matching local naming " +
                "and documented uses."
        );
        t.setUses(
                "Food: Figs are edible for children.\n\n" +
                "Timber: The bole is used as timber.\n\n" +
                "Wildlife: Fruits feed birds and animals."
        );
        t.setEcologicalImportance(
                "A riverine and forest-edge fig that supports frugivores and provides canopy structure in " +
                "native woodland and swamp-forest habitats."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Edible figs and timber from the bole.\n\n" +
                "For wildlife: Fruit and habitat for birds and mammals."
        );
        t.setCommonAreas(
                "Riverine and swamp forest, and occasional evergreen forest across tropical Africa, " +
                "including Rwanda."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Ikidoboli (TREE-022) label for photos, map location, " +
                "and multilingual audio/video.\n\n" +
                "Also known as: Ikidoboli · false cape fig · Haroni fig · Ficus vallis-choudae · Moraceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Ikidoboli");
        t.setShortDescription(
                "Ikidoboli (Ficus vallis-choudae) — igiti cy'umuryango wa Moraceae. " +
                "Imbuto zacyo ziribwa n'inyoni n'inyamaswa ndetse n'abantu; igiti biracyubakisha."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Ikidoboli.",
                "Izina ry'ubumenyi: Ficus vallis-choudae.",
                "Umuryango: Moraceae.",
                "Imbuto ziribwa n'abana n'inyamaswa.",
                "Igiti gikoreshwa mu byubatsi."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Moraceae",
                "Izina ry'ubumenyi: Ficus vallis-choudae",
                "Izina ry'ikinyarwanda: Ikidoboli",
                "Ikimenyetso: TREE-022"
        ));
        t.setDescription(
                "Ficus vallis-choudae ni Ikidoboli — igiti cy'umuryango wa Moraceae kiboneka mu Rwanda " +
                "n'ahandi mu Afurika.\n\n" +
                "Akamaro: Imbuto zacyo ziribwa n'inyoni n'inyamaswa ndetse n'abantu. Igiti biracyubakisha.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Ikidoboli (TREE-022)."
        );
        t.setUses(
                "Ibiryo: Imbuto ziribwa n'abana.\n\n" +
                "Ibyubatsi: Igiti gikoreshwa mu byubatsi.\n\n" +
                "Inyamaswa: Imbuto zigaburira inyoni n'inyamaswa."
        );
        t.setEcologicalImportance(
                "Ikidoboli gifasha inyoni n'inyamaswa ziriya imbuto; gitanga urugara mu mashyamba " +
                "n'imigezi."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Imbuto n'ibiti byo kubaka.\n\n" +
                "Ku nyamaswa: Imbuto n'ubuturo."
        );
        t.setCommonAreas(
                "Mu mashyamba y'imigezi n'ibiyaga by'u Rwanda n'ibihugu baturanye."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Ikidoboli (TREE-022).\n\n" +
                "Ikidoboli · Ficus vallis-choudae · Moraceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Figuier de Haroni");
        t.setShortDescription(
                "Ficus vallis-choudae — figuier de Haroni ou false cape fig, appelé Ikidoboli en kinyarwanda. " +
                "Figues comestibles; tronc utilisé comme bois d'œuvre."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Ikidoboli.",
                "Famille : Moraceae.",
                "Figues comestibles.",
                "Bois d'œuvre.",
                "Présent au Rwanda."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Moraceae",
                "Nom scientifique : Ficus vallis-choudae",
                "Nom local : Ikidoboli",
                "Identifiant parc : TREE-022"
        ));
        t.setDescription(
                "Ficus vallis-choudae est un figuier d'Afrique tropicale, présent au Rwanda. " +
                "En kinyarwanda : Ikidoboli.\n\n" +
                "Usages : figues comestibles pour les enfants ; tronc utilisé comme bois d'œuvre.\n\n" +
                "Au Kigali Eco-Park, cette fiche TREE-022 présente Ikidoboli."
        );
        t.setUses("Figues comestibles. Bois d'œuvre. Fruits pour la faune.");
        t.setEcologicalImportance("Figuier de ripisylve et de lisière forestière, important pour les frugivores.");
        t.setBenefitsToPeopleAndWildlife("Pour les populations : fruits et bois. Pour la faune : figues et ombrage.");
        t.setCommonAreas("Forêts riveraines et marécageuses d'Afrique tropicale, y compris le Rwanda.");
        t.setAdditionalInfo("Référence : " + REFERENCE_URL);
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
