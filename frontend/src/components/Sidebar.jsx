import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { displayCommonName } from '../utils/treeDisplay';
import LanguageSwitcher from './LanguageSwitcher';

export default function Sidebar({ trees = [], open, onNavigate }) {
  const location = useLocation();
  const { language } = useLanguage();
  const { user, isAuthenticated, logout } = useAuth();

  const isActive = (path) => {
    if (path === '/') return location.pathname === '/';
    return location.pathname.startsWith(path);
  };

  return (
    <aside className={`sidebar ${open ? 'open' : ''}`}>
      <div className="sidebar-brand">
        <Link to="/" className="sidebar-logo-link" onClick={onNavigate}>
          <div className="logo-badge">
            <span className="logo-mark" aria-hidden="true">
              <svg viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="20" cy="20" r="19" stroke="currentColor" strokeWidth="1.5" />
                <path d="M20 32V18M20 18C20 18 14 16 12 10C16 12 20 18 20 18ZM20 18C20 18 26 16 28 10C24 12 20 18 20 18Z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
            </span>
            <div>
              <span className="logo-text">KIGALI</span>
              <span className="logo-sub">Eco-Park</span>
            </div>
          </div>
        </Link>
        <p className="sidebar-tagline">{t(language, 'footerTagline')}</p>
      </div>

      <nav className="sidebar-nav" aria-label={t(language, 'mainNav')}>
        <Link to="/" className={`nav-link ${isActive('/') && location.pathname === '/' ? 'active' : ''}`} onClick={onNavigate}>
          <span className="nav-icon" aria-hidden="true">🏠</span>
          {t(language, 'navHome')}
        </Link>
        <Link to="/plantlist" className={`nav-link ${isActive('/plantlist') ? 'active' : ''}`} onClick={onNavigate}>
          <span className="nav-icon" aria-hidden="true">🌳</span>
          {t(language, 'plantlist')}
        </Link>
        <Link to="/about" className={`nav-link ${isActive('/about') ? 'active' : ''}`} onClick={onNavigate}>
          <span className="nav-icon" aria-hidden="true">ℹ️</span>
          {t(language, 'aboutUs')}
        </Link>
        <Link to="/faq" className={`nav-link ${isActive('/faq') ? 'active' : ''}`} onClick={onNavigate}>
          <span className="nav-icon" aria-hidden="true">❓</span>
          {t(language, 'faq')}
        </Link>

        <div className="nav-auth-section">
          {isAuthenticated ? (
            <>
              <p className="nav-section-label">{user?.fullName}</p>
              <button
                type="button"
                className="nav-link nav-button"
                onClick={() => {
                  logout();
                  onNavigate?.();
                }}
              >
                <span className="nav-icon" aria-hidden="true">🚪</span>
                {t(language, 'logout')}
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className={`nav-link ${isActive('/login') ? 'active' : ''}`} onClick={onNavigate}>
                <span className="nav-icon" aria-hidden="true">🔑</span>
                {t(language, 'login')}
              </Link>
              <Link to="/register" className={`nav-link ${isActive('/register') ? 'active' : ''}`} onClick={onNavigate}>
                <span className="nav-icon" aria-hidden="true">📝</span>
                {t(language, 'register')}
              </Link>
            </>
          )}
        </div>

        {trees.length > 0 && (
          <div className="nav-trees-section">
            <p className="nav-section-label">{t(language, 'treesInPark')}</p>
            <p className="nav-trees-note">{t(language, 'scanForDetails')}</p>
            {trees.map((tree) => (
              <div key={tree.id} className="nav-tree-name">
                <span className="tree-dot" aria-hidden="true" />
                <span className="tree-link-text">
                  <span className="tree-common">{displayCommonName(tree, language)}</span>
                  <span className="tree-scientific">{tree.scientificName}</span>
                </span>
              </div>
            ))}
          </div>
        )}
      </nav>

      <div className="sidebar-footer">
        <LanguageSwitcher />
      </div>
    </aside>
  );
}
