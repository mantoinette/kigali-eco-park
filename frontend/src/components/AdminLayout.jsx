import { Link, NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const navClass = ({ isActive }) =>
  `flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium transition ${
    isActive
      ? 'bg-primary text-white shadow-sm'
      : 'text-gray-700 hover:bg-primary/5 hover:text-primary-dark'
  }`;

export default function AdminLayout() {
  const { language } = useLanguage();
  const { user, logout } = useAuth();

  const links = [
    { to: '/admin/dashboard', labelKey: 'adminDashboard', icon: '📊' },
    { to: '/admin/requests', labelKey: 'adminMessages', icon: '✉️' },
    { to: '/admin/trees', labelKey: 'adminManageTrees', icon: '🌳' },
    { to: '/admin/qr', labelKey: 'adminQrCodes', icon: '📱' },
    { to: '/admin/settings', labelKey: 'adminSettings', icon: '⚙️' },
  ];

  return (
    <div className="min-h-screen bg-surface">
      <header className="border-b border-gray-200 bg-white">
        <div className="section-container flex h-16 items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <Link to="/admin/dashboard" className="font-display text-lg font-bold text-primary-dark">
              {t(language, 'adminTitle')}
            </Link>
            <span className="hidden rounded-full bg-primary/10 px-3 py-1 text-xs font-medium text-primary-dark sm:inline">
              {user?.fullName}
            </span>
          </div>
          <div className="flex items-center gap-2">
            <Link to="/" className="btn btn-secondary !rounded-xl !px-3 !py-2 text-xs">
              {t(language, 'backToWebsite')}
            </Link>
            <button
              type="button"
              className="btn btn-primary !rounded-xl !px-3 !py-2 text-xs"
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

      <div className="section-container grid gap-8 py-8 lg:grid-cols-[240px_1fr]">
        <aside className="space-y-1">
          <nav className="flex flex-col gap-1" aria-label={t(language, 'adminNav')}>
            {links.map((item) => (
              <NavLink key={item.to} to={item.to} className={navClass}>
                <span aria-hidden="true">{item.icon}</span>
                {t(language, item.labelKey)}
              </NavLink>
            ))}
          </nav>
        </aside>
        <main>
          <Outlet />
        </main>
      </div>
    </div>
  );
}
