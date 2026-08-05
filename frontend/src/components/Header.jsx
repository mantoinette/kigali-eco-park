import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import LanguageSwitcher from './LanguageSwitcher';

export default function Header({ onMenuToggle, menuOpen }) {
  const { language } = useLanguage();
  const { isAuthenticated } = useAuth();

  return (
    <header className="site-header">
      <div className="header-inner">
        <button
          type="button"
          className="menu-toggle"
          onClick={onMenuToggle}
          aria-label={menuOpen ? t(language, 'closeMenu') : t(language, 'openMenu')}
          aria-expanded={menuOpen}
        >
          <span className={`hamburger ${menuOpen ? 'open' : ''}`} />
        </button>

        <Link to="/" className="header-brand">
          <span className="brand-icon" aria-hidden="true">🌿</span>
          <span className="brand-name">{t(language, 'siteName')}</span>
        </Link>

        <div className="header-actions">
          <Link to={isAuthenticated ? '/about' : '/login'} className="header-auth-link">
            {isAuthenticated ? t(language, 'aboutUs') : t(language, 'login')}
          </Link>
          <LanguageSwitcher compact />
        </div>
      </div>
    </header>
  );
}
