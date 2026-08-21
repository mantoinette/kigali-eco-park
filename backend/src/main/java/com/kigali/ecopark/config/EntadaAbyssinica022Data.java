package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Entada abyssinica (Umusange / Splinter bean) — TREE-022.
 * Second park specimen of Umusange; distinct from TREE-009 so each QR label has its own guide page.
 * Content aligned with park ethnobotanical notes and research on entadanin.
 */
public final class EntadaAbyssinica022Data {

    public static final String SLUG = "entada-abyssinica-022";
    public static final String SCIENTIFIC_NAME = "Entada abyssinica";
    public static final String QR_CODE_ID = "TREE-022";
    public static final String FAMILY = "Fabaceae (Mimosaceae)";
    public static final String TYPICAL_HEIGHT = "Large climbing tree, 15–30 m";
    public static final String ORIGIN = "Tropical Africa; native in Rwanda and East Africa";
    public static final String AGE_ESTIMATE = "Approx. 10–40 years (park specimen)";
    public static final double LATITUDE = -1.9684;
    public static final double LONGITUDE = 30.1114;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-022";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-022";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Entada_abyssinica";

    private EntadaAbyssinica022Data() {}

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
        tree.setDisplayOrder(22);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/d/dd/Entada_abyssinica_MS_1790.JPG",
                        "Entada abyssinica — Umusange / splinter bean in woodland",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/f/f0/Entada_abyssinica_S-1381_6399.jpg",
                        "Developing pod of Entada abyssinica",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/1/1e/Entada_abyssinica_S-1387_6310.jpg",
                        "Splinter bean pod in savannah habitat",
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
        TreeTranslation t = base(tree, "en", "Splinter bean");
        t.setShortDescription(
                "Entada abyssinica Steud. ex A.Rich. — splinter bean, known in Kinyarwanda as Umusange. " +
                "A Fabaceae (Mimosaceae) tree used in traditional medicine; research highlights entadanin with " +
                "antibacterial, antioxidant and anticancer activity."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umusange.",
                "Family: Fabaceae (Mimosaceae).",
                "Traditional medicine for cough, side pain, respiratory disease, diarrhoea and fever.",
                "Research compound: entadanin.",
                "Antibacterial activity against Salmonella typhimurium.",
                "Also studied for antioxidant and anticancer activity.",
                "Park specimen ID: TREE-022 (second labelled Umusange; TREE-009 is the first)."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae (Mimosaceae)",
                "Scientific name: Entada abyssinica Steud. ex A.Rich.",
                "Common name: Splinter bean",
                "Local name (Kinyarwanda): Umusange",
                "Typical size: large climbing tree, 15–30 m",
                "Research: entadanin (antibacterial, antioxidant, anticancer)",
                "Park ID: TREE-022"
        ));
        t.setDescription(
                "Entada abyssinica is the splinter bean — a large climbing legume of the family Fabaceae " +
                "(Mimosaceae). In Rwanda it is known as Umusange.\n\n" +
                "Traditional medicine uses preparations for cough, side or rib pain (rubagimpande), respiratory " +
                "diseases, diarrhoea, fever and related complaints.\n\n" +
                "Scientific research has shown that entadanin from this species possesses strong antibacterial " +
                "activity against Salmonella typhimurium, which causes diarrhoea and abdominal pain, along with " +
                "documented antioxidant and anticancer activity.\n\n" +
                "At Kigali Eco-Park this TREE-022 specimen is presented as Umusange / splinter bean, matching " +
                "local naming and documented uses. It is a second labelled park specimen of the same species as " +
                "TREE-009."
        );
        t.setUses(
                "Traditional medicine: Treatment of cough, side pain, respiratory disease, diarrhoea and fever.\n\n" +
                "Research: Entadanin with antibacterial activity against Salmonella typhimurium; also antioxidant " +
                "and anticancer activity in laboratory studies."
        );
        t.setEcologicalImportance(
                "A woodland and savannah climbing tree of tropical Africa. Flowers and pods support insects and " +
                "wildlife; the large woody pods are a distinctive feature of the species."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Traditional medicine and a species of pharmacological research interest.\n\n" +
                "For wildlife: Flowers, pods and climbing structure provide food and cover."
        );
        t.setCommonAreas(
                "Tropical Africa, including Rwanda and neighbouring East African countries, in woodland and " +
                "savannah habitats."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umusange (TREE-022) label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: splinter bean · Umusange · Entada abyssinica · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umusange");
        t.setShortDescription(
                "Umusange (Entada abyssinica / splinter bean) — igiti cy'umuryango wa Fabaceae (Mimosaceae). " +
                "Abavuzi gakondo bayivuza; ubushakashatsi bwabonye entadanin ivura kanseri ikarwanya bakteriya."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umusange.",
                "Umuryango: Fabaceae (Mimosaceae).",
                "Abavuzi gakonda bayivuza: inkorora, rubagimpande, indwara z'ubuhumekero.",
                "Bayivuza: gucibwamo, umuriro, n'ibindi.",
                "Ubushakashatsi: entadanin ivura kanseri.",
                "Ikarwanya bakteriya nka Salmonella typhimurium itera guhitwa no kubabara mu nda.",
                "Ikimenyetso cy'ubusitani: TREE-022."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae (Mimosaceae)",
                "Izina ry'ubumenyi: Entada abyssinica Steud. ex A.Rich.",
                "Izina ry'ikinyarwanda: Umusange",
                "Izina ry'icyongereza: Splinter bean",
                "Ubushakashatsi: entadanin",
                "Ikimenyetso: TREE-022"
        ));
        t.setDescription(
                "Entada abyssinica ni Umusange — igiti cy'umuryango wa Fabaceae (Mimosaceae).\n\n" +
                "Akamaro: Abavuzi gakonda bayivuza: Inkorora, Rubagimpande, indwara z'ubuhumekero, Gucibwamo, " +
                "umuriro, gukuuramo inda.\n\n" +
                "Ubushakashatsi bwabonye muri icyo giti entadanin ivura kanseri, ikarwanya bakteriya ndetse na " +
                "Salmonella typhimurium itera guhitwa no kubabara mu nda.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umusange / splinter bean (TREE-022). Ni icyerekezo cya kabiri " +
                "cy'ubwoko bumwe na TREE-009."
        );
        t.setUses(
                "Akamaro / ubuvuzi gakondo:\n" +
                "• Inkorora\n" +
                "• Rubagimpande\n" +
                "• Indwara z'ubuhumekero\n" +
                "• Gucibwamo\n" +
                "• Umuriro\n" +
                "• Gukuuramo inda\n\n" +
                "Ubushakashatsi:\n" +
                "• Entadanin ivura kanseri\n" +
                "• Ikarwanya bakteriya nka Salmonella typhimurium"
        );
        t.setEcologicalImportance(
                "Umusange ukura mu mashyamba n'ubwatsi bwa savannah muri Afurika y'ubushyuhe; utanga amashurwe " +
                "n'ibishyimbo ku nyamaswa."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Ubuvuzi gakondo n'ubushakashatsi ku entadanin.\n\n" +
                "Ku nyamaswa: Amashurwe, ibishyimbo n'ubuturo."
        );
        t.setCommonAreas(
                "Muri Afurika y'ubushyuhe harimo u Rwanda n'ibihugu by'Iburasirazuba."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umusange (TREE-022) kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umusange · Entada abyssinica · Splinter bean · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Entada d'Abyssinie");
        t.setShortDescription(
                "Entada abyssinica — appelé Umusange en kinyarwanda. Légumineuse grimpante ; médecine " +
                "traditionnelle et entadanine (antibactérienne, antioxydante, anticancéreuse)."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umusange.",
                "Famille : Fabaceae (Mimosaceae).",
                "Médecine traditionnelle : toux, douleurs de côté, maladies respiratoires, diarrhée, fièvre.",
                "Entadanine active contre Salmonella typhimurium.",
                "Identifiant parc : TREE-022."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae (Mimosaceae)",
                "Nom scientifique : Entada abyssinica Steud. ex A.Rich.",
                "Nom commun : Splinter bean",
                "Nom local : Umusange",
                "Identifiant parc : TREE-022"
        ));
        t.setDescription(
                "Entada abyssinica est le haricot éclatant. Au Rwanda : Umusange.\n\n" +
                "Usages : médecine traditionnelle ; recherches sur l'entadanine.\n\n" +
                "Au Kigali Eco-Park, cette fiche TREE-022 présente Umusange (second spécimen après TREE-009)."
        );
        t.setUses(
                "Médecine traditionnelle : toux, douleurs de côté, maladies respiratoires, diarrhée, fièvre.\n\n" +
                "Recherche : entadanine antibactérienne (Salmonella typhimurium), antioxydante et anticancéreuse."
        );
        t.setEcologicalImportance(
                "Arbre grimpant des savanes et forêts claires d'Afrique tropicale ; fleurs et gousses pour la faune."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Médecine traditionnelle et recherche pharmacologique.\n\n" +
                "Pour la faune : Fleurs, gousses et abri."
        );
        t.setCommonAreas(
                "Afrique tropicale, y compris le Rwanda et l'Afrique de l'Est."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umusange (TREE-022) pour photos, carte et médias.\n\n" +
                "Umusange · Entada abyssinica · Splinter bean · Fabaceae."
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
