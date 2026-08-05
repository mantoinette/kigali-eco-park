import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function ParkAbout() {
  const { language } = useLanguage();

  return (
    <section className="park-about">
      <div className="section-container about-grid">
        <div className="about-image" aria-hidden="true">
          <div className="about-image-inner" />
        </div>
        <div className="about-content">
          <span className="section-eyebrow">{t(language, 'aboutPark')}</span>
          <h2>{t(language, 'aboutTitle')}</h2>
          <p>{t(language, 'aboutText1')}</p>
          <p>{t(language, 'aboutText2')}</p>
          <ul className="about-list">
            <li>{t(language, 'aboutPoint1')}</li>
            <li>{t(language, 'aboutPoint2')}</li>
            <li>{t(language, 'aboutPoint3')}</li>
          </ul>
          <Link to="/about" className="btn btn-primary btn-dark">
            {t(language, 'aboutUs')}
          </Link>
        </div>
      </div>
    </section>
  );
}
