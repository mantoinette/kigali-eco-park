package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Ficus ovata (Umurehe / Oval-leaved Fig) — TREE-002.
 * Content aligned with EasyScape species profile:
 * https://easyscape.com/species/Ficus-ovata(Oval-leaved-Fig)
 * plus East-African ethnobotany for park interpretation.
 */
public final class FicusOvataData {

    public static final String SLUG = "ficus-ovata";
    public static final String SCIENTIFIC_NAME = "Ficus ovata";
    public static final String QR_CODE_ID = "TREE-002";
    public static final String FAMILY = "Moraceae (Fig / mulberry family)";
    public static final String TYPICAL_HEIGHT = "10–20 m (up to 25 m)";
    public static final String ORIGIN = "Tropical Africa — Senegal to Ethiopia, south to Mozambique and Angola";
    public static final String AGE_ESTIMATE = "Approx. 12–35 years (park specimen)";
    public static final double LATITUDE = -1.9689;
    public static final double LONGITUDE = 30.1050;
    public static final String AUDIO_BASE_PATH = "/media/audio/ficus-ovata";
    public static final String VIDEO_BASE_PATH = "/media/video/ficus-ovata";
    public static final String REFERENCE_URL =
            "https://easyscape.com/species/Ficus-ovata(Oval-leaved-Fig)";

    private FicusOvataData() {}

    public static void applyTo(Tree tree, String apiPublicBaseUrl) {
        applyMetadata(tree, apiPublicBaseUrl);

        tree.getTranslations().clear();
        tree.getTranslations().add(english(tree));
        tree.getTranslations().add(kinyarwanda(tree));
        tree.getTranslations().add(french(tree));
    }

