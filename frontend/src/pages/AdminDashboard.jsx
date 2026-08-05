import { Navigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const ADMIN_LINKS = [
  { key: 'adminDashboard', to: '/admin', icon: '📊' },
  { key: 'adminAddTree', to: '/admin/trees/new', icon: '➕' },
  { key: 'adminManageTrees', to: '/admin/trees', icon: '🌳' },
  { key: 'adminQrCodes', to: '/admin/qr', icon: '📱' },
  { key: 'adminLanguages', to: '/admin/languages', icon: '🌍' },
  { key: 'adminUsers', to: '/admin/users', icon: '👥' },
  { key: 'adminStats', to: '/admin/stats', icon: '📈' },
];

export default function AdminDashboard() {
  const { user, isAuthenticated, loading } = useAuth();
  const { language } = useLanguage();

  if (loading) return null;
  if (!isAuthenticated || user?.role !== 'ADMIN') {
    return <Navigate to="/login" replace />;
  }

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
          {ADMIN_LINKS.map((item) => (
            <Link key={item.key} to={item.to} className="card flex items-center gap-4">
              <span className="text-3xl" aria-hidden="true">{item.icon}</span>
              <div>
                <h2 className="font-bold text-primary-dark">{t(language, item.key)}</h2>
                <p className="text-sm text-gray-600">{t(language, `${item.key}Desc`)}</p>
              </div>
            </Link>
          ))}
        </div>

        <div className="mt-10 rounded-2xl border border-amber-200 bg-amber-50 p-5 text-sm text-amber-900">
          {t(language, 'adminComingSoon')}
        </div>
      </div>
    </div>
  );
}
