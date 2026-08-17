package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Elaeis guineensis (Ikigazi / African oil palm) — TREE-014.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class ElaeisGuineensisData {

    public static final String SLUG = "elaeis-guineensis";
    public static final String SCIENTIFIC_NAME = "Elaeis guineensis";
    public static final String QR_CODE_ID = "TREE-014";
    public static final String FAMILY = "Arecaceae (Palm family)";
    public static final String TYPICAL_HEIGHT = "Up to 20 m; leaves to 5 m";
    public static final String ORIGIN = "West and Central Africa — widely cultivated in tropical Africa including Rwanda";
    public static final String AGE_ESTIMATE = "Approx. 10–40 years (park specimen)";
    public static final double LATITUDE = -1.9668;
    public static final double LONGITUDE = 30.1096;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-014";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-014";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Elaeis_guineensis";

    private ElaeisGuineensisData() {}

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
        tree.getCategories().addAll(List.of("FRUIT", "FIBRE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(14);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/f/ff/Elaeis_guineensis_-_African_Oil_Palm_tree_with_fruit.jpg",
                        "Elaeis guineensis — African oil palm with fruit (Ikigazi)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/a/aa/Oilpalm.JPG",
                        "African oil palm habit",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/9/9f/Elaeis_guineensis_fruits_on_tree.jpg",
                        "Oil palm fruit bunches on the tree",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/1/18/Fresh_oil_palm_fruit.jpg",
                        "Fresh oil palm fruit — source of palm oil and kernel oil",
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
        TreeTranslation t = base(tree, "en", "African oil palm");
        t.setShortDescription(
                "Elaeis guineensis — African oil palm, known in Kinyarwanda as Ikigazi. A tall palm that produces " +
                "palm oil for cooking, cosmetics and soap; leaves and fibres are used for thatch, baskets, brooms and ropes."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Ikigazi.",
                "Can reach about 20 m; fronds (leaves) up to about 5 m.",
                "Pollinated by the weevil Elaeidobius kamerunicus, which helps fruit set.",
                "Fruit pulp yields palm oil; the inner seed yields palm kernel oil.",
                "Family: Arecaceae (palm family)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Arecaceae (Palm family)",
                "Scientific name: Elaeis guineensis Jacq.",
                "Common name: African oil palm · Oil palm",
                "Local name (Kinyarwanda): Ikigazi",
                "Typical height: up to 20 m; leaves to 5 m",
                "Pollinator: Elaeidobius kamerunicus",
                "Status in Rwanda: Cultivated (West African origin)",
                "Park ID: TREE-014"
        ));
        t.setDescription(
                "Elaeis guineensis is the African oil palm — a tall palm of the family Arecaceae. In Rwanda it is " +
                "known as Ikigazi. Trees may reach about 20 metres, with fronds up to about 5 metres long.\n\n" +
                "Fruit set is helped by a small weevil, Elaeidobius kamerunicus. The fleshy fruit produces palm oil " +
                "used for cooking in Africa, and also in cosmetics, soap and biofuels. The inner seed yields palm " +
                "kernel oil. Leaves and fibres are used for traditional thatch, baskets, brooms and ropes.\n\n" +
                "At Kigali Eco-Park this TREE-014 specimen is presented as Ikigazi / African oil palm, matching local naming " +
                "and documented uses."
        );
        t.setUses(
                "Edible oil: Palm oil from the fruit pulp is used for cooking food in Africa.\n\n" +
                "Industry: Cosmetics, soap (amasabune) and biofuels.\n\n" +
                "Kernel oil: The inner seed yields palm kernel oil.\n\n" +
                "Craft: Leaves and fibres for traditional thatch, baskets (inkangara), brooms (imikubuzo) and ropes."
        );
        t.setEcologicalImportance(
                "Oil palm provides nectar and pollen for its specialised pollinator and habitat structure in " +
                "cultivated landscapes. Responsible planting matters because large plantations can replace natural forest."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Cooking oil, cosmetics, soap, thatch, baskets, brooms and ropes.\n\n" +
                "For wildlife: Flowers support the oil-palm pollinator weevil and other insects."
        );
        t.setCommonAreas(
                "Native to West and Central African rainforests; widely planted across tropical Africa, including Rwanda."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Ikigazi (TREE-014) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: African oil palm · oil palm · Ikigazi · Arecaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Ikigazi");
        t.setShortDescription(
                "Ikigazi (Elaeis guineensis / African oil palm) — ikigazi gishobora kugeza mu burebure bwa " +
                "metero 20; amababi ageza ku burebure bwa metero 5."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Ikigazi.",
                "Gishobora kugeza mu burebure bwa metero 20.",
                "Amababi ageza ku burebure bwa metero 5.",
                "Gifite agakoko gatuma kizana imbuto: Elaeidobius kamerunicus.",
                "Amavuta y'amamesa akoreshwa mu guteka, cosmetics n'amasabune."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Arecaceae",
                "Izina ry'ubumenyi: Elaeis guineensis",
                "Izina ry'ikinyarwanda: Ikigazi",
                "Izina ry'icyongereza: African oil palm",
                "Uburebure: kugeza metero 20; amababi metero 5",
                "Agakoko: Elaeidobius kamerunicus",
                "Ikimenyetso: TREE-014"
        ));
        t.setDescription(
                "Elaeis guineensis ni Ikigazi — ikigazi cy'umuryango wa Arecaceae. Gishobora kugeza mu burebure " +
                "bwa metero 20. Amababi ageza ku burebure bwa metero 5.\n\n" +
                "Ikigazi gifite agakoko gatuma kizana imbuto kitwa Elaeidobius kamerunicus.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Ikigazi / African oil palm (TREE-014) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Amavuta y'amamesa akoreshwa mu guteka ibiryo, cosmetics no mu gukora amasabune.\n" +
                "• Mu mababi bakoramo inkangara, imikubuzo, n'ibindi.\n" +
                "• Imbuto z'imbere zitanga palm kernel oil."
        );
        t.setEcologicalImportance(
                "Amashurwe y'ikigazi afasha agakoko Elaeidobius kamerunicus n'ibindi binyabuzima."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Amavuta yo guteka, amasabune, inkangara n'imikubuzo.\n\n" +
                "Ku nyamaswa: Amashurwe n'ahantu ku dukoko."
        );
        t.setCommonAreas(
                "Cyavuye muri Afurika y'Uburengerazuba n'hagati; gihingwa cyane mu Rwanda n'Afurika y'ubushyuhe."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Ikigazi (TREE-014) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Ikigazi · Elaeis guineensis · African oil palm · Arecaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Palmier à huile d'Afrique");
        t.setShortDescription(
                "Elaeis guineensis — palmier à huile d'Afrique, appelé Ikigazi en kinyarwanda. Jusqu'à 20 m ; " +
                "huile de palme pour la cuisine, cosmétique et savon ; feuilles pour nattes, paniers et balais."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Ikigazi.",
                "Hauteur jusqu'à 20 m ; feuilles jusqu'à 5 m.",
                "Pollinisateur : Elaeidobius kamerunicus.",
                "Huile de palme et huile de palmiste.",
                "Famille : Arecaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Arecaceae",
                "Nom scientifique : Elaeis guineensis",
                "Nom commun : Palmier à huile d'Afrique",
                "Nom local : Ikigazi",
                "Hauteur : jusqu'à 20 m",
                "Identifiant parc : TREE-014"
        ));
        t.setDescription(
                "Elaeis guineensis est le palmier à huile d'Afrique. Au Rwanda : Ikigazi.\n\n" +
                "Usages : huile de cuisine, cosmétique, savon, biocarburants ; feuilles pour toitures, paniers et balais.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Ikigazi selon les usages locaux."
        );
        t.setUses(
                "Huile alimentaire pour la cuisine en Afrique.\n\n" +
                "Cosmétique, savon et biocarburants.\n\n" +
                "Huile de palmiste (graine).\n\n" +
                "Artisanat : chaume, paniers, balais et cordes."
        );
        t.setEcologicalImportance(
                "Fleurs pour le charançon pollinisateur ; les grandes plantations doivent rester responsables."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Huile, savon, artisanat.\n\n" +
                "Pour la faune : Pollinisateurs."
        );
        t.setCommonAreas(
                "Originaire d'Afrique de l'Ouest et centrale ; cultivé au Rwanda."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Ikigazi pour photos, carte et médias.\n\n" +
                "Ikigazi · Elaeis guineensis · Palmier à huile · Arecaceae."
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
