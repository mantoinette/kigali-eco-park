package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Newtonia buchananii (Umukereko / forest newtonia) — TREE-019.
 * Content aligned with park ethnobotanical notes and Flora of Zimbabwe/Mozambique.
 */
public final class NewtoniaBuchananiiData {

    public static final String SLUG = "newtonia-buchananii";
    public static final String SCIENTIFIC_NAME = "Newtonia buchananii";
    public static final String QR_CODE_ID = "TREE-019";
    public static final String FAMILY = "Fabaceae (Mimosaceae)";
    public static final String TYPICAL_HEIGHT = "Large evergreen forest tree";
    public static final String ORIGIN = "Eastern and southern African forests";
    public static final String AGE_ESTIMATE = "Approx. 25–80 years (park specimen)";
    public static final double LATITUDE = -1.9678;
    public static final double LONGITUDE = 30.1108;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-019";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-019";
    public static final String REFERENCE_URL =
            "https://www.zimbabweflora.co.zw/speciesdata/species.php?species_id=126400";

    private NewtoniaBuchananiiData() {}

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
        tree.getCategories().addAll(List.of("TIMBER", "FIBRE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(19);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://www.zimbabweflora.co.zw/speciesdata/images/12/126400-1.jpg",
                        "Newtonia buchananii — forest newtonia (Umukereko)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://www.zimbabweflora.co.zw/speciesdata/images/12/126400-2.jpg",
                        "Newtonia buchananii foliage and habit",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://www.mozambiqueflora.com/speciesdata/images/12/126400-3.jpg",
                        "Newtonia buchananii in evergreen forest",
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
        TreeTranslation t = base(tree, "en", "Forest newtonia");
        t.setShortDescription(
                "Newtonia buchananii — forest newtonia or African newtonia, known in Kinyarwanda as Umukereko. " +
                "A large evergreen forest tree used for timber, agroforestry, livestock fodder and bee forage."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umukereko.",
                "Also called forest newtonia or African newtonia.",
                "Large evergreen tree, often near streams.",
                "Leaves and pods provide livestock fodder.",
                "Flowers supply nectar and pollen for bees.",
                "Family: Fabaceae (Mimosaceae)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae (Mimosaceae)",
                "Scientific name: Newtonia buchananii (Baker) G.C.C. Gilbert & Boutique",
                "Common name: Forest newtonia · African newtonia",
                "Local name (Kinyarwanda): Umukereko",
                "Typical height: large evergreen forest tree",
                "Distribution: eastern and southern Africa",
                "Park ID: TREE-019"
        ));
        t.setDescription(
                "Newtonia buchananii is a large evergreen forest tree of the family Fabaceae (subfamily Mimosoideae). " +
                "In Rwanda it is known as Umukereko. It grows in eastern and southern Africa, often in evergreen forest " +
                "near streams, and large trees may be buttressed at the base.\n\n" +
                "The wood is used as timber. In agroforestry it is mixed with crops; leaves and pods serve as livestock " +
                "fodder. The flowers are a good source of nectar and pollen for bees.\n\n" +
                "At Kigali Eco-Park this TREE-019 specimen is presented as Umukereko / forest newtonia, matching local " +
                "naming and documented uses."
        );
        t.setUses(
                "Timber: Valued forest wood for construction and carpentry.\n\n" +
                "Agroforestry: Grown with crops; leaves and pods used as livestock fodder.\n\n" +
                "Apiculture: Flowers provide nectar and pollen for bees."
        );
        t.setEcologicalImportance(
                "A canopy tree of moist evergreen forest. Flowers support bees and other pollinators; the crown " +
                "provides habitat structure along streams and forest edges."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Timber, agroforestry shade, livestock fodder and honey from bee forage.\n\n" +
                "For wildlife: Nectar, pollen, canopy cover and streamside forest habitat."
        );
        t.setCommonAreas(
                "Native to eastern and southern Africa — Angola, Cameroon, DRC, Uganda, Kenya, Tanzania, Malawi, " +
                "Mozambique, Zambia, Zimbabwe and Rwanda — typically in evergreen forest near streams."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umukereko (TREE-019) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: forest newtonia · African newtonia · Umukereko · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umukereko");
        t.setShortDescription(
                "Umukereko (Newtonia buchananii / forest newtonia) — igiti kinini cy'ishyamba. " +
                "Imbaho, kivangwa n'imyaka, kugaburira amatungo; inzuki zirahova."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umukereko.",
                "Imbaho zo kubaka no kubaza.",
                "Kivangwa n'imyaka mu buhinzi.",
                "Amababi n'imbuto bigaburira amatungo.",
                "Inzuki zirahova ku mashurwe.",
                "Umuryango: Fabaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae (Mimosaceae)",
                "Izina ry'ubumenyi: Newtonia buchananii",
                "Izina ry'ikinyarwanda: Umukereko",
                "Izina ry'icyongereza: Forest newtonia",
                "Aho kiboneka: Afurika y'Iburasirazuba n'Amajyepfo",
                "Ikimenyetso: TREE-019"
        ));
        t.setDescription(
                "Newtonia buchananii ni Umukereko — igiti kinini cy'ishyamba cy'umuryango wa Fabaceae. " +
                "Gikunze gukura mu ishyamba gihora kibisi, cyane hafi y'imigezi.\n\n" +
                "Imbaho zacyo zikoreshwa mu kubaka. Gishobora kuvangwa n'imyaka. Amababi n'imbuto bigaburira " +
                "amatungo. Amashurwe atuma inzuki zirahova.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umukereko / forest newtonia (TREE-019) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Imbaho.\n" +
                "• Kivangwa n'imyaka.\n" +
                "• Kugaburira amatungo.\n" +
                "• Inzuki zirahova."
        );
        t.setEcologicalImportance(
                "Umukereko utanga amashurwe ku nzuki n'ubuturo mu ishyamba hafi y'amazi."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Imbaho, ubuhinzi, ibyo kugaburira amatungo n'ubuki.\n\n" +
                "Ku nyamaswa: Amashurwe, ubuturo n'igicucu."
        );
        t.setCommonAreas(
                "Mu Afurika y'Iburasirazuba n'Amajyepfo, harimo n'u Rwanda — mu ishyamba hafi y'imigezi."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umukereko (TREE-019) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umukereko · Newtonia buchananii · Forest newtonia · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Newtonia des forêts");
        t.setShortDescription(
                "Newtonia buchananii — newtonia des forêts, appelé Umukereko en kinyarwanda. " +
                "Grand arbre forestier : bois d'œuvre, agroforesterie, fourrage et nectar pour les abeilles."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umukereko.",
                "Bois d'œuvre.",
                "Agroforesterie et fourrage.",
                "Nectar et pollen pour les abeilles.",
                "Famille : Fabaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae (Mimosaceae)",
                "Nom scientifique : Newtonia buchananii",
                "Nom commun : Newtonia des forêts",
                "Nom local : Umukereko",
                "Répartition : Afrique orientale et australe",
                "Identifiant parc : TREE-019"
        ));
        t.setDescription(
                "Newtonia buchananii est un grand arbre des forêts sempervirentes. Au Rwanda : Umukereko.\n\n" +
                "Usages : bois, cultures associées, fourrage, apiculture.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umukereko selon les usages locaux."
        );
        t.setUses(
                "Bois d'œuvre.\n\n" +
                "Agroforesterie : associé aux cultures ; feuilles et gousses pour le bétail.\n\n" +
                "Apiculture : nectar et pollen."
        );
        t.setEcologicalImportance(
                "Arbre de canopée des forêts humides ; fleurs pour les abeilles ; habitat le long des cours d'eau."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Bois, fourrage, miel.\n\n" +
                "Pour la faune : Nectar, pollen et couvert forestier."
        );
        t.setCommonAreas(
                "Afrique orientale et australe, y compris le Rwanda — forêts sempervirentes près des cours d'eau."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umukereko pour photos, carte et médias.\n\n" +
                "Umukereko · Newtonia buchananii · Newtonia des forêts · Fabaceae."
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