    /**
     * Update an existing managed tree without clear()+insert (avoids unique constraint on language_code).
     */
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
        tree.getCategories().addAll(java.util.List.of("SHADE", "MEDICINAL", "FIBRE", "WILDLIFE", "TIMBER"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(2);
    }

    /**
     * Browser-reachable gallery for Oval-leaved Fig (EasyScape identity).
     * EasyScape CDN is Cloudflare-blocked for server fetch, so we use verified
     * Wikimedia Ficus ovata photos with the same direct: pattern as tree 1.
     * Reference: https://easyscape.com/species/Ficus-ovata(Oval-leaved-Fig)
     */
    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/5/51/Ficus_ovata-2-JNTBGRI-kerala-India.jpg",
                        "Oval-leaved Fig (Ficus ovata) — large oval leaves and branching",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/4/45/Ficus_ovata-3-JNTBGRI-kerala-India.jpg",
                        "Ficus ovata specimen — foliage detail",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://commons.wikimedia.org/wiki/Special:FilePath/Ficus_ovata-2-JNTBGRI-kerala-India.jpg",
                        "Ficus ovata — oval-leaved crown habit",
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
        TreeTranslation t = base(tree, "en", "Oval-leaved Fig");
        t.setShortDescription(
                "Oval-leaved Fig (Ficus ovata) — known in Rwanda as Umurehe. An evergreen African fig with a " +
                "spreading crown, large oval glossy leaves, and small figs that feed wildlife."
        );
        t.setInterestingFacts(String.join("\n",
                "Common English name: Oval-leaved Fig (EasyScape species profile).",
                "Local Kinyarwanda name at Kigali Eco-Park: Umurehe.",
                "Evergreen, much-branched tree with a spreading crown; usually ~10 m, sometimes to 25 m.",
                "May begin life as an epiphyte before sending aerial roots to the ground.",
                "Figs are pollinated by a specialised fig wasp (Courtella).",
                "Bark fibre is valued in parts of Africa for traditional cloth and garments."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Moraceae (Fig / mulberry family)",
                "Scientific name: Ficus ovata Vahl",
                "Common name: Oval-leaved Fig",
                "Local name: Umurehe",
                "Typical height: 10–20 m (recorded to about 25 m)",
                "Leaves: Large, ovate to elliptic, long-pointed tip (often 9–31 cm long)",
                "Figs: Ovoid to ellipsoid, about 3–5 cm, often pale green with pale spots when ripe",
                "Habitat: Wooded savannah, forest edges, riverine forest, secondary forest up to ~2,100 m",
                "Range: Tropical Africa — Senegal to Ethiopia, south to Mozambique and Angola"
        ));
        t.setDescription(
                "Ficus ovata, commonly called the Oval-leaved Fig, is an evergreen African fig of the family " +
                "Moraceae. It forms a much-branched shrub or tree with a broad, spreading crown and a generally " +
                "straight bole. The species name “ovata” refers to its characteristic oval / egg-shaped leaves.\n\n" +
                "Leaves are rather large (commonly about 9–31 cm long), with a long-pointed tip and a rounded base " +
                "on a long stalk. Bark is pale, thin and relatively smooth. Like many figs, the plant produces " +
                "milky latex when cut.\n\n" +
                "Young plants may start as epiphytes on other trees, later sending aerial roots to the soil. In " +
                "older specimens the crown can carry pendulous aerial roots. At Kigali Eco-Park the tree is " +
                "presented under its Rwandan name Umurehe, matching the EasyScape profile of Oval-leaved Fig."
        );
        t.setUses(
                "Shade & landscape: Widely planted for compound shade, street amenity, live fences and boundary marking.\n\n" +
                "Fibre & crafts: Bark yields fibre used for traditional barkcloth and garments in parts of Africa.\n\n" +
                "Farm & poles: Fast-growing cuttings produce building poles; often left or planted in farmland " +
                "because it improves shade without heavy competition with crops.\n\n" +
                "Medicine: Stem-bark and leaf decoctions are used in African traditional medicine for " +
                "gastrointestinal infections and diarrhoea; latex has been applied for skin conditions such as " +
                "ringworm; latex is also reported to stimulate lactation. Caution: use only with qualified guidance.\n\n" +
                "Wildlife: Figs feed birds and mammals and support specialised fig-wasp pollination."
        );
        t.setEcologicalImportance(
                "Oval-leaved Fig is a multipurpose keystone fig of African woodlands and forest edges. Its " +
                "spreading crown moderates heat and provides nesting and resting sites for birds.\n\n" +
                "Asynchronous flowering within populations sustains fig-wasp pollinators year-round. Ripe figs " +
                "are an important food for frugivores, helping seed dispersal across savannah–forest mosaics " +
                "and park landscapes."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Shade, poles, live fences, bark fibre/cloth, and documented traditional medicinal uses.\n\n" +
                "For wildlife: Figs and canopy shelter for birds and small mammals; obligate fig-wasp partnership."
        );
        t.setCommonAreas(
                "Native and widespread across tropical Africa — from Senegal and West Africa east to Eritrea and " +
                "Ethiopia, and south to Angola, Zambia, Malawi, Mozambique and Madagascar.\n\n" +
                "Habitats include wooded savannah, gallery-forest edges, river sides, secondary forest, evergreen " +
                "bushland, lakesides and swamp-forest margins, typically up to about 2,100 m elevation. Often planted."
        );
        t.setAdditionalInfo(
                "Species reference (EasyScape): " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umurehe label for photos, map, and multilingual audio/video.\n\n" +
                "Also known as: Oval-leaved Fig, Ficus ovata Vahl. Synonyms include Ficus brachypoda.\n" +
                "Kinyarwanda: Umurehe. Family: Moraceae."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umurehe");
        t.setShortDescription(
                "Umurehe (Ficus ovata / Oval-leaved Fig) — igiti cy'Afurika gifite igiti cyagutse, ibyatsi binini " +
                "by'ubuso bw'umuyaga, n'imbuto z'amateke zifasha inyoni n'inyamaswa."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'icyongereza: Oval-leaved Fig (EasyScape).",
                "Izina ry'ikinyarwanda: Umurehe.",
                "Igiti kidapfa ibyatsi gifite igiti cyagutse; uburebure busanzwe ~10 m, gishobora kugera 25 m.",
                "Rimwe na rimwe ritangirira ku bindi biti mbere yo kohereza imizi mu butaka.",
                "Imbuto z'amateke zongerwa n'inzuki zihariye (fig wasps).",
                "Igiti rishobora gukoreshwa mu gukora imyenda / barkcloth."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Moraceae",
                "Izina ry'ubumenyi: Ficus ovata",
                "Izina ry'icyongereza: Oval-leaved Fig",
                "Izina ry'ikinyarwanda: Umurehe",
                "Uburebure: metero 10–20 (kugeza ~25)",
                "Ibyatsi: Binini, by'ubuso bw'umuyaga, isonga rirerire",
                "Imbuto: Amateke 3–5 cm, asanzwe y'icyatsi kibisi n'utudomo twera",
                "Aho zikura: Amasaka, imbibi z'amasaka, impande z'imigezi kugeza ~2,100 m"
        ));
        t.setDescription(
                "Ficus ovata (Oval-leaved Fig) ni igiti cy'umuryango wa Moraceae. Mu Rwanda kizwi nk'Umurehe. " +
                "Gifite igiti cyagutse n'ibyatsi binini by'ubuso bw'umuyaga — ni yo mpamvu yitwa “ovata”.\n\n" +
                "Ibyatsi bishobora kugera cm 9–31. Igiti ryera, rirutoye. Nka indi Ficus, ritanga amateke " +
                "n'amateke yo mu mazi iyo ryacitse.\n\n" +
                "Mu Kigali Eco-Park iki giti cyerekana Oval-leaved Fig nkuko byanditswe na EasyScape, " +
                "hamwe n'izina ry'igihugu Umurehe."
        );
        t.setUses(
                "Igicucu n'ubwiza: Giterwa ku gicucu, mu mihanda, n'uruzitiro.\n\n" +
                "Igiti n'imyenda: Igiti ritanga fibre yo gukora barkcloth n'imyenda ya kinyarwanda.\n\n" +
                "Inkingi: Gikura vuba kuva ku mashami; gitanga inkingi zo kubaka.\n\n" +
                "Ubuvuzi: Igishihwa n'ibyatsi ku ndwara zo mu nda n'amacinya; amateke ku ndwara z'uruhu. " +
                "Iburira: gukoresha ubuvuzi bisaba ubumenyi.\n\n" +
                "Inyamaswa: Imbuto z'amateke ziribwa n'inyoni n'inyamaswa."
        );
        t.setEcologicalImportance(
                "Umurehe ufasha mu gicucu n'ubuzima bw'inyoni. Imbuto z'amateke zitanga ibiribwa by'ingenzi " +
                "ku nyoni n'inyamaswa, zigafasha gutanga imbuto mu masaka n'ubusitani."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Igicucu, inkingi, uruzitiro, imyenda, n'ubuvuzi.\n\n" +
                "Ku nyamaswa: Imbuto z'amateke n'ahantu ho kwihisha."
        );
        t.setCommonAreas(
                "Iboneka mu Afurika y'ubuturo — kuva Senegal kugeza Etiyopiya n'i Mozambique. " +
                "Imiterere: amasaka, imbibi z'amasaka, imigezi, n'amasaka y'inyuma kugeza ~2,100 m."
        );
        t.setAdditionalInfo(
                "Inkomoko (EasyScape): " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umurehe kugira ngo ubone amafoto n'amajwi.\n\n" +
                "Oval-leaved Fig · Ficus ovata · Umurehe · Moraceae."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Figuier à feuilles ovales");
        t.setShortDescription(
                "Figuier à feuilles ovales (Ficus ovata) — Umurehe au Rwanda. Figuier africain sempervirent à " +
                "couronne étalée, grandes feuilles ovales et figues utiles à la faune."
        );
        t.setInterestingFacts(String.join("\n",
                "Nom anglais (EasyScape) : Oval-leaved Fig.",
                "Nom local rwandais : Umurehe.",
                "Arbre sempervirent à couronne large ; environ 10 m, parfois jusqu'à 25 m.",
                "Peut débuter comme épiphyte avant d'envoyer des racines aériennes au sol.",
                "Pollinisation par une guêpe des figues spécialisée (Courtella).",
                "L'écorce fournit une fibre pour le tissu d'écorce traditionnel."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Moraceae",
                "Nom scientifique : Ficus ovata Vahl",
                "Nom commun : Oval-leaved Fig / Figuier à feuilles ovales",
                "Nom local : Umurehe",
                "Hauteur : 10–20 m (jusqu'à ~25 m)",
                "Feuilles : Grandes, ovales à elliptiques, sommet acuminé",
                "Figues : Ovoïdes à ellipsoïdes, 3–5 cm",
                "Habitat : Savane boisée, lisières, forêts riveraines jusqu'à ~2 100 m"
        ));
        t.setDescription(
                "Ficus ovata, appelé Oval-leaved Fig sur EasyScape, est un figuier africain de la famille des " +
                "Moracées. Au Rwanda on l'appelle Umurehe. Il forme un arbuste ou un arbre très ramifié à " +
                "couronne étalée.\n\n" +
                "Les feuilles sont grandes (souvent 9–31 cm), ovales, à pointe allongée — d'où le nom ovata. " +
                "L'écorce est pâle et assez lisse ; la plante produit un latex laiteux.\n\n" +
                "Au Kigali Eco-Park, cette fiche suit le profil EasyScape « Oval-leaved Fig » avec le nom local Umurehe."
        );
        t.setUses(
                "Ombrage et paysage : Planté pour l'ombre, les clôtures vives et le bornage.\n\n" +
                "Fibre et artisanat : Fibre d'écorce pour le tissu d'écorce traditionnel.\n\n" +
                "Perches agricoles : Boutures à croissance rapide pour la construction.\n\n" +
                "Médecine : Décoctions d'écorce et de feuilles contre troubles digestifs ; latex contre la teigne. " +
                "Attention : usage médicinal avec accompagnement compétent.\n\n" +
                "Faune : Figues pour oiseaux et mammifères."
        );
        t.setEcologicalImportance(
                "Espèce clé des boisements et lisières africaines. Sa couronne offre abri et sites de repos. " +
                "Les figues nourrissent les frugivores et maintiennent le partenariat avec les guêpes pollinisatrices."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Ombre, perches, clôtures, fibre d'écorce et usages médicinaux.\n\n" +
                "Pour la faune : Figues et abri dans la couronne."
        );
        t.setCommonAreas(
                "Afrique tropicale — du Sénégal à l'Éthiopie et vers le Mozambique et l'Angola. " +
                "Habitats : savanes boisées, lisières, berges, forêts secondaires jusqu'à environ 2 100 m."
        );
        t.setAdditionalInfo(
                "Référence EasyScape : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umurehe pour photos, carte et médias.\n\n" +
                "Oval-leaved Fig · Ficus ovata · Umurehe · Moraceae."
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
