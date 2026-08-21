import { Navigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

/** Redirects non-admins to login. */
export function RequireAdmin({ children }) {
  const { user, isAuthenticated, loading } = useAuth();
  if (loading) return null;
  if (!isAuthenticated || user?.role !== 'ADMIN') {
    return <Navigate to="/login" replace />;
  }
  return children;
}

export function AdminHome() {
  const { user } = useAuth();
  const { language } = useLanguage();

  const links = [
    { key: 'adminDashboard', to: '/admin', icon: '📊' },
    { key: 'adminAddTree', to: '/admin/trees/new', icon: '➕' },
    { key: 'adminManageTrees', to: '/admin/trees', icon: '🌳' },
    { key: 'adminQrCodes', to: '/admin/qr', icon: '📱' },
    { key: 'adminLanguages', to: '/admin/languages', icon: '🌍' },
    { key: 'adminUsers', to: '/admin/users', icon: '👥' },
    { key: 'adminStats', to: '/admin/stats', icon: '📈' },
  ];

  return (
    <div className="bg-surface py-12">
      <div className="section-container">
        <div className="mb-8 flex flex-wrap items-center justify-between gap-4">
          <div>
            <h1 className="section-title">{t(language, 'adminTitle')}</h1>
            <p className="section-subtitle">{t(language, 'adminIntro')}</p>
          </div>
          <span className="rounded-full bg-primary/10 px-4 py-2 text-sm font-medium text-primary-dark">
            {user.fullName}
          </span>
        </div>

        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {links.map((item) => (
            <Link key={item.key} to={item.to} className="card flex items-center gap-4">
              <span className="text-3xl" aria-hidden="true">{item.icon}</span>
              <div>
                <h2 className="font-bold text-primary-dark">{t(language, item.key)}</h2>
                <p className="text-sm text-gray-600">{t(language, `${item.key}Desc`)}</p>
              </div>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}

export default function AdminDashboard() {
  return (
    <RequireAdmin>
      <AdminHome />
    </RequireAdmin>
  );
}
