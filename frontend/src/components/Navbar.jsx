import { Link, NavLink } from 'react-router-dom';
import { useState } from 'react';
import { useLanguage } from '../context/LanguageContext';
import { useAuth } from '../context/AuthContext';
import { t } from '../i18n/ui';
import LanguageSelector from './LanguageSelector';

const navLinkClass = ({ isActive }) =>
  `rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
    isActive
      ? 'bg-primary/10 text-primary-dark'
      : 'text-gray-700 hover:bg-gray-100 hover:text-primary-dark'
  }`;

export default function Navbar() {
  const { language } = useLanguage();
  const { isAuthenticated, isAdmin, logout } = useAuth();
  const [open, setOpen] = useState(false);

  const links = [
    { to: '/', label: t(language, 'navHome') },
    { to: '/trees', label: t(language, 'exploreTrees') },
    { to: '/map', label: t(language, 'navMap') },
    { to: '/about', label: t(language, 'aboutUs') },
    { to: '/contact', label: t(language, 'navContact') },
  ];

  const authControls = (
    <>
      {isAdmin ? (
        <Link to="/admin" className="btn btn-primary !px-4 !py-2 text-xs">
          {t(language, 'admin')}
        </Link>
      ) : isAuthenticated ? (
        <span className="rounded-lg bg-gray-100 px-3 py-2 text-xs font-medium text-gray-600">
          {t(language, 'visitorAccount')}
        </span>
      ) : null}
      {isAuthenticated ? (
        <button
          type="button"
          className="btn btn-secondary !px-4 !py-2 text-xs"
          onClick={() => {
            logout();
            setOpen(false);
          }}
        >
          {t(language, 'logout')}
        </button>
      ) : (
        <Link to="/login" className="btn btn-secondary !px-4 !py-2 text-xs">
          {t(language, 'login')}
        </Link>
      )}
    </>
  );

  return (
    <header className="no-print sticky top-0 z-50 border-b border-gray-200/80 bg-white/95 shadow-nav backdrop-blur-md">
      <div className="section-container flex h-16 items-center justify-between gap-4">
        <Link to="/" className="flex shrink-0 items-center">
          <img
            src="/treescan-rwanda-logo.png"
            alt={t(language, 'siteName')}
            className="h-11 w-auto max-w-[200px] object-contain object-left sm:h-12 sm:max-w-[240px]"
          />
        </Link>

        <nav className="hidden items-center gap-1 lg:flex" aria-label={t(language, 'mainNav')}>
          {links.map((link) => (
            <NavLink key={link.to} to={link.to} className={navLinkClass} end={link.to === '/'}>
              {link.label}
            </NavLink>
          ))}
        </nav>

        <div className="hidden items-center gap-3 lg:flex">
          <LanguageSelector />
          <Link to="/search" className="rounded-lg p-2 text-gray-600 hover:bg-gray-100" aria-label={t(language, 'search')}>
            🔍
          </Link>
          {authControls}
        </div>

        <button
          type="button"
          className="rounded-lg p-2 text-gray-700 lg:hidden"
          onClick={() => setOpen((v) => !v)}
          aria-expanded={open}
          aria-label={open ? t(language, 'closeMenu') : t(language, 'openMenu')}
        >
          {open ? '✕' : '☰'}
        </button>
      </div>

      {open && (
        <div className="border-t border-gray-100 bg-white px-4 py-4 lg:hidden">
          <nav className="flex flex-col gap-1">
            {links.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                className={navLinkClass}
                end={link.to === '/'}
                onClick={() => setOpen(false)}
              >
                {link.label}
              </NavLink>
            ))}
            <Link to="/search" className="rounded-lg px-3 py-2 text-sm font-medium text-gray-700" onClick={() => setOpen(false)}>
              {t(language, 'search')}
            </Link>
            <div className="mt-3 border-t border-gray-100 pt-3">
              <LanguageSelector />
            </div>
            <div className="mt-3 flex flex-col gap-2" onClick={() => setOpen(false)}>
              {authControls}
            </div>
          </nav>
        </div>
      )}
    </header>
  );
}

