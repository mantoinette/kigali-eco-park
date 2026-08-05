import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function VisitCta() {
  const { language } = useLanguage();

  return (
    <section className="visit-cta">
      <div className="section-container visit-cta-inner">
        <div>
          <h2>{t(language, 'visitCtaTitle')}</h2>
          <p>{t(language, 'visitCtaText')}</p>
        </div>
        <div className="visit-cta-actions">
          <Link to="/plantlist" className="btn btn-light">
            {t(language, 'explorePlants')}
          </Link>
          <Link to="/faq" className="btn btn-outline-light">
            {t(language, 'faq')}
          </Link>
        </div>
      </div>
    </section>
  );
}
