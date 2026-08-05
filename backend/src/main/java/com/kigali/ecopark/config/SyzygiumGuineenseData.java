package com.kigali.ecopark.config;

import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.entity.TreeImage;
import com.kigali.ecopark.entity.TreeTranslation;
import com.kigali.ecopark.service.TreeImageAcquisitionService;

import java.util.List;

/**
 * Curated multilingual content for Syzygium guineense — the first tree profile
 * developed for Kigali Eco-Park.
 */
public final class SyzygiumGuineenseData {

    public static final String SLUG = "syzygium-guineense";
    public static final String SCIENTIFIC_NAME = "Syzygium guineense";
    public static final String QR_CODE_ID = "TREE-001";
    public static final String FAMILY = "Myrtaceae (Myrtle family)";
    public static final String TYPICAL_HEIGHT = "10–25 m";
    public static final String ORIGIN = "Sub-Saharan Africa (native to East Africa)";
    public static final String AGE_ESTIMATE = "Approx. 15–40 years (park specimen)";
    public static final double LATITUDE = -1.9686;
    public static final double LONGITUDE = 30.1045;
    /** Base path for park-recorded narrations (language suffix added by the frontend). */
    public static final String AUDIO_BASE_PATH = "/media/audio/syzygium-guineense";
    /** Base path for tree videos (language suffix -en/-fr/-rw added by the frontend). */
    public static final String VIDEO_BASE_PATH = "/media/video/syzygium-guineense";

    private SyzygiumGuineenseData() {}

    public static void applyTo(Tree tree, String apiPublicBaseUrl) {
        applyMetadata(tree, apiPublicBaseUrl);

        tree.getTranslations().clear();
        tree.getTranslations().add(english(tree));
        tree.getTranslations().add(kinyarwanda(tree));
        tree.getTranslations().add(french(tree));
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
        tree.getCategories().addAll(List.of("FRUIT", "MEDICINAL", "SHADE", "WILDLIFE"));
        tree.setAudioUrl(apiPublicBaseUrl + AUDIO_BASE_PATH + "-en.mp3");
        tree.setVideoUrl(apiPublicBaseUrl + VIDEO_BASE_PATH + "-en.mp4");
        tree.setPublished(true);
        tree.setDisplayOrder(1);
    }

