import { Link, NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { ADMIN_ICONS, IconExternal } from './admin/AdminIcons';

const navClass = ({ isActive }) =>
  `flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition ${
    isActive
      ? 'bg-white/15 text-white shadow-sm'
      : 'text-white/75 hover:bg-white/10 hover:text-white'
  }`;

function userInitials(name) {
  if (!name) return 'A';
  return name
    .split(/\s+/)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase() || '')
    .join('');
}

export default function AdminLayout() {
  const { language } = useLanguage();
  const { user, logout } = useAuth();

  const links = [
    { to: '/admin/dashboard', labelKey: 'adminDashboard', icon: 'dashboard' },
    { to: '/admin/trees', labelKey: 'adminManageTrees', icon: 'tree' },
    { to: '/admin/trees/new', labelKey: 'adminAddTree', icon: 'plus' },
    { to: '/admin/qr', labelKey: 'adminQrCodes', icon: 'qr' },
    { to: '/admin/requests', labelKey: 'adminMessages', icon: 'mail' },
    { to: '/admin/languages', labelKey: 'adminLanguages', icon: 'globe' },
    { to: '/admin/users', labelKey: 'adminUsers', icon: 'users' },
    { to: '/admin/stats', labelKey: 'adminStats', icon: 'chart' },
  ];

  return (
    <div className="min-h-screen bg-[#eef2ef]">
      <div className="lg:flex">
        <aside className="admin-sidebar lg:fixed lg:inset-y-0 lg:z-40 lg:flex lg:w-64 lg:flex-col">
          <div className="flex h-16 items-center gap-3 border-b border-white/10 px-5">
            <Link to="/admin/dashboard" className="flex min-w-0 items-center gap-3">
              <img
                src="/treescan-rwanda-logo.png"
                alt={t(language, 'siteName')}
                className="h-9 w-auto max-w-[140px] object-contain brightness-0 invert"
              />
            </Link>
          </div>

          <nav className="flex-1 space-y-1 overflow-y-auto px-3 py-4" aria-label={t(language, 'adminNav')}>
            <p className="mb-2 px-3 text-[10px] font-semibold uppercase tracking-[0.2em] text-white/45">
              {t(language, 'adminNav')}
            </p>
            {links.map((item) => {
              const Icon = ADMIN_ICONS[item.icon];
              return (
                <NavLink key={item.to} to={item.to} className={navClass} end={item.to === '/admin/dashboard'}>
                  {Icon && <Icon className="h-[18px] w-[18px]" />}
                  <span className="truncate">{t(language, item.labelKey)}</span>
                </NavLink>
              );
            })}
          </nav>

          <div className="border-t border-white/10 p-4">
            <div className="flex items-center gap-3 rounded-lg bg-white/10 px-3 py-2.5">
              <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-primary-light/30 text-xs font-bold text-white">
                {userInitials(user?.fullName)}
              </span>
              <div className="min-w-0 flex-1">
                <p className="truncate text-sm font-semibold text-white">{user?.fullName}</p>
                <p className="truncate text-xs text-white/60">{user?.email}</p>
              </div>
            </div>
          </div>
        </aside>

        <div className="flex min-h-screen flex-1 flex-col lg:pl-64">
          <header className="sticky top-0 z-30 border-b border-gray-200/80 bg-white/95 backdrop-blur-md">
            <div className="flex h-16 items-center justify-between gap-4 px-4 sm:px-6 lg:px-8">
              <div className="min-w-0">
                <p className="text-xs font-medium uppercase tracking-wide text-gray-500">{t(language, 'siteName')}</p>
                <p className="truncate font-display text-lg font-bold text-primary-dark">{t(language, 'adminTitle')}</p>
              </div>
              <div className="flex shrink-0 items-center gap-2">
                <Link
                  to="/"
                  className="inline-flex items-center gap-1.5 rounded-lg border border-gray-200 bg-white px-3 py-2 text-xs font-semibold text-gray-700 transition hover:border-primary/30 hover:text-primary-dark"
                >
                  <IconExternal className="h-3.5 w-3.5" />
                  {t(language, 'backToWebsite')}
                </Link>
                <button
                  type="button"
                  className="rounded-lg bg-primary px-3 py-2 text-xs font-semibold text-white shadow-sm transition hover:bg-primary-dark"
                  onClick={() => {
                    logout();
                    window.location.href = '/admin/login';
                  }}
                >
                  {t(language, 'logout')}
                </button>
              </div>
            </div>
          </header>

          <main className="flex-1 px-4 py-6 sm:px-6 lg:px-8">
            <Outlet />
          </main>
        </div>
      </div>
    </div>
  );
}
