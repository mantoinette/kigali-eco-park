package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Phoenix reclinata (Umukindo / wild date palm) — TREE-015.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class PhoenixReclinataData {

    public static final String SLUG = "phoenix-reclinata";
    public static final String SCIENTIFIC_NAME = "Phoenix reclinata";
    public static final String QR_CODE_ID = "TREE-015";
    public static final String FAMILY = "Arecaceae (Palm family)";
    public static final String TYPICAL_HEIGHT = "Clustering palm to about 10 m";
    public static final String ORIGIN = "Tropical and southern Africa — common in Rwanda";
    public static final String AGE_ESTIMATE = "Approx. 15–50 years (park specimen)";
    public static final double LATITUDE = -1.9670;
    public static final double LONGITUDE = 30.1100;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-015";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-015";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Phoenix_reclinata";

    private PhoenixReclinataData() {}

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
        tree.getCategories().addAll(List.of("FRUIT", "FIBRE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(15);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/e/ef/Phoenix-reclinata-AK.jpg",
                        "Phoenix reclinata — wild date palm (Umukindo)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/d/de/Phoenixreclinatafruit.JPG",
                        "Wild date palm fruit — edible when ripe",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/0/0a/Reclinatabloom.JPG",
                        "Phoenix reclinata inflorescence",
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
        TreeTranslation t = base(tree, "en", "Wild date palm");
        t.setShortDescription(
                "Phoenix reclinata — the wild date palm or Senegal date palm, known in Kinyarwanda as Umukindo. " +
                "A clustering African palm with edible fruit, palm-wine sap, and leaf fibre for mats and brooms."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umukindo.",
                "Also called Senegal date palm or wild date palm.",
                "Fruit are edible when ripe.",
                "Sap can be tapped for palm wine.",
                "Young unopened leaf fibre used for carpets, kilts and brooms.",
                "Family: Arecaceae (palm family)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Arecaceae (Palm family)",
                "Scientific name: Phoenix reclinata Jacq.",
                "Common name: Wild date palm · Senegal date palm",
                "Local name (Kinyarwanda): Umukindo",
                "Typical height: clustering palm to about 10 m",
                "Status in Rwanda: Native across tropical Africa",
                "Park ID: TREE-015"
        ));
        t.setDescription(
                "Phoenix reclinata is the wild date palm — a native African palm of the family Arecaceae. " +
                "In Rwanda it is known as Umukindo. It often grows as a clustering palm along rivers, wetlands " +
                "and moist valleys.\n\n" +
                "The fruit are edible. Sap from the trunk can be fermented into palm wine. The fibre of young " +
                "unopened leaves is traditionally used to make carpets, kilts and brooms.\n\n" +
                "At Kigali Eco-Park this TREE-015 specimen is presented as Umukindo / wild date palm, matching " +
                "local naming and documented uses."
        );
        t.setUses(
                "Food: The fruit are edible.\n\n" +
                "Beverage: Sap gives palm wine.\n\n" +
                "Craft: Fibre from young unopened leaves is used for carpets, kilts and brooms."
        );
        t.setEcologicalImportance(
                "Wild date palm provides fruit and cover along riverbanks and wetlands, supporting birds and " +
                "other wildlife in moist habitats."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Edible fruit, palm wine, mats, kilts and brooms.\n\n" +
                "For wildlife: Fruit and sheltered roosting sites along watercourses."
        );
        t.setCommonAreas(
                "Widespread in tropical and southern Africa, including Rwanda — often near rivers, swamps and valleys."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umukindo (TREE-015) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: wild date palm · Senegal date palm · Umukindo · Arecaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umukindo");
        t.setShortDescription(
                "Umukindo (Phoenix reclinata / wild date palm) — igisabo cy'umuryango wa Arecaceae. " +
                "Imbuto ziraribwa; amababi akiri mato bakoresha mu gukora imikeka n'imikubuzo."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umukindo.",
                "Imbuto ziraribwa.",
                "Umuti wo mu mababi ushobora gukoreshwa mu gukora inzoga y'igisabo.",
                "Amababi akiri mato bayakoramo imikeka n'imikubuzo.",
                "Ku munsi wa mashami (Kiriziya Gatorika) bakoresha amababi y'umukindo akiri mato.",
                "Umuryango: Arecaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Arecaceae",
                "Izina ry'ubumenyi: Phoenix reclinata",
                "Izina ry'ikinyarwanda: Umukindo",
                "Izina ry'icyongereza: Wild date palm",
                "Uburebure: igisabo gishobora kugeza metero 10",
                "Ikimenyetso: TREE-015"
        ));
        t.setDescription(
                "Phoenix reclinata ni Umukindo — igisabo cy'umuryango wa Arecaceae gikunze gukura hafi y'imigezi " +
                "n'ibishanga.\n\n" +
                "Imbuto ziraribwa. Umuti wo mu mababi ushobora gukoreshwa mu gukora inzoga y'igisabo. " +
                "Amababi akiri mato bayakoramo imikeka n'imikubuzo.\n\n" +
                "Ku munsi wa mashami (Kiriziya Gatorika) bakoresha amababi y'umukindo akiri mato.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umukindo / wild date palm (TREE-015) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Imbuto ziraribwa.\n" +
                "• Amababi akiri mato bayakoramo imikeka, imikubuzo.\n" +
                "• Ku munsi wa mashami (Kiriziya Gatorika) bakoresha amababi y'umukindo akiri mato."
        );
        t.setEcologicalImportance(
                "Umukindo utanga imbuto n'ahantu heza ku nyamaswa zikunze kuba hafi y'amazi."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Imbuto, imikeka, imikubuzo n'inzoga y'igisabo.\n\n" +
                "Ku nyamaswa: Imbuto n'ubuturo hafi y'imigezi."
        );
        t.setCommonAreas(
                "Gikunze mu Rwanda no mu Afurika y'ubushyuhe — cyane hafi y'imigezi n'ibishanga."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umukindo (TREE-015) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umukindo · Phoenix reclinata · Wild date palm · Arecaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Palmier-dattier sauvage");
        t.setShortDescription(
                "Phoenix reclinata — palmier-dattier sauvage ou palmier dattier du Sénégal, appelé Umukindo en kinyarwanda. " +
                "Fruits comestibles, vin de palme et fibres de jeunes feuilles pour nattes et balais."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umukindo.",
                "Fruits comestibles.",
                "Sève pour le vin de palme.",
                "Fibres de jeunes feuilles pour nattes et balais.",
                "Famille : Arecaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Arecaceae",
                "Nom scientifique : Phoenix reclinata",
                "Nom commun : Palmier-dattier sauvage",
                "Nom local : Umukindo",
                "Hauteur : palmier touffu jusqu'à environ 10 m",
                "Identifiant parc : TREE-015"
        ));
        t.setDescription(
                "Phoenix reclinata est le palmier-dattier sauvage. Au Rwanda : Umukindo.\n\n" +
                "Usages : fruits comestibles, vin de palme, nattes et balais à partir de jeunes feuilles.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umukindo selon les usages locaux."
        );
        t.setUses(
                "Fruits comestibles.\n\n" +
                "Vin de palme à partir de la sève.\n\n" +
                "Artisanat : nattes, kilts et balais à partir de jeunes feuilles."
        );
        t.setEcologicalImportance(
                "Fruits et couvert le long des cours d'eau ; habitat pour la faune des zones humides."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Fruits, vin de palme, artisanat.\n\n" +
                "Pour la faune : Fruits et abris le long des rivières."
        );
        t.setCommonAreas(
                "Afrique tropicale et australe, y compris le Rwanda — rivières et zones humides."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umukindo pour photos, carte et médias.\n\n" +
                "Umukindo · Phoenix reclinata · Palmier-dattier sauvage · Arecaceae."
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
