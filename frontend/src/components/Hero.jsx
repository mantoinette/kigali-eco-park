import { Link } from 'react-router-dom';
import LanguageSwitcher from './LanguageSwitcher';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function Hero() {
  const { language } = useLanguage();

  return (
    <section className="hero">
      <div className="hero-bg" aria-hidden="true" />
      <div className="hero-content">
        <span className="hero-badge">{t(language, 'heroBadge')}</span>
        <h1>{t(language, 'heroTitle')}</h1>
        <p className="hero-subtitle">{t(language, 'heroSubtitle')}</p>

        <div className="hero-language-panel">
          <p className="hero-language-title">{t(language, 'chooseLanguage')}</p>
          <LanguageSwitcher showHint />
        </div>

        <div className="hero-actions">
          <Link to="/plantlist" className="btn btn-primary">
            {t(language, 'explorePlants')}
          </Link>
          <Link to="/faq" className="btn btn-outline">
            {t(language, 'howItWorks')}
          </Link>
        </div>

        <div className="hero-trust">
          <span>{t(language, 'trustQr')}</span>
          <span className="hero-trust-dot" aria-hidden="true">·</span>
          <span>{t(language, 'trustFree')}</span>
          <span className="hero-trust-dot" aria-hidden="true">·</span>
          <span>{t(language, 'trustMultilingual')}</span>
        </div>
      </div>
      <div className="hero-scroll" aria-hidden="true">
        <span>↓</span>
      </div>
    </section>
  );
}
