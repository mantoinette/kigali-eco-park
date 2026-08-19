/**
 * Correct local display names when the live API still has stale translations.
 * Source of truth remains the backend seed data after Render redeploys.
 * Kinyarwanda common names are always shown in CAPITAL LETTERS.
 */
const LOCAL_COMMON_NAMES = {
  'syzygium-guineense': {
    rw: 'Umugote',
    en: 'Waterberry',
    fr: 'Faux pistachier d\'Afrique',
  },
  'ficus-ovata': {
    rw: 'Umurehe',
    en: 'Oval-leaved Fig',
    fr: 'Figuier à feuilles ovales',
  },
  'aeschynomene-elaphroxylon': {
    rw: 'Umuburu',
    en: 'Ambatch',
    fr: 'Ambatch',
  },
  'albizia-versicolor': {
    rw: 'Umububa',
    en: 'Poison-pod Albizia',
    fr: 'Albizie versicolore',
  },
  'bambusa-vulgaris': {
    rw: 'Umugano',
    en: 'Common bamboo',
    fr: 'Bambou commun',
  },
  'erythrina-abyssinica': {
    rw: 'Umuko',
    en: 'Red hot poker tree',
    fr: 'Érythrine d\'Abyssinie',
  },
  'olea-europaea-subsp-africana': {
    rw: 'Umunzenze',
    en: 'Wild olive',
    fr: 'Olivier sauvage',
  },
  'senegalia-polyacantha-campylacantha': {
    rw: 'Umuharata',
    en: "Falcon's claw acacia",
    fr: 'Acacia à griffes de faucon',
  },
  'entada-abyssinica': {
    rw: 'Umusange',
    en: 'Splinter bean',
    fr: 'Entada d\'Abyssinie',
  },
  'phragmites-mauritianus': {
    rw: 'Imiseke',
    en: 'Reed grass',
    fr: 'Roseau africain',
  },
  'maesa-lanceolata': {
    rw: 'Umuhanga',
    en: 'False assegai',
    fr: 'Fausse assegai',
  },
  'senegalia-polyacantha-ruganambuga': {
    rw: 'Ruganambuga',
    en: "Falcon's claw acacia",
    fr: 'Acacia à griffes de faucon',
  },
  'elaeis-guineensis': {
    rw: 'Ikigazi',
    en: 'African oil palm',
    fr: 'Palmier à huile d\'Afrique',
  },
  'chrysophyllum-gorungosanum': {
    rw: 'Umutoyi',
    en: 'Fluted milkwood',
    fr: 'Fluted milkwood',
  },
  'phoenix-reclinata': {
    rw: 'Umukindo',
    en: 'Wild date palm',
    fr: 'Palmier-dattier sauvage',
  },
  'millettia-laurentii': {
    rw: 'Umuyogoro',
    en: 'Faux ebony',
    fr: 'Faux ébène',
  },
  'ficus-thonningii': {
    rw: 'Umuvumu',
    en: 'Wild fig',
    fr: 'Figuier sauvage',
  },
  'trema-orientalis': {
    rw: 'Umudobori',
    en: 'Pigeon wood',
    fr: 'Charbonnier',
  },
  'newtonia-buchananii': {
    rw: 'Umukereko',
    en: 'Forest newtonia',
    fr: 'Newtonia des forêts',
  },
  'blighia-unijugata': {
    rw: 'Umuturamugina',
    en: 'Triangle-top',
    fr: 'Blighia à deux folioles',
  },
};

const STALE_RW_NAMES = new Set(["Umubavu w'Igihugu", 'Umubavu w’Igihugu']);

function formatForLanguage(name, language) {
  if (!name) return '';
  if ((language || 'en').toLowerCase() === 'rw') {
    return name.toLocaleUpperCase('rw-RW');
  }
  return name;
}

export function displayCommonName(tree, language = 'en') {
  if (!tree) return '';
  const slug = tree.slug;
  const lang = (language || 'en').toLowerCase();
  const bySlug = slug ? LOCAL_COMMON_NAMES[slug]?.[lang] : null;
  if (bySlug) return formatForLanguage(bySlug, lang);

  // Safety net: never show the retired RW name for TREE-001 / Syzygium.
  if (
    STALE_RW_NAMES.has(tree.commonName) ||
    tree.qrCodeId === 'TREE-001' ||
    tree.scientificName === 'Syzygium guineense'
  ) {
    if (lang === 'rw') return formatForLanguage('Umugote', lang);
    if (lang === 'en') return LOCAL_COMMON_NAMES['syzygium-guineense'].en;
    if (lang === 'fr') return LOCAL_COMMON_NAMES['syzygium-guineense'].fr;
  }

  return formatForLanguage(tree.commonName || tree.scientificName || '', lang);
}

export function withDisplayName(tree, language = 'en') {
  if (!tree) return tree;
  return { ...tree, commonName: displayCommonName(tree, language) };
}
