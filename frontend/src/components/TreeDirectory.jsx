import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { displayCommonName } from '../utils/treeDisplay';
import { TreeSpeciesHeading } from './TreeSpeciesHeading';

function shortFamily(family) {
  if (!family) return '';
  return family.split('(')[0].trim();
}

function treeHref(tree) {
  return `/trees/${tree.qrCodeId || tree.slug}`;
}

/**
 * Compact, scalable tree directory — scientific name first, no large images.
 */
export default function TreeDirectory({ trees }) {
  const { language } = useLanguage();

  return (
    <div className="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
      <div className="hidden grid-cols-[minmax(0,1.4fr)_minmax(0,1fr)_minmax(0,0.9fr)_2.5rem] gap-3 border-b border-gray-100 bg-primary-dark/5 px-5 py-3 text-[11px] font-semibold uppercase tracking-[0.14em] text-gray-500 md:grid">
        <span>{t(language, 'directoryColumnScientific')}</span>
        <span>{t(language, 'directoryColumnName')}</span>
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
                className="group grid items-center gap-1 px-4 py-3.5 transition hover:bg-primary/5 md:grid-cols-[minmax(0,1.4fr)_minmax(0,1fr)_minmax(0,0.9fr)_2.5rem] md:gap-3 md:px-5"
                aria-label={`${tree.scientificName || ''} — ${commonName}`}
              >
                <TreeSpeciesHeading
                  scientificName={tree.scientificName}
                  commonName={commonName}
                  variant="directory"
                />
                <span className="hidden truncate text-sm text-gray-500 md:block">
                  {commonName}
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
