import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchContactRequests, fetchContactStats } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const HUB_CARDS = [
  { key: 'adminDashboard', descKey: 'adminDashboardDesc', to: '/admin/dashboard', icon: '📊' },
  { key: 'adminAddTree', descKey: 'adminAddTreeDesc', to: '/admin/trees/new', icon: '➕' },
  { key: 'adminManageTrees', descKey: 'adminManageTreesDesc', to: '/admin/trees', icon: '🌳' },
  { key: 'adminQrCodes', descKey: 'adminQrCodesDesc', to: '/admin/qr', icon: '📱' },
  { key: 'adminLanguages', descKey: 'adminLanguagesDesc', to: '/admin/languages', icon: '🌐' },
  { key: 'adminUsers', descKey: 'adminUsersDesc', to: '/admin/users', icon: '👥' },
  { key: 'adminStats', descKey: 'adminStatsDesc', to: '/admin/stats', icon: '📈' },
  { key: 'adminMessages', descKey: 'adminMessagesDesc', to: '/admin/requests', icon: '✉️' },
];

function formatDate(iso, language) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString(language === 'rw' ? 'rw-RW' : language === 'fr' ? 'fr-FR' : 'en-GB', {
    dateStyle: 'medium',
    timeStyle: 'short',
  });
}

function statusBadgeClass(status) {
  if (status === 'NEW') return 'bg-blue-100 text-blue-800';
  if (status === 'IN_PROGRESS') return 'bg-amber-100 text-amber-900';
  return 'bg-green-100 text-green-800';
}

export default function AdminDashboardHome() {
  const { language } = useLanguage();
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [recent, setRecent] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([
      fetchContactStats(user.token),
      fetchContactRequests(user.token),
    ])
      .then(([summary, list]) => {
        setStats(summary);
        setRecent((list || []).slice(0, 6));
      })
      .catch((err) => setError(err.message || 'Failed to load dashboard'))
      .finally(() => setLoading(false));
  }, [user.token]);

  if (loading) return <LoadingSpinner />;

  const statCards = stats
    ? [
        { label: t(language, 'adminMessagesTotal'), value: stats.total, to: '/admin/requests' },
        { label: t(language, 'statusNew'), value: stats.newCount, to: '/admin/requests?status=NEW' },
        { label: t(language, 'statusInProgress'), value: stats.inProgressCount, to: '/admin/requests?status=IN_PROGRESS' },
        { label: t(language, 'statusResolved'), value: stats.resolvedCount, to: '/admin/requests?status=RESOLVED' },
        { label: t(language, 'adminQrRequests'), value: stats.qrRequestCount, to: '/admin/requests?type=QR_CODE_REQUEST' },
      ]
    : [];

  return (
    <div>
      <div className="mb-8">
        <h1 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'adminDashboard')}</h1>
        <p className="mt-2 text-sm text-gray-600">{t(language, 'adminIntro')}</p>
      </div>

      <div className="mb-10">
        <h2 className="text-sm font-semibold uppercase tracking-wide text-gray-500">{t(language, 'adminHubTitle')}</h2>
        <div className="mt-4 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {HUB_CARDS.map((card) => (
            <Link
              key={card.to}
              to={card.to}
              className="rounded-2xl border border-gray-200 bg-white p-5 shadow-sm transition hover:border-primary/30 hover:shadow-md"
            >
              <span className="text-2xl" aria-hidden="true">{card.icon}</span>
              <h3 className="mt-3 font-display text-lg font-bold text-primary-dark">{t(language, card.key)}</h3>
              <p className="mt-1 text-sm text-gray-600">{t(language, card.descKey)}</p>
            </Link>
          ))}
        </div>
      </div>

      <div className="mb-8">
        <h2 className="font-display text-xl font-bold text-primary-dark">{t(language, 'adminDashboardOverview')}</h2>
      </div>

      {error && (
        <p className="mb-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
      )}

      <div className="mb-8 grid gap-4 sm:grid-cols-2 xl:grid-cols-5">
        {statCards.map((card) => (
          <Link
            key={card.label}
            to={card.to}
            className="rounded-2xl border border-gray-200 bg-white p-5 shadow-sm transition hover:border-primary/30 hover:shadow-md"
          >
            <p className="text-xs font-semibold uppercase tracking-wide text-gray-500">{card.label}</p>
            <p className="mt-2 text-3xl font-bold text-primary-dark">{card.value}</p>
          </Link>
        ))}
      </div>

      <div className="rounded-2xl border border-gray-200 bg-white shadow-sm">
        <div className="flex flex-wrap items-center justify-between gap-3 border-b border-gray-100 px-5 py-4">
          <h2 className="font-display text-xl font-bold text-primary-dark">{t(language, 'recentMessages')}</h2>
          <Link to="/admin/requests" className="text-sm font-semibold text-primary hover:underline">
            {t(language, 'viewAllRequests')}
          </Link>
        </div>

        {recent.length === 0 ? (
          <p className="px-5 py-12 text-center text-sm text-gray-500">{t(language, 'noMessagesYet')}</p>
        ) : (
          <ul className="divide-y divide-gray-100">
            {recent.map((msg) => (
              <li key={msg.id}>
                <Link
                  to={`/admin/requests?id=${msg.id}`}
                  className="flex flex-wrap items-start justify-between gap-3 px-5 py-4 transition hover:bg-primary/5"
                >
                  <div>
                    <p className="font-semibold text-primary-dark">{msg.fullName}</p>
                    <p className="text-sm text-gray-600">
                      {t(language, `requestType_${msg.requestType}`)}
                      {msg.treeName ? ` · ${msg.treeName}` : ''}
                    </p>
                    <p className="mt-1 line-clamp-1 text-xs text-gray-500">{msg.message}</p>
                  </div>
                  <div className="text-right">
                    <span className={`inline-block rounded-full px-2.5 py-0.5 text-xs font-semibold ${statusBadgeClass(msg.status)}`}>
                      {t(language, `status_${msg.status}`)}
                    </span>
                    <p className="mt-2 text-xs text-gray-400">{formatDate(msg.createdAt, language)}</p>
                  </div>
                </Link>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
