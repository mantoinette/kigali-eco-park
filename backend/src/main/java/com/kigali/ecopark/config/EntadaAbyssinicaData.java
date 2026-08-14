package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Entada abyssinica (Umusange / Splinter bean) — TREE-009.
 * Content aligned with park ethnobotanical notes and regional species profiles.
 */
public final class EntadaAbyssinicaData {

    public static final String SLUG = "entada-abyssinica";
    public static final String SCIENTIFIC_NAME = "Entada abyssinica";
    public static final String QR_CODE_ID = "TREE-009";
    public static final String FAMILY = "Fabaceae (Legume / pea family; Mimosaceae)";
    public static final String TYPICAL_HEIGHT = "15–30 m (climbing / scandent tree)";
    public static final String ORIGIN = "Tropical Africa — Sudan to South Africa; native in Rwanda and East Africa";
    public static final String AGE_ESTIMATE = "Approx. 10–40 years (park specimen)";
    public static final double LATITUDE = -1.9680;
    public static final double LONGITUDE = 30.1084;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-009";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-009";
    public static final String REFERENCE_URL =
            "https://en.wikipedia.org/wiki/Entada_abyssinica";

    private EntadaAbyssinicaData() {}

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
        tree.setDisplayOrder(9);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/d/dd/Entada_abyssinica_MS_1790.JPG",
                        "Entada abyssinica — splinter bean tree in woodland",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/7/7c/Entada_abyssinica_MHNT.BOT.2009.13.16.jpg",
                        "Umusange — Entada abyssinica herbarium specimen",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/f/f0/Entada_abyssinica_S-1381_6399.jpg",
                        "Developing pod of Entada abyssinica",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/1/1e/Entada_abyssinica_S-1387_6310.jpg",
                        "Splinter bean pod in savannah habitat",
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
        TreeTranslation t = base(tree, "en", "Splinter bean");
        t.setShortDescription(
                "Entada abyssinica — splinter bean, known in Kinyarwanda as Umusange. A large climbing legume " +
                "tree of tropical Africa, widely used in traditional medicine and noted in research for " +
                "entadanin with antibacterial, antioxidant and anticancer activity."
        );
        t.setInterestingFacts(String.join("\n",
                "Local name (Kinyarwanda): Umusange.",
                "Large scandent tree producing huge woody pods with hard splinter-like seeds.",
                "Traditional medicine for cough, respiratory complaints, diarrhoea and fever.",
                "Scientific research identified entadanin with strong antibacterial activity.",
                "Recorded activity against Salmonella typhimurium in laboratory studies.",
                "Also studied for antioxidant and anticancer properties."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae (Mimosaceae)",
                "Scientific name: Entada abyssinica Steud. ex A.Rich.",
                "Common name: Splinter bean",
                "Local name (Kinyarwanda): Umusange",
                "Typical size: Large climbing tree, 15–30 m",
                "Fruit: Enormous woody pod with hard flattened seeds",
                "Active compound (research): Entadanin",
                "Status in Rwanda: Native indigenous tree"
        ));
        t.setDescription(
                "Entada abyssinica is the splinter bean — a large climbing or scandent legume tree native to " +
                "tropical Africa. In Rwanda it is known as Umusange and is recognised in ethnobotany for a wide " +
                "range of traditional medicinal uses.\n\n" +
                "The tree produces massive woody pods containing hard, flat seeds. Traditional healers use " +
                "preparations for cough, side pain, respiratory disease, diarrhoea, fever and related complaints.\n\n" +
                "Scientific research has shown that entadanin from this species possesses antibacterial activity " +
                "against Salmonella typhimurium, along with documented antioxidant and anticancer activity in " +
                "laboratory studies.\n\n" +
                "At Kigali Eco-Park this species is presented as Umusange / splinter bean, matching local " +
                "naming and documented uses."
        );
        t.setUses(
                "Traditional medicine: Used by traditional healers for cough, pleurisy/side pain, respiratory " +
                "disease, diarrhoea, fever and related conditions.\n\n" +
                "Research: Entadanin shows antibacterial activity against Salmonella typhimurium; antioxidant " +
                "and anticancer activity documented in scientific studies.\n\n" +
                "Ecological: Large liana-like tree in woodland and riverine forest margins."
        );
        t.setEcologicalImportance(
                "As a large legume climber, Entada abyssinica adds structure to woodland and forest-edge habitats. " +
                "Its massive pods and seeds support seed dispersal ecology; nitrogen-fixing roots enrich soils " +
                "where it grows."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Traditional medicine; subject of pharmacological research on entadanin.\n\n" +
                "For wildlife: Pods and seeds used by animals; canopy structure in native woodlands."
        );
        t.setCommonAreas(
                "Native across tropical Africa from East Africa to southern Africa. Found in woodland, savannah " +
                "margins and riverine forest; tolerates seasonal drought."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umusange label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: splinter bean · Umusange · Entada abyssinica · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umusange");
        t.setShortDescription(
                "Umusange (Entada abyssinica / Splinter bean) — igiti cy'igihugu cy'umuryango wa Fabaceae " +
                "gikoreshwa cyane mu buvuzi gakondo."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umusange.",
                "Ni igiti kinini cy'umuryango wa Fabaceae gifite ibishyimbo binini.",
                "Abavuzi gakondo bayikoresha mu kuvura indwara zitandukanye.",
                "Ubushakashatsi bwabonye entadanin ifite imbaraga zo kurwanya bakteriya.",
                "Entadanin ifite n'imbaraga zo kurwanya kanseri mu bushakashatsi."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae (Mimosaceae)",
                "Izina ry'ubumenyi: Entada abyssinica",
                "Izina ry'ikinyarwanda: Umusange",
                "Izina ry'icyongereza: Splinter bean",
                "Ubunini: Igiti kinini cy'umuryango (15–30 m)",
                "Igice cy'ubushakashatsi: Entadanin",
                "Aho ukura: Amasaka n'impera z'ibihanga mu Rwanda"
        ));
        t.setDescription(
                "Entada abyssinica ni Umusange — igiti cy'igihugu cy'umuryango wa Fabaceae. Mu Rwanda " +
                "cy'igihugu kandi cy'akamaro mu buvuzi gakondo.\n\n" +
                "Ifite ibishyimbo binini by'igiti. Abavuzi gakondo bayivuza mu kuvura indwara zitandukanye. " +
                "Ubushakashatsi bwabonye muri iki giti entadanin ivura kanseri, ikagira imbaraga zo kurwanya " +
                "bakteriya.\n\n" +
                "Mu Kigali Eco-Park, iki cyerekanwa nk'Umusange / splinter bean hakurikijwe amazina " +
                "n'akamaro k'aho."
        );
        t.setUses(
                "Abavuzi gakondo bayivuza mu kuvura:\n" +
                "• Inkorora\n" +
                "• Rubagimpande\n" +
                "• Indwara z'ubuhumekero\n" +
                "• Gucibwamo (amacinya)\n" +
                "• Umuriro (umuriro w'umubiri)\n" +
                "• Gukuuramo inda\n\n" +
                "Ubushakashatsi: Entadanin ivura kanseri, ikagira imbaraga zo kurwanya bakteriya " +
                "(nka Salmonella typhimurium) n'ibindi."
        );
        t.setEcologicalImportance(
                "Umusange utanga imiterere mu masaka n'ibihanga. Nk'igiti cy'umuryango wa Fabaceae, " +
                "ufasha gushyira nitrogen mu butaka."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Ubuvuzi gakondo n'ubushakashatsi ku entadanin.\n\n" +
                "Ku nyamaswa: Ibishyimbo binini n'imiterere y'igiti mu masaka."
        );
        t.setCommonAreas(
                "Uboneka mu Rwanda n'Afurika y'ubushyuhe — mu masaka, ku mpera z'ibihanga no mu migezi."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umusange kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umusange · Entada abyssinica · Splinter bean · Fabaceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Entada d'Abyssinie");
        t.setShortDescription(
                "Entada abyssinica — haricot éclatant, appelé Umusange en kinyarwanda. Grand légumineux " +
                "grimpant d'Afrique tropicale, utilisé en médecine traditionnelle et étudié pour l'entadanine."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umusange.",
                "Arbre grimpant aux gousses ligneuses géantes.",
                "Médecine traditionnelle : toux, respiration, diarrhée, fièvre.",
                "Entadanine : activité antibactérienne, antioxydante et anticancéreuse (recherche).",
                "Activité documentée contre Salmonella typhimurium."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae (Mimosaceae)",
                "Nom scientifique : Entada abyssinica",
                "Nom commun : Haricot éclatant",
                "Nom local : Umusange",
                "Taille : Grand arbre grimpant, 15–30 m",
                "Composé actif : Entadanine"
        ));
        t.setDescription(
                "Entada abyssinica est l'entada d'Abyssinie — grand légumineux grimpant indigène d'Afrique " +
                "tropicale. Au Rwanda, on l'appelle Umusange.\n\n" +
                "Usages ethnobotaniques et pharmacologiques : entadanine avec activité antibactérienne, " +
                "antioxydante et anticancéreuse.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umusange selon les usages locaux."
        );
        t.setUses(
                "Médecine traditionnelle : Toux, douleurs latérales, maladies respiratoires, diarrhée, fièvre.\n\n" +
                "Recherche : Entadanine — antibactérien (Salmonella typhimurium), antioxydant, anticancéreux."
        );
        t.setEcologicalImportance(
                "Structure des boisements tropicaux ; fixation de l'azote ; grosses gousses dans l'écologie des graines."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Médecine traditionnelle et recherche pharmaceutique.\n\n" +
                "Pour la faune : Gousses et graines dans les écosystèmes forestiers."
        );
        t.setCommonAreas(
                "Indigène au Rwanda et à l'Afrique tropicale — bois, lisières et forêts riveraines."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umusange pour photos, carte et médias.\n\n" +
                "Umusange · Entada abyssinica · Haricot éclatant · Fabaceae."
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
