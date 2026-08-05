import { Link, useLocation } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const NAV_ITEMS = [
  { key: 'home', path: '/', icon: '🏠' },
  { key: 'plantlist', path: '/plantlist', icon: '🌳' },
  { key: 'about', path: '/about', icon: 'ℹ️' },
  { key: 'login', path: '/login', icon: '🔑' },
];

export default function MobileNav() {
  const location = useLocation();
  const { language } = useLanguage();

  const labels = {
    home: t(language, 'navHome'),
    plantlist: t(language, 'plantlist'),
    about: t(language, 'aboutUs'),
    login: t(language, 'login'),
  };

  return (
    <nav className="mobile-nav" aria-label={t(language, 'mainNav')}>
      {NAV_ITEMS.map(({ key, path, icon }) => {
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
            <span className="mobile-nav-label">{labels[key]}</span>
          </Link>
        );
      })}
    </nav>
  );
}
