/**
 * Correct local display names when the live API still has stale translations.
 * Source of truth remains the backend seed data after Render redeploys.
 */
const LOCAL_COMMON_NAMES = {
  'syzygium-guineense': {
    rw: 'Umugote',
    en: 'Waterberry',
    fr: 'Faux pistachier d\'Afrique',
  },
};

const STALE_RW_NAMES = new Set(["Umubavu w'Igihugu", 'Umubavu w’Igihugu']);

export function displayCommonName(tree, language = 'en') {
  if (!tree) return '';
  const slug = tree.slug;
  const lang = (language || 'en').toLowerCase();
  const bySlug = slug ? LOCAL_COMMON_NAMES[slug]?.[lang] : null;
  if (bySlug) return bySlug;

  // Safety net: never show the retired RW name for TREE-001 / Syzygium.
  if (
    STALE_RW_NAMES.has(tree.commonName) ||
    tree.qrCodeId === 'TREE-001' ||
    tree.scientificName === 'Syzygium guineense'
  ) {
    if (lang === 'rw') return 'Umugote';
    if (lang === 'en') return LOCAL_COMMON_NAMES['syzygium-guineense'].en;
    if (lang === 'fr') return LOCAL_COMMON_NAMES['syzygium-guineense'].fr;
  }

  return tree.commonName || tree.scientificName || '';
}

export function withDisplayName(tree, language = 'en') {
  if (!tree) return tree;
  return { ...tree, commonName: displayCommonName(tree, language) };
}
