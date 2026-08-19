package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Blighia unijugata (Umuturamugina / triangle-top) — TREE-020.
 * Content aligned with park ethnobotanical notes and Flora of Zimbabwe/Mozambique.
 */
public final class BlighiaUnijugataData {

    public static final String SLUG = "blighia-unijugata";
    public static final String SCIENTIFIC_NAME = "Blighia unijugata";
    public static final String QR_CODE_ID = "TREE-020";
    public static final String FAMILY = "Sapindaceae (Soapberry family)";
    public static final String TYPICAL_HEIGHT = "Shrub or small forest tree";
    public static final String ORIGIN = "Tropical Africa, south to South Africa";
    public static final String AGE_ESTIMATE = "Approx. 15–40 years (park specimen)";
    public static final double LATITUDE = -1.9680;
    public static final double LONGITUDE = 30.1110;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-020";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-020";
    public static final String REFERENCE_URL =
            "https://www.zimbabweflora.co.zw/speciesdata/species.php?species_id=137480";

    private BlighiaUnijugataData() {}

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
        tree.getCategories().addAll(List.of("MEDICINAL", "TIMBER", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(20);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/f/f8/Blighia-unijugata-JWGw1367_1-CS.jpg",
                        "Blighia unijugata — triangle-top (Umuturamugina)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/b/b3/Blighia_unijugata_JWGw1367_1_LR2.jpg",
                        "Blighia unijugata leaves",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://www.zimbabweflora.co.zw/speciesdata/images/13/137480-1.jpg",
                        "Blighia unijugata habit (Flora of Zimbabwe)",
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
        TreeTranslation t = base(tree, "en", "Triangle-top");
        t.setShortDescription(
                "Blighia unijugata — triangle-top, known in Kinyarwanda as Umuturamugina. Widespread in tropical " +
                "Africa; used in traditional medicine and for light construction timber."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umuturamugina.",
                "Also called triangle-top or triangle tops.",
                "Fruit is three-lobed, pink to red when ripe.",
                "Leaves, bark and roots used in traditional medicine.",
                "Wood used in light construction.",
                "Family: Sapindaceae (soapberry family)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Sapindaceae (Soapberry family)",
                "Scientific name: Blighia unijugata Baker",
                "Common name: Triangle-top",
                "Local name (Kinyarwanda): Umuturamugina",
                "Typical height: shrub or small forest tree",
                "Distribution: tropical Africa, south to South Africa",
                "Park ID: TREE-020"
        ));
        t.setDescription(
                "Blighia unijugata is a shrub or small tree of the family Sapindaceae. In Rwanda it is known as " +
                "Umuturamugina. It is widespread in tropical Africa and also occurs in South Africa, in riverine " +
                "thickets, woodland and evergreen forest.\n\n" +
                "In traditional medicine it is used for rheumatism, kidney pain and stiffness of the joints. Leaves, " +
                "bark and roots are also recorded as having oxytocic action in childbirth. The wood is used in light " +
                "construction.\n\n" +
                "At Kigali Eco-Park this TREE-020 specimen is presented as Umuturamugina / triangle-top, matching " +
                "local naming and documented uses."
        );
        t.setUses(
                "Traditional medicine: Treatment of rheumatism, kidney pain and stiffness; various parts have oxytocic " +
                "action in childbirth.\n\n" +
                "Timber: Wood used in light construction."
        );
        t.setEcologicalImportance(
                "Grows in riverine thickets, woodland and evergreen forest, often near termite mounds. Flowers and " +
                "fruit support insects and birds."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Traditional medicine and light construction timber.\n\n" +
                "For wildlife: Flowers, fruit and understorey cover."
        );
        t.setCommonAreas(
                "Widespread in tropical Africa, including Rwanda, southwards to Zimbabwe, Mozambique and coastal " +
                "KwaZulu-Natal, South Africa."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umuturamugina (TREE-020) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: triangle-top · Umuturamugina · Sapindaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umuturamugina");
        t.setShortDescription(
                "Umuturamugina (Blighia unijugata / triangle-top) — igiti kiba mu Rwanda hose no muri Afurika " +
                "munsi y'ubutayu. Mu buvuzi gakondo bakivuza rubagimpande n'ububabare bw'impyiko."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umuturamugina.",
                "Kiba mu Rwanda hose no muri Afurika munsi y'ubutayu.",
                "Mu buvuzi gakondo bakivuza rubagimpande.",
                "Bivura ububabare bw'impyiko n'ingingo.",
                "Imbaho zikoreshwa mu kubaka ibyoroshye.",
                "Umuryango: Sapindaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Sapindaceae",
                "Izina ry'ubumenyi: Blighia unijugata",
                "Izina ry'ikinyarwanda: Umuturamugina",
                "Izina ry'icyongereza: Triangle-top",
                "Aho kiboneka: Afurika y'ubushyuhe kugeza muri Afurika y'Amajyepfo",
                "Ikimenyetso: TREE-020"
        ));
        t.setDescription(
                "Blighia unijugata ni Umuturamugina — igiti cy'umuryango wa Sapindaceae. Ni igiti kiba mu Rwanda " +
                "hose, no muri Afurika munsi y'ubutayu.\n\n" +
                "Mu buvuzi gakondo bakivuza rubagimpande, ububabare bw'impyiko, no kumugara kw'ingingo zinyuranye " +
                "z'umubiri. Mu mababi, igishishwa ndetse no mu mizi yacyo harimo imiti ikoreshwa mu kabyaza abagore.\n\n" +
                "Imbaho zikoreshwa mu kubaka ibyoroshye.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umuturamugina / triangle-top (TREE-020) hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Imbaho zo kubaka ibyoroshye.\n\n" +
                "Ubuvuzi gakondo:\n" +
                "• Rubagimpande, ububabare bw'impyiko, no kumugara kw'ingingo.\n" +
                "• Amababi, igishishwa n'imizi bikoreshwa mu kabyaza abagore."
        );
        t.setEcologicalImportance(
                "Umuturamugina ukura hafi y'imigezi, mu ishyamba n'ibishanga; utanga imbuto n'amashurwe ku nyamaswa."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Ubuvuzi gakondo n'imbaho zo kubaka ibyoroshye.\n\n" +
                "Ku nyamaswa: Amashurwe, imbuto n'ubuturo."
        );
        t.setCommonAreas(
                "Mu Rwanda hose no muri Afurika munsi y'ubutayu, kugeza muri Afurika y'Amajyepfo."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umuturamugina (TREE-020) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umuturamugina · Blighia unijugata · Triangle-top · Sapindaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Blighia à deux folioles");
        t.setShortDescription(
                "Blighia unijugata — appelé Umuturamugina en kinyarwanda. Répandu en Afrique tropicale ; " +
                "médecine traditionnelle et bois de construction légère."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umuturamugina.",
                "Rhumatisme et douleurs rénales en médecine traditionnelle.",
                "Bois pour constructions légères.",
                "Famille : Sapindaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Sapindaceae",
                "Nom scientifique : Blighia unijugata",
                "Nom commun : Triangle-top",
                "Nom local : Umuturamugina",
                "Identifiant parc : TREE-020"
        ));
        t.setDescription(
                "Blighia unijugata est un arbuste ou petit arbre. Au Rwanda : Umuturamugina.\n\n" +
                "Usages : médecine traditionnelle et bois de construction légère.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umuturamugina selon les usages locaux."
        );
        t.setUses(
                "Médecine traditionnelle : rhumatisme, douleurs rénales, raideur articulaire ; action oxytocique.\n\n" +
                "Bois de construction légère."
        );
        t.setEcologicalImportance(
                "Forêts riveraines, fourrés et forêts sempervirentes ; fruits et fleurs pour la faune."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Médecine traditionnelle et bois.\n\n" +
                "Pour la faune : Fruits et abri."
        );
        t.setCommonAreas(
                "Afrique tropicale, y compris le Rwanda, jusqu'à l'Afrique du Sud."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umuturamugina pour photos, carte et médias.\n\n" +
                "Umuturamugina · Blighia unijugata · Triangle-top · Sapindaceae."
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
