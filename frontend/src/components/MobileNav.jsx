import { Link, useLocation } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

/** Bottom mobile nav — mirrors main site structure. */
const NAV_ITEMS = [
  { key: 'home', path: '/', icon: '🏠', labelKey: 'navHome' },
  { key: 'trees', path: '/trees', icon: '🌳', labelKey: 'exploreTrees' },
  { key: 'map', path: '/map', icon: '🗺️', labelKey: 'navMap' },
  { key: 'about', path: '/about', icon: 'ℹ️', labelKey: 'aboutUs' },
  { key: 'contact', path: '/contact', icon: '✉️', labelKey: 'navContact' },
];

export default function MobileNav() {
  const location = useLocation();
  const { language } = useLanguage();

  return (
    <nav className="mobile-nav" aria-label={t(language, 'mainNav')}>
      {NAV_ITEMS.map(({ key, path, icon, labelKey }) => {
        const active = path === '/'
          ? location.pathname === '/'
          : location.pathname.startsWith(path);

        return (
          <Link
            key={key}
            to={path}
            className={`mobile-nav-item ${active ? 'active' : ''}`}
          >
            <span className="mobile-nav-icon" aria-hidden="true">{icon}</span>
            <span className="mobile-nav-label">{t(language, labelKey)}</span>
          </Link>
        );
      })}
    </nav>
  );
}
