package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Olea europaea subsp. africana (Umunzenze / Wild olive) — TREE-007.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class OleaEuropaeaSubspAfricanaData {

    public static final String SLUG = "olea-europaea-subsp-africana";
    public static final String SCIENTIFIC_NAME = "Olea europaea subsp. africana";
    public static final String QR_CODE_ID = "TREE-007";
    public static final String FAMILY = "Oleaceae (Olive family)";
    public static final String TYPICAL_HEIGHT = "5–10 m (up to 15 m)";
    public static final String ORIGIN = "Tropical and southern Africa, Mediterranean basin; native in Rwanda and East Africa";
    public static final String AGE_ESTIMATE = "Approx. 10–40 years (park specimen)";
    public static final double LATITUDE = -1.9688;
    public static final double LONGITUDE = 30.1076;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-007";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-007";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Olea_europaea_subsp._africana";

    private OleaEuropaeaSubspAfricanaData() {}

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
        tree.getCategories().addAll(List.of("MEDICINAL", "TIMBER", "SHADE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(7);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/3/36/Kirstenbosch_National_Botanical_Garden_%2852114552131%29.jpg",
                        "Olea europaea subsp. africana — wild olive at Kirstenbosch",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/1/11/Olea_europaea_africana_KirstenboshBotGard09292010A.JPG",
                        "African olive — Umunzenze foliage and branching",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/2/21/Olea_europaea_africana_KirstenboshBotGard09292010C.JPG",
                        "Wild olive tree habit and silvery-green leaves",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/6/6e/Olea_europaea_cuspidata-africana_-_Cape_Town.JPG",
                        "Olea europaea subsp. africana in natural African landscape",
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
        TreeTranslation t = base(tree, "en", "Wild olive");
        t.setShortDescription(
                "Olea europaea subsp. africana — wild olive or African olive, known in Kinyarwanda as Umunzenze. " +
                "A hardy native olive of Rwanda, valued for leaf tea, carving wood, and a wide range of " +
                "traditional medicinal uses."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umunzenze.",
                "Also called African olive or wild olive — related to the cultivated olive.",
                "Leaves are brewed as tea in local practice.",
                "Hard wood used for carving figures and traditional household items.",
                "Documented uses include eye infections, sore throat, headache and hypertension.",
                "Parts used: leaves, bark, roots, flowers and seeds."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Oleaceae (Olive family)",
                "Scientific name: Olea europaea subsp. africana (Mill.) P.S.Green",
                "Common names: Wild olive · African olive",
                "Local name (Kinyarwanda): Umunzenze",
                "Typical height: 5–10 m",
                "Distribution: Tropical Africa, South Africa, Mediterranean basin",
                "Parts used: Leaves, bark, roots, flowers, seeds",
                "Status in Rwanda: Native indigenous tree"
        ));
        t.setDescription(
                "Olea europaea subsp. africana is the wild or African olive — a small to medium evergreen " +
                "tree in the olive family. In Rwanda it is known as Umunzenze and grows naturally in " +
                "woodland and on hillsides across East Africa.\n\n" +
                "The tree has narrow silvery-green leaves, rough bark and small dark fruits. It is widely " +
                "recognised in ethnobotany for leaf tea, durable carving wood, and medicines prepared " +
                "from leaves, bark, roots, flowers and seeds.\n\n" +
                "Documented traditional uses include treatment of eye infections, urinary tract infections, " +
                "headaches, sore throat, as a diuretic and for hypertension.\n\n" +
                "At Kigali Eco-Park this species is presented as Umunzenze / wild olive, matching local " +
                "naming and documented uses."
        );
        t.setUses(
                "Leaf tea: Leaves brewed as tea in local practice.\n\n" +
                "Wood & crafts: Hard wood carved into figures, spoons and other traditional household items.\n\n" +
                "Traditional medicine: Leaf and fruit preparations used for eye complaints, blood pressure, " +
                "kidney function and sore throat; leaves also recorded for inflammation, diabetes and " +
                "hypertension in local ethnobotany.\n\n" +
                "Documented ailments: Eye infection, sore throat, headache, urinary tract infection, " +
                "diuretic and hypertension."
        );
        t.setEcologicalImportance(
                "Wild olive provides evergreen cover and food for birds and insects. Its hardy nature helps " +
                "stabilise slopes and it is often retained as a shade tree in farms and park landscapes " +
                "across tropical Africa."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Leaf tea, carving timber, traditional medicine and shade.\n\n" +
                "For wildlife: Fruits and cover for birds; nectar and habitat in woodland margins."
        );
        t.setCommonAreas(
                "Native across tropical Africa, South Africa and related Mediterranean regions. In Rwanda " +
                "found in natural woodland, hillsides and park plantings; tolerates dry rocky soils."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umunzenze label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: wild olive · African olive · Umunzenze · Oleaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umunzenze");
        t.setShortDescription(
                "Umunzenze (Olea europaea subsp. africana / Wild olive) — igiti cy'igihugu gifite " +
                "akamaro mu icyayi, ubukorikori n'ubuvuzi gakondo."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umunzenze.",
                "Ni olivyeri y'igihugu — ifite isura n'olive isanzwe.",
                "Amababi atekwamo icyayi.",
                "Igiti kibazwamo amashusho, imidaho n'ibindi bikoresho byo mu rugo.",
                "Igice cy'igiti gikoreshwa: amababi, igishihwa, imizi, indabyo n'imbuto.",
                "Bikoreshwa mu kuvura amaso, umuvuduko w'amaraso, impyiko n'ibyo mu mihogo."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Oleaceae",
                "Izina ry'ubumenyi: Olea europaea subsp. africana",
                "Izina ry'ikinyarwanda: Umunzenze",
                "Izina ry'icyongereza: Wild olive / African olive",
                "Uburebure: metero 5–10",
                "Igice gikoreshwa: Amababi, igishihwa, imizi, indabyo, imbuto",
                "Aho ukura: Afurika y'ubushyuhe, u Rwanda n'ibindi"
        ));
        t.setDescription(
                "Olea europaea subsp. africana ni Umunzenze — igiti cy'igihugu cy'umuryango wa Oleaceae. " +
                "Mu Rwanda cy'igihugu kandi cy'akamaro mu buzima busanzwe.\n\n" +
                "Ifite amababi y'icyatsi cyangwa n'ifeza, igishihwa gikomeye n'imbuto nto. Abantu bakoresha " +
                "amababi mu gukora icyayi, bakoresha igiti mu gukora amashusho n'ibikoresho byo mu rugo, " +
                "kandi bakoresha ibice by'igiti mu buvuzi gakondo.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umunzenze / wild olive hakurikijwe amazina " +
                "n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Bavuza: amababi, igishihwa, imizi, indabyo, imbuto.\n" +
                "• Amababi atekwamo icyayi.\n" +
                "• Igiti kibazwamo amashusho, imidaho n'ibindi bikoresho byo mu rugo.\n\n" +
                "Kuvura:\n" +
                "• Umuti ukozwe mu mababi n'imbuto uvura amaso, ukagabanya umuvuduko w'amaraso, " +
                "ugatuma impyiko zikora neza, ukavura no mu mihogo.\n" +
                "• Amababi y'umunzenze afite kurinda kanseri, kubyimbirwa, diabete n'umuvuduko w'amaraso."
        );
        t.setEcologicalImportance(
                "Umunzenze utanga igicucu n'imbuto z'inyoni. Ni igiti gikomeye gifasha kurinda ubutaka " +
                "ku misozi kandi ukunze ahantu h'ubutaka bukarabuye."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Icyayi, ubukorikori, ubuvuzi, igicucu.\n\n" +
                "Ku nyamaswa: Imbuto n'ahantu ho kwihisha inyoni."
        );
        t.setCommonAreas(
                "Uboneka mu Rwanda, Afurika y'ubushyuhe, Afurika y'Epfo n'akarere ka Mediterane. " +
                "Ukunda amasaka, imisozi n'ubutaka bukarabuye."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umunzenze kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umunzenze · Olea europaea subsp. africana · Wild olive · Oleaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Olivier sauvage");
        t.setShortDescription(
                "Olea europaea subsp. africana — olivier sauvage ou olivier d'Afrique, appelé Umunzenze " +
                "en kinyarwanda. Arbre indigène utilisé pour le thé de feuilles, la sculpture sur bois " +
                "et la médecine traditionnelle."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umunzenze.",
                "Olivier sauvage d'Afrique — parent de l'olivier cultivé.",
                "Feuilles infusées en thé.",
                "Bois dur pour sculpture et objets domestiques.",
                "Usages documentés : infections oculaires, maux de gorge, hypertension.",
                "Parties utilisées : feuilles, écorce, racines, fleurs, graines."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Oleaceae",
                "Nom scientifique : Olea europaea subsp. africana",
                "Noms communs : Olivier sauvage · Olivier d'Afrique",
                "Nom local : Umunzenze",
                "Hauteur : 5–10 m",
                "Répartition : Afrique tropicale, Afrique du Sud, bassin méditerranéen"
        ));
        t.setDescription(
                "Olea europaea subsp. africana est l'olivier sauvage d'Afrique — un petit arbre persistant " +
                "de la famille des Oléacées. Au Rwanda, on l'appelle Umunzenze.\n\n" +
                "Feuilles argentées, écorce rugueuse et petits fruits. Usages ethnobotaniques : thé, " +
                "sculpture sur bois et préparations médicinales.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umunzenze / olivier sauvage selon les usages locaux."
        );
        t.setUses(
                "Thé de feuilles : Infusion traditionnelle.\n\n" +
                "Bois et artisanat : Sculpture, cuillers et objets du quotidien.\n\n" +
                "Médecine traditionnelle : Feuilles et fruits pour les yeux, tension artérielle, reins " +
                "et maux de gorge ; feuilles aussi pour inflammation, diabète et hypertension.\n\n" +
                "Affections documentées : Infection oculaire, maux de gorge, céphalées, infections urinaires, " +
                "diurétique et hypertension."
        );
        t.setEcologicalImportance(
                "Couverture persistante, fruits pour les oiseaux et stabilisation des pentes en Afrique tropicale."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Thé, bois d'œuvre, médecine, ombrage.\n\n" +
                "Pour la faune : Fruits et abri pour les oiseaux."
        );
        t.setCommonAreas(
                "Indigène au Rwanda et en Afrique tropicale ; bois, collines et parcs ; sols secs et rocheux."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umunzenze pour photos, carte et médias.\n\n" +
                "Umunzenze · Olea europaea subsp. africana · Olivier sauvage · Oleaceae."
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
