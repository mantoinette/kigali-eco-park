import { Link } from 'react-router-dom';
import TreeDirectory from './TreeDirectory';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

/** Home featured strip — compact directory preview, not large cards. */
export default function FeaturedTrees({ trees }) {
  const { language } = useLanguage();

  if (!trees?.length) return null;

  const featured = trees.slice(0, 6);

  return (
    <section className="bg-surface py-20">
      <div className="section-container">
        <div className="mb-8 flex flex-wrap items-end justify-between gap-4">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.2em] text-primary">
              {t(language, 'treesPageEyebrow')}
            </p>
            <h2 className="section-title mt-2">{t(language, 'featuredTrees')}</h2>
            <p className="section-subtitle">{t(language, 'featuredTreesIntro')}</p>
          </div>
          <Link to="/trees" className="btn btn-secondary !rounded-xl !py-2.5 text-sm">
            {t(language, 'viewAllTrees')}
          </Link>
        </div>

        <TreeDirectory trees={featured} />
      </div>
    </section>
  );
}
