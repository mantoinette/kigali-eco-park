package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Albizia versicolor (Umububa / Poison-pod Albizia) — TREE-004.
 * Content aligned with PROTA and PlantZAfrica species profiles.
 */
public final class AlbiziaVersicolorData {

    public static final String SLUG = "albizia-versicolor";
    public static final String SCIENTIFIC_NAME = "Albizia versicolor";
    public static final String QR_CODE_ID = "TREE-004";
    public static final String FAMILY = "Fabaceae (Legume / pea family)";
    public static final String TYPICAL_HEIGHT = "10–20 m (up to 25 m)";
    public static final String ORIGIN = "Sub-Saharan Africa — Angola to Tanzania, south to South Africa and Madagascar";
    public static final String AGE_ESTIMATE = "Approx. 10–30 years (park specimen)";
    public static final double LATITUDE = -1.9695;
    public static final double LONGITUDE = 30.1062;
    public static final String AUDIO_BASE_PATH = "/media/audio/TREE-004";
    public static final String VIDEO_BASE_PATH = "/media/video/TREE-004";
    public static final String REFERENCE_URL =
            "https://pza.sanbi.org/albizia-versicolor";

    private AlbiziaVersicolorData() {}

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
        tree.setDisplayOrder(4);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/b/b4/Albizia_versicolor_tree.jpg",
                        "Albizia versicolor — poison-pod albizia with spreading crown",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Albizia_versicolor_411596378.jpg/1280px-Albizia_versicolor_411596378.jpg",
                        "Large-leaved Albizia — foliage and branch structure",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Albizia_versicolor_105503311.jpg/1280px-Albizia_versicolor_105503311.jpg",
                        "Poison-pod Albizia in natural woodland habitat",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Albizia_versicolor_105503422.jpg/1280px-Albizia_versicolor_105503422.jpg",
                        "Creamy-white flower heads and compound leaves",
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
        TreeTranslation t = base(tree, "en", "Poison-pod Albizia");
        t.setShortDescription(
                "Albizia versicolor — also called poison-pod albizia or large-leaved albizia. " +
                "A deciduous African legume tree with a spreading crown, large compound leaves, " +
                "and medicinal bark used widely in traditional African medicine."
        );
        t.setInterestingFacts(String.join("\n",
                "Common names: poison-pod albizia, large-leaved false thorn, large-leaved albizia.",
                "New leaves emerge pinkish-red before turning olive-green — the name versicolor means 'variously coloured'.",
                "Produces large fluffy creamy-white flower heads and long reddish-brown pods.",
                "Pods and seeds are toxic to livestock (albiziosis); young fallen pods are most dangerous.",
                "Nitrogen-fixing legume used in agroforestry and as an ornamental shade tree.",
                "IUCN status: Least Concern."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Fabaceae (Legume / pea family)",
                "Scientific name: Albizia versicolor Welw. ex Oliv.",
                "Common name: Poison-pod Albizia / large-leaved albizia",
                "Local name (Kinyarwanda): Umububa",
                "Typical height: 10–20 m (up to ~25 m)",
                "Crown: Spreading, rounded to flat",
                "Flowers: Large fluffy creamy-white to greenish-yellow heads",
                "Fruit: Long thin pods, reddish-brown when mature (up to ~27 cm)",
                "Habitat: Woodland, savanna and riverine forest in tropical Africa"
        ));
        t.setDescription(
                "Albizia versicolor is a medium to large deciduous tree in the legume family Fabaceae. " +
                "It is native to much of sub-Saharan Africa and is widely known as poison-pod albizia " +
                "because its pods contain toxins harmful to cattle, sheep and goats.\n\n" +
                "The tree has a straight trunk and an open, spreading crown. Bark on older stems is " +
                "greyish-brown and rough; young branches are hairy. Leaves are bipinnately compound " +
                "with large, leathery leaflets that change colour as they mature. Flowers appear in " +
                "large, semi-spherical fluffy heads and are creamy-white to greenish-yellow.\n\n" +
                "At Kigali Eco-Park this species is presented as Umububa, matching local Rwandan " +
                "ethnobotanical naming while preserving the scientific profile of Albizia versicolor."
        );
        t.setUses(
                "Medicine: Bark is medicinal. Roots and bark are used to treat anaemia, swollen glands, " +
                "diseases associated with sexual intercourse, backache, and as an anthelmintic (internal worms). " +
                "Bark relieves coughs, headache and sinusitis; powdered bark may be sniffed for the same purpose. " +
                "Bark can also be applied to the skin for rashes.\n\n" +
                "Wood & household: Timber for building and general carpentry; bark fibre used for kitchen " +
                "and household items in some communities.\n\n" +
                "Fuel: Used as firewood.\n\n" +
                "Agroforestry & shade: Planted for shade and in agroforestry systems, but note that pods " +
                "are toxic to livestock when eaten from the ground."
        );
        t.setEcologicalImportance(
                "As a nitrogen-fixing legume, Albizia versicolor enriches soils and supports woodland " +
                "and savanna ecosystems. Its flowers provide nectar for pollinators and its crown offers " +
                "shade and habitat structure for birds and insects.\n\n" +
                "Important caution: fallen pods can poison grazing livestock in late dry season — a key " +
                "management consideration near pasture."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Traditional medicine, timber, firewood, shade and agroforestry value; " +
                "ornamental potential in parks and gardens.\n\n" +
                "For wildlife: Flowers attract pollinators; crown and pods provide food and shelter " +
                "for birds and insects (livestock must be kept away from fallen pods)."
        );
        t.setCommonAreas(
                "Indigenous across tropical and subtropical Africa — from Angola and East Africa " +
                "(including Tanzania and the Rwanda region) south to South Africa; also recorded in Madagascar.\n\n" +
                "Typical habitats: dry woodland, savanna, bushveld and riverine forest on a variety of soils."
        );
        t.setAdditionalInfo(
                "Species reference: " + REFERENCE_URL + "\n\n" +
                "At Kigali Eco-Park, scan the QR on the Umububa label for photos, map location, and " +
                "multilingual audio/video.\n\n" +
                "Also known as: poison-pod albizia, large-leaved albizia, large-leaved false thorn.\n" +
                "Family: Fabaceae. Conservation: IUCN Least Concern."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umububa");
        t.setShortDescription(
                "Umububa (Albizia versicolor) — igiti kinini cy'umuryango wa Fabaceae gifite " +
                "ibyatsi binini n'ibishyimbo by'ubuvuzi. Mu Rwanda cyitwa Umububa."
        );
        t.setInterestingFacts(String.join("\n",
                "Izina ry'icyongereza: poison-pod albizia, large-leaved albizia.",
                "Ibyatsi bishya biba ibara ry'umutuku-rose mbere yo guhinduka ibyatsi by'icyatsi.",
                "Indabo ni nini, z'umweru-icyatsi, zisa n'ibyatsi byoroshye.",
                "Ibishyimbo n'imbuto birashobora kwica amatungo — birafite uburozi.",
                "Ni igiti cya legume gifasha gufata azote mu butaka.",
                "IUCN: Least Concern."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Fabaceae",
                "Izina ry'ubumenyi: Albizia versicolor",
                "Izina ry'ikinyarwanda: Umububa",
                "Uburebure: metero 10–20 (kugeza ~25)",
                "Igiti: Gifite igiti cyagutse",
                "Indabo: Nini, z'umweru-icyatsi",
                "Ibishyimbo: By'ibara ry'umutuku-brown, birebire",
                "Aho zikura: Ibiti by'igihugu, savana n'imigezi"
        ));
        t.setDescription(
                "Albizia versicolor ni igiti kinini cy'umuryango wa Fabaceae. Mu Rwanda twagira " +
                "tukacyita Umububa. Gikura mu biti by'igihugu, savana n'imbibi z'imigezi mu Afurika y'ubuturo.\n\n" +
                "Igiti gifite umubumbe ugororotse n'igiti cyagutse. Igishihwa cyacyo ni cyera-cy'ibara " +
                "ry'umukara ku gice gikuru; amashami make afite uboya. Ibyatsi ni binini kandi bifite " +
                "ibice byinshi. Indabo ziboneka mu matongo manini y'ibyatsi byoroshye.\n\n" +
                "Mu Kigali Eco-Park, iki giti cyerekana Umububa nk'uko cyanditswe mu bucukumbuzi " +
                "bw'u Rwanda, hamwe n'amakuru y'ubumenyi ku bwoko Albizia versicolor."
        );
        t.setUses(
                "Akamaro:\n" +
                "• Kuvura inzoka zo munda (uburozi bw'ibinyabuzima mu nda).\n" +
                "• Kwongera amaraso mu mubiri (anemia).\n" +
                "• Kivura kubyimbirwa (impande z'uruhu zibyimbye).\n" +
                "• Kivura indwara zifata mu myanya y'ibanga.\n" +
                "• Kivura kubabara umutwe.\n" +
                "• Bakibazamo ibikoresho byo mu gikoni.\n" +
                "• Kivangwa n'imyaka.\n\n" +
                "Igishihwa n'imizi bikoreshwa mu buvuzi gakondo. Igishihwa gishobora gukoreshwa " +
                "ku gukura impfu, kubabara umutwe n'uburwayi bwo mu zuru; uburozi bw'igishihwa bushobora " +
                "gukorerwa mu nzu. Ibishyimbo birafite uburozi ku matungo — birafite agaciro ko kwitondera " +
                "hafi y'ibyatsi by'amatungo."
        );
        t.setEcologicalImportance(
                "Nk'igiti cya legume, Umububa ufasha gufata azote mu butaka no gufasha ibidukikije " +
                "by'ibiti n'ibyatsi. Indabo zacyo zifasha inzuki n'inyoni. Ibishyimbo byamanutse ku butaka " +
                "bishobora kwica amatungo — ibintu by'ingenzi mu gucunga ahantu hafi y'urugo rw'amatungo."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Ubuvuzi gakondo, ibiti, imyaka, igicucu, n'ibikoresho byo mu gikoni.\n\n" +
                "Ku nyamaswa: Indabo zifasha inzuki n'inyoni; igiti gitanga igicucu. Amatungo agomba " +
                "kwirindwa ku bishyimbo byamanutse."
        );
        t.setCommonAreas(
                "Iboneka mu Afurika y'ubuturo — kuva Angola, Tanzaniya n'Uburundi/U Rwanda kugeza " +
                "muri Afurika y'Epfo. Imiterere: ibiti by'igihugu, savana, imigezi n'ubutaka butandukanye."
        );
        t.setAdditionalInfo(
                "Inkomoko: " + REFERENCE_URL + "\n\n" +
                "Sikana QR ku kimenyetso cy'Umububa kugira ngo ubone amafoto, ikarita, n'amajwi.\n\n" +
                "Umububa · Albizia versicolor · Fabaceae · IUCN LC."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Albizie versicolore");
        t.setShortDescription(
                "Albizia versicolor — aussi appelée albizie à gousse toxique ou albizie à grandes feuilles. " +
                "Arbre légumineux caduc d'Afrique à couronne étalée, aux feuilles composées larges et " +
                "à l'écorce médicinale."
        );
        t.setInterestingFacts(String.join("\n",
                "Noms : poison-pod albizia, large-leaved albizia, albizie versicolore.",
                "Les jeunes feuilles sont rosées avant de devenir vert olive — versicolor signifie « de couleurs variées ».",
                "Fleurs en grosses têtes crémeuses et gousses longues brun-rougeâtre.",
                "Gousses et graines toxiques pour le bétail (albiziose).",
                "Légumineuse fixatrice d'azote utilisée en agroforesterie.",
                "Statut UICN : Préoccupation mineure."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Fabaceae",
                "Nom scientifique : Albizia versicolor",
                "Nom commun : Albizie à gousse toxique",
                "Nom local : Umububa",
                "Hauteur : 10–20 m (jusqu'à ~25 m)",
                "Couronne : Étalee, arrondie",
                "Fleurs : Grosses têtes blanc-crème",
                "Fruits : Gousses longues brun-rougeâtre",
                "Habitat : Forêts sèches, savanes et ripisylves"
        ));
        t.setDescription(
                "Albizia versicolor est un arbre caduc de taille moyenne à grande de la famille des Fabacées, " +
                "indigène en Afrique subsaharienne. Il est connu pour ses gousses toxiques pour le bétail.\n\n" +
                "Le tronc est droit avec une couronne ouverte et étalée. L'écorce est gris-brun et rugueuse " +
                "sur les vieilles branches. Les feuilles sont bipinnées avec de larges folioles coriaces. " +
                "Les fleurs apparaissent en têtes fluffy crémeuses.\n\n" +
                "Au Kigali Eco-Park, cette fiche présente Umububa selon la nomenclature locale rwandaise."
        );
        t.setUses(
                "Médecine : L'écorce est médicinale. Racines et écorce contre l'anémie, les ganglions enflés, " +
                "certaines maladies liées aux rapports sexuels, le mal de dos et comme anthelmintique. " +
                "L'écorce soulage toux, maux de tête et sinusite ; poudre à inhaler.\n\n" +
                "Bois et ménage : Bois de construction ; fibre d'écorce pour ustensiles domestiques.\n\n" +
                "Combustible : Bois de feu.\n\n" +
                "Agroforesterie : Ombrage et systèmes agroforestiers — attention aux gousses toxiques au sol."
        );
        t.setEcologicalImportance(
                "Légumineuse fixatrice d'azote enrichissant les sols. Fleurs pour pollinisateurs ; " +
                "couronne ombragée pour oiseaux et insectes. Les gousses tombées peuvent empoisonner " +
                "le bétail en fin de saison sèche."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Médecine traditionnelle, bois, combustible, ombrage.\n\n" +
                "Pour la faune : Fleurs et abri ; les animaux d'élevage doivent éviter les gousses au sol."
        );
        t.setCommonAreas(
                "Afrique tropicale et subtropicale — de l'Angola à l'Afrique de l'Est et jusqu'à l'Afrique " +
                "australe ; aussi à Madagascar. Forêts sèches, savanes et ripisylves."
        );
        t.setAdditionalInfo(
                "Référence : " + REFERENCE_URL + "\n\n" +
                "Scannez le QR de l'étiquette Umububa pour photos, carte et médias multilingues.\n\n" +
                "Umububa · Albizia versicolor · Fabaceae · UICN LC."
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
