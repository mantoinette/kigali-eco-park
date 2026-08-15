package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Maesa lanceolata (Umuhanga / False assegai) — TREE-011.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class MaesaLanceolataData {

    public static final String SLUG = "maesa-lanceolata";
    public static final String SCIENTIFIC_NAME = "Maesa lanceolata";
    public static final String QR_CODE_ID = "TREE-011";
    public static final String FAMILY = "Myrsinaceae (Primulaceae / Maesa family)";
    public static final String TYPICAL_HEIGHT = "3–10 m (shrub to small tree)";
    public static final String ORIGIN = "Tropical Africa — forest margins and woodland; native in Rwanda and East Africa";
    public static final String AGE_ESTIMATE = "Approx. 5–25 years (park specimen)";
    public static final double LATITUDE = -1.9674;
    public static final double LONGITUDE = 30.1090;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-011";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-011";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Maesa_lanceolata";

    private MaesaLanceolataData() {}

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
        tree.getCategories().addAll(List.of("CULTURAL", "MEDICINAL", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(11);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/5/58/Maesa_lanceolata_1.jpg",
                        "Maesa lanceolata — false assegai (Umuhanga)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/1/16/Maesa_lanceolata%2C_habitus%2C_Louwsburg.jpg",
                        "Umuhanga — habit of Maesa lanceolata",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/5/53/Maesa_lanceolata%2C_bloeiwyse%2C_Louwsburg.jpg",
                        "Flowering shoots of Maesa lanceolata",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/9/93/Maesa_lanceolata%2C_vrugte%2C_Louwsburg.jpg",
                        "Fruits of Maesa lanceolata (false assegai)",
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
        TreeTranslation t = base(tree, "en", "False assegai");
        t.setShortDescription(
                "Maesa lanceolata — false assegai, known in Kinyarwanda as Umuhanga. A native African shrub or " +
                "small tree used in Kubandwa ceremony and traditional care for mental distress; seeds studied for " +
                "anticancer and antifungal activity."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umuhanga.",
                "Also called false assegai — a shrub to small tree of forest margins.",
                "Culturally used in the Kubandwa ceremony.",
                "Traditional use includes treating mental illness (ibisazi).",
                "Seeds of Maesa lanceolata have anticancer compounds and antifungal activity.",
                "Family traditionally listed as Myrsinaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Myrsinaceae (Primulaceae)",
                "Scientific name: Maesa lanceolata Forssk.",
                "Common name: False assegai",
                "Local name (Kinyarwanda): Umuhanga",
                "Typical height: 3–10 m",
                "Habitat: Forest margins, woodland, thickets",
                "Cultural note: Kubandwa ceremony",
                "Status in Rwanda: Native"
        ));
        t.setDescription(
                "Maesa lanceolata is the false assegai — a shrub or small tree native across much of tropical " +
                "Africa. In Rwanda it is known as Umuhanga and holds an important place in cultural and healing " +
                "traditions.\n\n" +
                "Park ethnobotanical notes record its use in the Kubandwa ceremony and in traditional treatment of " +
                "mental illness (kuvura ibisazi). Scientific work has also reported that seeds of Maesa lanceolata " +
                "contain anticancer compounds and show antifungal activity.\n\n" +
                "At Kigali Eco-Park this species is presented as Umuhanga / false assegai, matching local naming " +
                "and documented uses."
        );
        t.setUses(
                "Cultural: Maesa lanceolata is culturally used in the Kubandwa ceremony.\n\n" +
                "Traditional medicine: Used in local practice to treat mental illness (ibisazi).\n\n" +
                "Research: Seeds of Maesa lanceolata have anticancer compounds and antifungal activity.\n\n" +
                "Ecology: Provides cover and fruit for birds and insects along forest edges."
        );
        t.setEcologicalImportance(
                "Umuhanga grows on forest margins and in woodland thickets, supporting understorey structure and " +
                "food for wildlife. As a native species it contributes to Rwanda's indigenous plant diversity."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Cultural ceremonies (Kubandwa), traditional healing, and research interest in seed chemistry.\n\n" +
                "For wildlife: Shelter and fruit for birds and small animals."
        );
        t.setCommonAreas(
                "Native in Rwanda and across tropical Africa — forest edges, woodland, and thickets from lowland " +
                "to mid-altitude sites."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umuhanga label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: false assegai · Umuhanga · Maesa · Myrsinaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umuhanga");
        t.setShortDescription(
                "Umuhanga (Maesa lanceolata / False assegai) — igiti cy'igihugu gikoreshwa mu mihango yo " +
                "Kubandwa no mu kuvura ibisazi."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umuhanga.",
                "Ni igiti cyangwa urwuri rw'inkengero z'amashyamba.",
                "Gikoreshwa mu mihango yo Kubandwa.",
                "Gikoreshwa mu kuvura ibisazi.",
                "Imbuto zafatanyije n'ubushakashatsi bw'anticancer n'antifungal."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Myrsinaceae",
                "Izina ry'ubumenyi: Maesa lanceolata",
                "Izina ry'ikinyarwanda: Umuhanga",
                "Izina ry'icyongereza: False assegai",
                "Uburebure: metero 3–10",
                "Aho ukura: Inkengero z'amashyamba n'ibihuru",
                "Imihango: Kubandwa"
        ));
        t.setDescription(
                "Maesa lanceolata ni Umuhanga — igiti cy'igihugu cy'umuryango wa Myrsinaceae. Mu Rwanda " +
                "gifite akamaro mu mihango n'ubuvuzi gakondo.\n\n" +
                "Akamaro: imihango yo Kubandwa, no kuvura ibisazi. Ubushakashatsi bwerekanye ko imbuto " +
                "zafite imbaraga zo kurwanya kanseri n'udukoko.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umuhanga / false assegai hakurikijwe amazina n'akamaro k'aho."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Imihango yo Kubandwa.\n" +
                "• Kuvura ibisazi.\n" +
                "• Ubushakashatsi: imbuto zafite anticancer n'antifungal activity."
        );
        t.setEcologicalImportance(
                "Umuhanga ufasha gutanga ahantu n'imyambaro ku nyoni n'ibinyabuzima byo mu mashyamba."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Imihango (Kubandwa), ubuvuzi gakondo, n'ubushakashatsi.\n\n" +
                "Ku nyamaswa: Ahantu ho kwihisha n'imbuto ku nyoni."
        );
        t.setCommonAreas(
                "Uboneka mu Rwanda n'Afurika y'ubushyuhe — inkengero z'amashyamba n'ibihuru."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umuhanga kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umuhanga · Maesa lanceolata · False assegai · Myrsinaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Fausse assegai");
        t.setShortDescription(
                "Maesa lanceolata — fausse assegai, appelée Umuhanga en kinyarwanda. Arbuste ou petit arbre " +
                "utilisé dans la cérémonie Kubandwa et en soins traditionnels; graines étudiées pour activité " +
                "anticancéreuse et antifongique."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umuhanga.",
                "Arbuste à petit arbre des lisières forestières.",
                "Usage culturel dans la cérémonie Kubandwa.",
                "Usage traditionnel pour soigner les troubles mentaux (ibisazi).",
                "Graines : composés anticancéreux et activité antifongique."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Myrsinaceae",
                "Nom scientifique : Maesa lanceolata",
                "Nom commun : Fausse assegai",
                "Nom local : Umuhanga",
                "Hauteur : 3–10 m",
                "Habitat : Lisières, boisements"
        ));
        t.setDescription(
                "Maesa lanceolata est la fausse assegai — arbuste ou petit arbre indigène d'Afrique tropicale. " +
                "Au Rwanda, on l'appelle Umuhanga.\n\n" +
                "Usages : cérémonie Kubandwa, soins traditionnels (ibisazi), recherche sur les graines.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umuhanga selon les usages locaux."
        );
        t.setUses(
                "Culturel : utilisé dans la cérémonie Kubandwa.\n\n" +
                "Médecine traditionnelle : traitement des troubles mentaux (ibisazi).\n\n" +
                "Recherche : les graines de Maesa lanceolata ont des composés anticancéreux et une activité antifongique."
        );
        t.setEcologicalImportance(
                "Contribute à la diversité indigène des lisières et offre abri et fruits à la faune."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Cérémonies, médecine traditionnelle, recherche.\n\n" +
                "Pour la faune : Abri et fruits."
        );
        t.setCommonAreas(
                "Indigène au Rwanda et en Afrique tropicale — lisières forestières et fourrés."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umuhanga pour photos, carte et médias.\n\n" +
                "Umuhanga · Maesa lanceolata · Fausse assegai · Myrsinaceae."
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