    public static List<TreeImageAcquisitionService.ImageRequest> imageSources() {
        return List.of(
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Syzygium_guineense.jpg/1280px-Syzygium_guineense.jpg",
                        "Syzygium guineense — woodland waterberry in its natural habitat",
                        true,
                        1
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Syzygium_guineense_25188238.jpg/1280px-Syzygium_guineense_25188238.jpg",
                        "Evergreen crown and foliage of Syzygium guineense",
                        false,
                        2
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Syzygium_guineense_guineense_15594186.jpg/1280px-Syzygium_guineense_guineense_15594186.jpg",
                        "Close view of leaves and branches",
                        false,
                        3
                ),
                new TreeImageAcquisitionService.ImageRequest(
                        "direct:https://upload.wikimedia.org/wikipedia/commons/1/1a/Syzygium_guineense_71895621.jpg",
                        "Botanical detail — Syzygium guineense subsp. guineense",
                        false,
                        4
                )
        );
    }

    public static void attachImages(Tree tree, List<TreeImageAcquisitionService.AcquiredImage> acquiredImages) {
        tree.getImages().clear();
        for (TreeImageAcquisitionService.AcquiredImage acquired : acquiredImages) {
            tree.getImages().add(image(
                    tree,
                    acquired.publicUrl(),
                    acquired.caption(),
                    acquired.primary(),
                    acquired.displayOrder()
            ));
        }
    }

    private static TreeTranslation english(Tree tree) {
        TreeTranslation t = base(tree, "en", "Woodland Waterberry");
        t.setShortDescription(
                "A native evergreen waterberry with glossy leaves, fragrant white flowers, and edible purple berries — a cornerstone of Rwanda's wetland forests."
        );
        t.setInterestingFacts(String.join("\n",
                "Can live over 200 years in ideal conditions.",
                "Native to East Africa and widespread across tropical Africa.",
                "Flowers bloom between August and December in the Rwanda region.",
                "Young leaves emerge in an attractive purplish-red colour.",
                "Berries are a traditional snack for children across East Africa."
        ));
        t.setQuickFacts(String.join("\n",
                "Family: Myrtaceae (Myrtle family)",
                "Typical height: 10–25 metres",
                "Habit: Evergreen tree with a rounded, heavy crown",
                "Leaves: Opposite, glossy dark green; young leaves purplish-red",
                "Flowers: Creamy-white, sweetly scented (Aug–Dec in Rwanda region)",
                "Fruit: Oval berries, purple to black when ripe (Dec–Apr)",
                "Habitat: Riverbanks, wetlands, forest edges, moist woodlands"
        ));
        t.setDescription(
                "Syzygium guineense is a medium to large evergreen tree native to sub-Saharan Africa. " +
                "It is widely known as the woodland waterberry or water pear because it thrives beside rivers, " +
                "swamps, and other moist habitats. The tree has a broad trunk, drooping branches, and a dense " +
                "rounded crown that provides deep shade.\n\n" +
                "Young leaves emerge in an attractive purplish-red colour before maturing to shiny dark green. " +
                "In season, the tree bears fragrant creamy-white flowers in rounded clusters, followed by " +
                "edible purple-to-black berries. In Rwanda, Syzygium guineense subsp. guineense is recorded " +
                "in the national flora and is an important indigenous species of moist lowland environments."
        );
        t.setUses(
                "Timber & crafts: The wood is pale red, hard, durable, and easy to work. It is used for " +
                "construction, furniture, tool handles, carvings, poles, and traditionally for dugout canoes.\n\n" +
                "Food: Ripe berries are eaten fresh—especially by children—or used to make beverages and vinegar. " +
                "The sweet pulp is sucked and the seed discarded.\n\n" +
                "Medicine: Bark, leaves, and roots are used in African traditional medicine for digestive " +
                "complaints, colic, and bathing therapies. Caution: bark preparations can be toxic if used incorrectly.\n\n" +
                "Other uses: Bark for tanning and dyeing; leaves and flowers as livestock fodder and bee forage; " +
                "excellent shade tree in gardens and coffee plots."
        );
        t.setEcologicalImportance(
                "Syzygium guineense is a keystone species of moist African landscapes. Its roots stabilise " +
                "riverbanks and reduce soil erosion along waterways. The dense canopy moderates temperature and " +
                "humidity in wetland and forest-edge ecosystems.\n\n" +
                "Fragrant flowers attract bees and other pollinators. Ripe fruit feeds birds, fruit bats, and " +
                "small mammals, spreading seeds across the landscape. The tree tolerates seasonal waterlogging, " +
                "making it valuable for restoring riparian zones and protecting watersheds in Rwanda's hill-country environment."
        );
        t.setBenefitsToPeopleAndWildlife(
                "For people: Provides shade, edible fruit, medicinal plants, timber, fuelwood, and charcoal. " +
                "During difficult seasons it has served as a famine food in parts of East Africa. Honey bees visit " +
                "the flowers, supporting local beekeeping.\n\n" +
                "For wildlife: Birds and mammals feed on the berries. The tree offers nesting sites and shelter " +
                "within its crown. Butterflies and insects visit the flowers, supporting broader food webs in the park."
        );
        t.setCommonAreas(
                "Widespread across tropical Africa from sea level to about 2,500 m elevation. In East Africa it " +
                "occurs in Rwanda, Uganda, Kenya, Tanzania, Ethiopia, and surrounding regions.\n\n" +
                "Typical habitats include lowland and montane forest margins, riverbanks, humid savanna, swamps, " +
                "and moist valleys. In Rwanda it is associated with woodland and wetland edges throughout the country."
        );
        t.setAdditionalInfo(
                "At Kigali Eco-Park, this tree introduces visitors to Rwanda's indigenous moist-forest flora. " +
                "Scan the QR code on the label to return to this page any time.\n\n" +
                "Also known as: Waterberry, water pear, Syzygium guineense subsp. guineense.\n" +
                "Flowering period (regional): August – December. Fruiting: December – April."
        );
        return t;
    }

    private static TreeTranslation kinyarwanda(Tree tree) {
        TreeTranslation t = base(tree, "rw", "Umubavu w'Igihugu");
        t.setShortDescription(
                "Igiti cy'igihugu kidapfa ibyatsi gifite ibyatsi by'icyatsi kibisi, indabo z'impumuro nziza n'imbuto zirya — ingenzi mu masaka y'igihugu ahameza."
        );
        t.setInterestingFacts(String.join("\n",
                "Gishobora kubaho imyaka irenga 200 mu bihe byiza.",
                "Cyavutse mu Burasirazuba bw'Afurika kandi kiboneka mu turere tw'Afurika y'ubuturo.",
                "Indabo zitera mu gihe cy'Agasozi kugeza ku Ukuboza mu Rwanda.",
                "Ibyatsi bishya biba ibara ry'ubutare n'umutuku.",
                "Imbuto zirya ni ibiribwa by'ingenzi by'abana mu Burasirazuba bw'Afurika."
        ));
        t.setQuickFacts(String.join("\n",
                "Umuryango: Myrtaceae",
                "Uburebure busanzwe: metero 10–25",
                "Ubwoko: Igiti kidapfa ibyatsi gifite igiti cyuzuye",
                "Ibyatsi: Bikurura hamwe, by'icyatsi kibisi; ibishya biba ibara ry'ubutare n'umutuku",
                "Indabo: Z'umweru, zifite impumuro nziza (Agasozi–Ukuboza)",
                "Imbuto: Nyinshi, zibiba umuhora kugeza ku zirabwa (Ukuboza– Mata)",
                "Aho zikura: Impande z'imigezi, imigezi, imbibi z'amasaka"
        ));
        t.setDescription(
                "Syzygium guineense ni igiti kinini kidapfa ibyatsi gifite inkomoko mu buhinga bwa Afrika yo " +
                "hepfo y'ubutaka bwa Sahara. Kimenyekana cyane nka woodland waterberry cyangwa water pear " +
                "kuko gikunda ahantu hameza n'imigezi. Gifite umubumbe mugari, amashami yamanuka, n'igiti " +
                "cyuzuye gitanga igicucu kinini.\n\n" +
                "Ibyatsi bishya biba ibara ry'ubutare n'umutuku mbere yo guhinduka iby'icyatsi kibisi. Mu gihe " +
                "cyacyo, gitanga indabo z'umweru zifite impumuro nziza, hanyuma imbuto ziboneka zigira " +
                "umuhondo kugeza ku zirabwa. Mu Rwanda, Syzygium guineense subsp. guineense ibarwa mu " +
                "flora y'igihugu kandi ni ubwoko bw'ingenzi bw'ibidukikije by'ahantu hameza."
        );
        t.setUses(
                "Ibiti n'ubukorikori: Ibiti by'ibara ry'umutuku bucoro, bikomeye, biramba kandi byoroshye " +
                "gukoresha. Bikoreshwa mu kubaka, gukora ibikoresho by'urugo, inkoni, ibishushanyo, inkingi, " +
                "n'amato y'abakurambere.\n\n" +
                "Ibiribwa: Imbuto zirabwa zirya—cyane n'abana—cyangwa zikoreshwa mu gukora ibinyobwa. Umushyushya " +
                "w'iryoshye ushobora gusucwa n'imbuto isigara.\n\n" +
                "Ubuvejuru: Igiti, ibyatsi, n'imizi bikoreshwa mu buvuzi bwa kinyarwanda ku ndwara zo mu nda " +
                "n'izindi. Iburira: imiti y'igiti ishobora kuba y'uburozi niba itagenzuwe neza.\n\n" +
                "Izindi nyungu: Igiti cy'ibara, ibyatsi n'indabo by'amatungo, gutera inzuki, n'igiti cy'igicucu " +
                "mu busitani n'imirima y'ikawa."
        );
        t.setEcologicalImportance(
                "Syzygium guineense ni ubwoko bw'ingenzi mu bidukikije by'Afurika by'ahantu hameza. Imizi yacyo " +
                "ifasha mu kubungabunga imbibi y'imigezi no gukumira erosiyo y'ubutaka. Igiti cyuzuye gafasha " +
                "gucunga ubushyuhe n'ubushuhe mu masaka n'imbibi z'imigezi.\n\n" +
                "Indabo zifite impumuro nziza zishaka inzuki n'izindi nyongera. Imbuto zirabwa zirirwa n'inyoni " +
                "n'inyamaswa, zigatanga imbuto mu turere. Igiti cyihanganira amazi menshi mu gihe runaka, " +
                "bikacyo gikora cyane mu kugarura imigezi no kurinda amasoko y'amazi mu Rwanda rwo mu misozi."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Ku bantu: Gitanga igicucu, imbuto zirya, ibimera by'ubuvuzi, ibiti, inkoni, n'amariba. Mu bihe " +
                "bigoye byari ibiribwa by'ingenzi mu bice by'Uburasirazuba bw'Afurika. Inzuki zishaka indabo, " +
                "bikafasha ubworozi bw'inzuki.\n\n" +
                "Ku nyamaswa: Inyoni n'inyamaswa zirya imbuto. Igiti gitanga ahantu ho kurara no kwihisha mu " +
                "giti cyacyo. Ibinyabutabire n'uduraganda busura indabo, bigafasha ubuzima bw'ibinyabuzima mu busitani."
        );
        t.setCommonAreas(
                "Iboneka mu turere tw'Afurika y'ubuturo kuva ku nkombe z'inyanja kugeza metero 2,500. Mu " +
                "Burasirazuba bw'Afurika iboneka mu Rwanda, Uganda, Kenya, Tanzaniya, Etiyopiya n'ahandi.\n\n" +
                "Imiterere y'ahantu ibarizwamo harimo imbibi z'amasaka, impande z'imigezi, savana ihumeka, " +
                "imigezi, n'imigezi myinshi. Mu Rwanda ihuza n'amasaka n'imbibi z'imigezi mu gihugu hose."
        );
        t.setAdditionalInfo(
                "Muri Kigali Eco-Park, iki giti cyigisha abashyitsi ibimera by'igihugu by'amasaka ahameza. " +
                "Sikana kode ya QR ku kimenyetso kugira ngo usubire kuri iyi paje igihe cyose.\n\n" +
                "Amazina yo kuyita: Waterberry, water pear, Syzygium guineense subsp. guineense.\n" +
                "Igihe cy'indabo (mu karere): Agasozi – Ukuboza. Igihe cy'imbuto: Ukubozo – Mata."
        );
        return t;
    }

    private static TreeTranslation french(Tree tree) {
        TreeTranslation t = base(tree, "fr", "Faux pistachier d'Afrique");
        t.setShortDescription(
                "Arbre sempervirent indigène aux feuilles brillantes, fleurs blanches parfumées et baies comestibles — pilier des forêts humides du Rwanda."
        );
        t.setInterestingFacts(String.join("\n",
                "Peut vivre plus de 200 ans dans des conditions idéales.",
                "Originaire d'Afrique de l'Est et répandu en Afrique tropicale.",
                "Floraison entre août et décembre dans la région du Rwanda.",
                "Les jeunes feuilles apparaissent d'un beau rouge pourpre.",
                "Les baies sont une collation traditionnelle pour les enfants en Afrique de l'Est."
        ));
        t.setQuickFacts(String.join("\n",
                "Famille : Myrtaceae (famille des Myrtes)",
                "Hauteur typique : 10–25 mètres",
                "Port : Arbre sempervirent à couronne dense et arrondie",
                "Feuilles : Opposées, vert foncé brillant ; jeunes feuilles rouge pourpre",
                "Fleurs : Blanc crème, au parfum sucré (août–déc. dans la région)",
                "Fruits : Baies ovales, violettes à noires à maturité (déc.–avr.)",
                "Habitat : Berges, zones humides, lisières forestières"
        ));
        t.setDescription(
                "Syzygium guineense est un arbre sempervirent de taille moyenne à grande, originaire d'Afrique " +
                "subsaharienne. Il est connu sous le nom de woodland waterberry ou faux pistachier d'eau car il " +
                "prospère le long des rivières, des marais et des milieux humides. L'arbre présente un tronc " +
                "large, des branches retombantes et une couronne dense offrant une ombre profonde.\n\n" +
                "Les jeunes feuilles apparaissent d'un beau rouge pourpre avant de devenir vert foncé et luisant. " +
                "En saison, il produit des fleurs blanc crème parfumées en cymes arrondies, suivies de baies " +
                "comestibles violet-noir. Au Rwanda, Syzygium guineense subsp. guineense est répertorié dans " +
                "la flore nationale et constitue une espèce indigène importante des milieux humides de plaine."
        );
        t.setUses(
                "Bois et artisanat : Le bois rouge pâle est dur, durable et facile à travailler. Il sert à la " +
                "construction, aux meubles, aux manches d'outils, aux sculptures et aux poteaux.\n\n" +
                "Alimentation : Les baies mûres sont consommées fraîches ou utilisées pour des boissons et du vinaigre.\n\n" +
                "Médecine : L'écorce, les feuilles et les racines sont utilisées en médecine traditionnelle africaine. " +
                "Attention : certaines préparations d'écorce peuvent être toxiques.\n\n" +
                "Autres usages : Tannage et teinture, fourrage, plante mellifère, arbre d'ombrage en jardins et plantations de café."
        );
        t.setEcologicalImportance(
                "Syzygium guineense est une espèce clé des paysages humides africains. Ses racines stabilisent les " +
                "berges et limitent l'érosion. Son feuillage dense modère la température et l'humidité le long des cours d'eau.\n\n" +
                "Les fleurs attirent les abeilles et autres pollinisateurs. Les fruits nourrissent oiseaux et petits mammifères. " +
                "L'arbre tolère les sols temporairement gorgés d'eau, ce qui le rend précieux pour la restauration des rives au Rwanda."
        );
        t.setBenefitsToPeopleAndWildlife(
                "Pour les populations : Ombre, fruits comestibles, plantes médicinales, bois, combustible et charbon de bois. " +
                "Les abeilles butinent les fleurs, soutenant l'apiculture locale.\n\n" +
                "Pour la faune : Les oiseaux et mammifères consomment les baies. L'arbre offre abri et sites de nidification dans sa couronne."
        );
        t.setCommonAreas(
                "Réparti dans toute l'Afrique tropicale, de la mer jusqu'à environ 2 500 m d'altitude. En Afrique de l'Est : " +
                "Rwanda, Ouganda, Kenya, Tanzanie, Éthiopie et régions voisines.\n\n" +
                "Habitats typiques : lisières de forêts, berges, savanes humides, marais et vallées humides. " +
                "Au Rwanda, associé aux boisements et aux zones ripariennes."
        );
        t.setAdditionalInfo(
                "Au Kigali Eco-Park, cet arbre présente aux visiteurs la flore indigène des milieux humides du Rwanda. " +
                "Scannez le code QR sur l'étiquette pour revenir à cette page à tout moment.\n\n" +
                "Aussi connu sous : Waterberry, water pear, Syzygium guineense subsp. guineense.\n" +
                "Floraison (régionale) : août – décembre. Fructification : décembre – avril."
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
