package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Chrysophyllum gorungosanum (Umutoyi / Fluted milkwood) - TREE-014.
 * Sources aligned with Flora of Rwanda / Zimbabwe and park note wording.
 */
public final class ChrysophyllumGorungosanumData {

    public static final String SLUG = "chrysophyllum-gorungosanum";
    public static final String SCIENTIFIC_NAME = "Chrysophyllum gorungosanum";
    public static final String QR_CODE_ID = "TREE-014";
    public static final String FAMILY = "Sapotaceae";
    // Keep this short: `trees.typical_height` is varchar(50) in the DB.
    public static final String TYPICAL_HEIGHT = "Fluted evergreen forest tree";
    // Keep this comfortably within the DB column (varchar(120)).
    public static final String ORIGIN = "Evergreen forests in Rwanda and tropical Africa";
    public static final String AGE_ESTIMATE = "Approx. 20-60 years (park specimen)";
    public static final double LATITUDE = -1.9666;
    public static final double LONGITUDE = 30.1098;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-014";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-014";
    public static final String REFERENCE_URL = "https://www.rwandaflora.com/speciesdata/species.php?species_id=143740";

    private ChrysophyllumGorungosanumData() {}

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
        tree.setDisplayOrder(14);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://www.mozambiqueflora.com/speciesdata/images/14/143740-1.jpg",
                        "Chrysophyllum gorungosanum — forest tree (Flora of Mozambique)",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://www.mozambiqueflora.com/speciesdata/images/14/143740-3.jpg",
                        "Chrysophyllum gorungosanum — remnant forest, Mt Gorongosa",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://www.zimbabweflora.co.zw/speciesdata/images/14/143740-1.jpg",
                        "Chrysophyllum gorungosanum — Castleburn Forest, Vumba",
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
        TreeTranslation t = base(tree, "en", "Fluted milkwood");
        t.setShortDescription(
                "Chrysophyllum gorungosanum, known in Kinyarwanda as Umutoyi, is a large evergreen Afromontane " +
                "forest tree valued for latex, timber, charcoal and support for bees."
        );
        t.setInterestingFacts(String.join("\n",
                "Kinyarwanda name: Umutoyi.",
                "English names: Fluted milkwood; Brown-berry fluted-milkwood.",
                "A large evergreen tree with a long straight stem that is characteristically fluted.",
                "Leaves are dark green and shiny above, with rusty or silvery hairs beneath.",
                "A white latex exudes when the bark is cut.",
                "Family: Sapotaceae."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Sapotaceae",
                "Scientific name: Chrysophyllum gorungosanum Engl.",
                "Accepted related name in some sources: Gambeya gorungosana",
                "Common name: Fluted milkwood",
                "Alternative common name: Brown-berry fluted-milkwood",
                "Local name (Kinyarwanda): Umutoyi",
                "Habit: large evergreen forest tree with a fluted bole",
                "Park ID: TREE-014"
        ));
        t.setDescription(
                "Chrysophyllum gorungosanum is a large evergreen tree of the milkwood family, Sapotaceae. " +
                "It is known for its straight, often fluted trunk and its glossy leaves with pale to rusty " +
                "hairy undersides.\n\n" +
                "It occurs in tropical African highland forests, including Rwanda, where it is part of moist " +
                "evergreen forest vegetation. The tree produces latex and durable wood that is used locally " +
                "for timber, construction, firewood and charcoal.\n\n" +
                "At Kigali Eco-Park, TREE-014 presents this species under its Kinyarwanda name Umutoyi and " +
                "its English name Fluted milkwood."
        );
        t.setUses(
                "Latex: White latex is obtained from the tree.\n\n" +
                "Timber and construction: Wood is used for boards, building and other local carpentry.\n\n" +
                "Fuel: Wood is also used for firewood and charcoal.\n\n" +
                "Beekeeping: Flowers support bees as a nectar resource."
        );
        t.setEcologicalImportance(
                "As a tall evergreen forest tree, Umutoyi contributes to canopy structure, shade and habitat " +
                "for forest wildlife. Its flowers and fruits support ecological food webs in montane forest systems."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Latex, timber, construction wood, firewood, charcoal and bee forage.\n\n" +
                "For wildlife: Forest cover, fruit and floral resources, and long-term canopy habitat."
        );
        t.setCommonAreas(
                "Recorded across tropical Africa, including Rwanda, Uganda, Kenya, Tanzania, DRC, Zambia, Malawi, " +
                "Mozambique, Zimbabwe, Angola and Cameroon; typically in evergreen forest."
        );
        t.setAdditionalInfo(
                "Reference: " + REFERENCE_URL + "\n\n" +
                "QR label: Scan the TREE-014 Umutoyi sign to reopen the full multilingual guide.\n\n" +
                "Also listed in some sources as Gambeya gorungosana."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umutoyi");
        t.setShortDescription(
                "Umutoyi ni igiti kinini cy'ishyamba gihora kibisi. Gitanga amata y'ibiti (latex), ibiti byo " +
                "kubaka no kubaza, inkwi n'amakara, kandi gifasha inzuki kubona indabo."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'ikinyarwanda: Umutoyi.",
                "Izina ry'ubumenyi: Chrysophyllum gorungosanum.",
                "Ni igiti kinini cy'ishyamba gifite uruti rurerure rukunze kuba rufite imirongo y'ubusate.",
                "Amababi aba asa neza hejuru, hasi hakagaragara udushishi tw'ibara ry'umutuku cyangwa ifeza.",
                "Iyo gikatwe cyangwa gisemwe, gisohora amata y'ibiti (latex)."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Sapotaceae",
                "Izina ry'ubumenyi: Chrysophyllum gorungosanum",
                "Irindi zina rikoreshwa ahandi: Gambeya gorungosana",
                "Izina ry'ikinyarwanda: Umutoyi",
                "Izina ry'icyongereza: Fluted milkwood",
                "Ahandi banayita: Brown-berry fluted-milkwood",
                "Imiterere: igiti kinini cy'ishyamba gihora kibisi",
                "Ikimenyetso: TREE-014"
        ));
        t.setDescription(
                "Umutoyi ni igiti kinini cy'umuryango wa Sapotaceae. Ubusanzwe kiba mu mashyamba atoshye kandi " +
                "gihora kibisi. Kigaragazwa n'uruti rurerure kandi rukunze kuba rufite ubusate n'imirongo.\n\n" +
                "Mu Rwanda no mu bindi bice by'Afurika yo hagati n'iburasirazuba, kiba mu mashyamba y'imisozi " +
                "ifite imvura nyinshi. Gikoreshwa mu kuvamo amata y'ibiti, mu kubaka, mu kubaza, mu nkwi no mu " +
                "gukora amakara.\n\n" +
                "Muri Kigali Eco-Park, TREE-014 yerekana Umutoyi nk'igiti gifite akamaro ku bantu no ku bidukikije."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Givamo amata y'ibiti (latex).\n" +
                "• Igiti cyacyo gikoreshwa mu kubaka no kubaza.\n" +
                "• Gikoreshwa nk'inkwi no mu gukora amakara.\n" +
                "• Indabo zacyo zifasha inzuki kubona indabo n'ubuki."
        );
        t.setEcologicalImportance(
                "Umutoyi ufasha kubaka urusobe rw'ishyamba, ugatanga igicucu n'aho ibinyabuzima byo mu ishyamba " +
                "biba. Indabo n'imbuto byawo bifasha urusobe rw'ubuzima rw'ishyamba."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Latex, ibiti byo kubaka no kubaza, inkwi, amakara n'ubworozi bw'inzuki.\n\n" +
                "Ku nyamaswa n'ibindi binyabuzima: Igicucu, aho kuba, ibiribwa n'ubuzima bw'ishyamba."
        );
        t.setCommonAreas(
                "Uboneka mu mashyamba atoshye yo mu Rwanda no mu bindi bihugu byinshi byo muri Afurika yo hagati " +
                "n'iburasirazuba."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR iri ku kimenyetso cya TREE-014 Umutoyi urebe amajwi, videwo n'ibisobanuro mu ndimi nyinshi."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Fluted milkwood");
        t.setShortDescription(
                "Chrysophyllum gorungosanum, appelé Umutoyi en kinyarwanda, est un grand arbre sempervirent " +
                "de forêt montagnarde utilisé pour le latex, le bois, le charbon et l'appui à l'apiculture."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom local : Umutoyi.",
                "Nom scientifique : Chrysophyllum gorungosanum.",
                "Grand arbre forestier à tronc souvent cannelé.",
                "Feuilles vert foncé dessus, avec duvet roux ou argenté dessous.",
                "Le tronc donne un latex blanc lorsqu'il est incisé."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Sapotaceae",
                "Nom scientifique : Chrysophyllum gorungosanum",
                "Nom commun : Fluted milkwood",
                "Autre nom anglais : Brown-berry fluted-milkwood",
                "Nom local : Umutoyi",
                "Identifiant parc : TREE-014"
        ));
        t.setDescription(
                "Chrysophyllum gorungosanum est un grand arbre sempervirent des forêts humides africaines. " +
                "Son tronc droit est souvent cannelé, et ses feuilles ont une face inférieure soyeuse.\n\n" +
                "L'espèce est présente dans les forêts tropicales d'altitude, y compris au Rwanda. Elle est " +
                "utilisée pour le latex, le bois d'oeuvre, la construction, le bois de feu et le charbon.\n\n" +
                "Au Kigali Eco-Park, TREE-014 présente cette espèce sous le nom local Umutoyi."
        );
        t.setUses(
                "Latex.\n\n" +
                "Bois pour construction et menuiserie.\n\n" +
                "Bois de feu et charbon.\n\n" +
                "Ressource mellifère pour les abeilles."
        );
        t.setEcologicalImportance(
                "Arbre de canopée important dans les forêts humides, il contribue à l'habitat, à l'ombre et aux " +
                "ressources pour la faune."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : latex, bois, énergie et apiculture.\n\n" +
                "Pour la faune : couverture forestière et ressources alimentaires."
        );
        t.setCommonAreas(
                "Présent dans plusieurs pays d'Afrique tropicale, surtout dans les forêts sempervirentes de montagne."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR TREE-014 pour rouvrir le guide complet."
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
