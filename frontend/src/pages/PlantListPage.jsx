import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function PlantListPage({ trees }) {
  const { language } = useLanguage();

  return (
    <div className="page">
      <div className="page-hero-strip">
        <div className="section-container">
          <span className="section-eyebrow">{t(language, 'plantlist')}</span>
          <h1>{t(language, 'plantlistTitle')}</h1>
          <p>{t(language, 'plantlistIntro')}</p>
        </div>
      </div>

      <div className="section-container page-body">
        <div className="info-banner">
          <span aria-hidden="true">📱</span>
          <p>{t(language, 'scanForDetails')}</p>
        </div>

        {trees.length === 0 ? (
          <div className="empty-state">
            <span aria-hidden="true">🌱</span>
            <p>{t(language, 'noTrees')}</p>
          </div>
        ) : (
          <ul className="name-only-list">
            {trees.map((tree) => (
              <li key={tree.id} className="name-only-item">
                <div className="name-only-main">
                  <span className="name-only-common">{tree.commonName}</span>
                  <span className="name-only-scientific">{tree.scientificName}</span>
                </div>
                <span className="name-only-hint">{t(language, 'scanAtPark')}</span>
              </li>
            ))}
          </ul>
        )}

        <div className="section-cta">
          <Link to="/about" className="btn btn-outline">
            {t(language, 'aboutUs')}
          </Link>
        </div>
      </div>
    </div>
  );
}
