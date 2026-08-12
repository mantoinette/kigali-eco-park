import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { displayCommonName } from '../utils/treeDisplay';

function shortFamily(family) {
  if (!family) return '';
  return family.split('(')[0].trim();
}

function treeHref(tree) {
  return `/trees/${tree.qrCodeId || tree.slug}`;
}

/**
 * Compact, scalable tree directory — names first, no large images.
 * Each row opens that tree's dedicated preview page.
 */
export default function TreeDirectory({ trees }) {
  const { language } = useLanguage();

  return (
    <div className="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
      <div className="hidden grid-cols-[7.5rem_minmax(0,1.2fr)_minmax(0,1.4fr)_minmax(0,1fr)_2.5rem] gap-3 border-b border-gray-100 bg-primary-dark/5 px-5 py-3 text-[11px] font-semibold uppercase tracking-[0.14em] text-gray-500 md:grid">
        <span>{t(language, 'directoryColumnId')}</span>
        <span>{t(language, 'directoryColumnName')}</span>
        <span>{t(language, 'directoryColumnScientific')}</span>
        <span>{t(language, 'directoryColumnFamily')}</span>
        <span className="sr-only">{t(language, 'openTreePage')}</span>
      </div>

      <ul className="divide-y divide-gray-100">
        {trees.map((tree) => {
          const commonName = displayCommonName(tree, language);
          const href = treeHref(tree);
          return (
            <li key={tree.id || tree.slug || tree.qrCodeId}>
              <Link
                to={href}
                className="group grid items-center gap-1 px-4 py-3.5 transition hover:bg-primary/5 md:grid-cols-[7.5rem_minmax(0,1.2fr)_minmax(0,1.4fr)_minmax(0,1fr)_2.5rem] md:gap-3 md:px-5"
                aria-label={`${commonName} — ${tree.scientificName || ''}`}
              >
                <span className="font-mono text-[11px] font-bold tracking-wide text-primary">
                  {tree.qrCodeId || '—'}
                </span>
                <span className="min-w-0">
                  <span className="block truncate font-display text-base font-semibold text-primary-dark group-hover:text-primary">
                    {commonName}
                  </span>
                  <span className="mt-0.5 block truncate text-sm italic text-gray-500 md:hidden">
                    {tree.scientificName}
                  </span>
                </span>
                <span className="hidden truncate text-sm italic text-gray-600 md:block">
                  {tree.scientificName}
                </span>
                <span className="hidden truncate text-sm text-gray-500 md:block">
                  {shortFamily(tree.family)}
                </span>
                <span
                  className="hidden justify-self-end text-lg text-primary/50 transition group-hover:translate-x-0.5 group-hover:text-primary md:block"
                  aria-hidden="true"
                >
                  →
                </span>
              </Link>
            </li>
          );
        })}
      </ul>
    </div>
  );
}
