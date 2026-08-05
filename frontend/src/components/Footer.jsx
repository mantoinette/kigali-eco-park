import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function Footer() {
  const { language } = useLanguage();
  const year = new Date().getFullYear();

  return (
    <footer className="site-footer no-print mt-auto border-t border-gray-200 bg-primary-dark text-white">
      <div className="section-container grid gap-10 py-12 md:grid-cols-3">
        <div>
          <div className="mb-4">
            <img
              src="/kigali-eco-park-logo.png"
              alt="Kigali Eco-Park"
              className="h-16 w-auto object-contain"
            />
          </div>
          <p className="text-sm text-white/80 leading-relaxed">
            {t(language, 'footerTagline')}
          </p>
        </div>

        <div>
          <h3 className="mb-4 font-semibold">{t(language, 'navContact')}</h3>
          <ul className="space-y-2 text-sm text-white/80">
            <li>📍 Kigali, Rwanda</li>
            <li>📧 info@ecopark.rw</li>
            <li>📞 +250 788 000 000</li>
          </ul>
        </div>

        <div>
          <h3 className="mb-4 font-semibold">{t(language, 'footerLinks')}</h3>
          <ul className="space-y-2 text-sm">
            <li><Link to="/about" className="text-white/80 hover:text-white">{t(language, 'aboutUs')}</Link></li>
            <li><Link to="/privacy" className="text-white/80 hover:text-white">{t(language, 'privacyPolicy')}</Link></li>
            <li><Link to="/faq" className="text-white/80 hover:text-white">{t(language, 'faq')}</Link></li>
          </ul>
          <div className="mt-4 flex gap-3 text-xl">
            <a href="https://facebook.com" target="_blank" rel="noreferrer" aria-label="Facebook">📘</a>
            <a href="https://instagram.com" target="_blank" rel="noreferrer" aria-label="Instagram">📷</a>
            <a href="https://twitter.com" target="_blank" rel="noreferrer" aria-label="X">🐦</a>
          </div>
        </div>
      </div>

      <div className="border-t border-white/10 py-4 text-center text-xs text-white/60">
        © {year} {t(language, 'siteName')}. {t(language, 'allRightsReserved')}
      </div>
    </footer>
  );
}
