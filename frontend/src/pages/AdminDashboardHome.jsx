import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchContactRequests, fetchContactStats, fetchSiteStats } from '../api/client';
import { ADMIN_ICONS, IconChevronRight, IconPlus } from '../components/admin/AdminIcons';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const QUICK_ACTIONS = [
  { key: 'adminAddTree', descKey: 'adminAddTreeDesc', to: '/admin/trees/new', icon: 'plus', accent: 'bg-emerald-600' },
  { key: 'adminManageTrees', descKey: 'adminManageTreesDesc', to: '/admin/trees', icon: 'tree', accent: 'bg-primary' },
  { key: 'adminQrCodes', descKey: 'adminQrCodesDesc', to: '/admin/qr', icon: 'qr', accent: 'bg-teal-700' },
  { key: 'adminMessages', descKey: 'adminMessagesDesc', to: '/admin/requests', icon: 'mail', accent: 'bg-sky-700' },
  { key: 'adminLanguages', descKey: 'adminLanguagesDesc', to: '/admin/languages', icon: 'globe', accent: 'bg-indigo-700' },
  { key: 'adminUsers', descKey: 'adminUsersDesc', to: '/admin/users', icon: 'users', accent: 'bg-violet-700' },
  { key: 'adminStats', descKey: 'adminStatsDesc', to: '/admin/stats', icon: 'chart', accent: 'bg-amber-700' },
];

function formatDate(iso, language) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString(language === 'rw' ? 'rw-RW' : language === 'fr' ? 'fr-FR' : 'en-GB', {
    dateStyle: 'medium',
    timeStyle: 'short',
  });
}

function statusBadgeClass(status) {
  if (status === 'NEW') return 'bg-blue-50 text-blue-700 ring-1 ring-blue-200';
  if (status === 'IN_PROGRESS') return 'bg-amber-50 text-amber-800 ring-1 ring-amber-200';
  return 'bg-emerald-50 text-emerald-800 ring-1 ring-emerald-200';
}

function KpiCard({ label, value, to, icon: iconKey, tone }) {
  const Icon = ADMIN_ICONS[iconKey];
  const tones = {
    green: 'from-primary/10 to-primary/5 text-primary-dark',
    blue: 'from-blue-50 to-white text-blue-900',
    amber: 'from-amber-50 to-white text-amber-900',
    slate: 'from-gray-50 to-white text-gray-900',
  };

  const content = (
    <>
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-xs font-semibold uppercase tracking-wide text-gray-500">{label}</p>
          <p className="mt-2 text-3xl font-bold tabular-nums">{value ?? '—'}</p>
        </div>
        {Icon && (
          <span className={`flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br ${tones[tone]} shadow-sm`}>
            <Icon className="h-5 w-5" />
          </span>
        )}
      </div>
    </>
  );

  const className = 'block rounded-2xl border border-gray-200/80 bg-white p-5 shadow-card transition hover:-translate-y-0.5 hover:border-primary/20 hover:shadow-lg';

  return to ? <Link to={to} className={className}>{content}</Link> : <div className={className}>{content}</div>;
}

export default function AdminDashboardHome() {
  const { language } = useLanguage();
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [siteStats, setSiteStats] = useState(null);
  const [recent, setRecent] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([
      fetchContactStats(user.token),
      fetchContactRequests(user.token),
      fetchSiteStats(),
    ])
      .then(([summary, list, site]) => {
        setStats(summary);
        setSiteStats(site);
        setRecent((list || []).slice(0, 5));
      })
      .catch((err) => setError(err.message || 'Failed to load dashboard'))
      .finally(() => setLoading(false));
  }, [user.token]);

  if (loading) return <LoadingSpinner />;

  const today = new Date().toLocaleDateString(language === 'rw' ? 'rw-RW' : language === 'fr' ? 'fr-FR' : 'en-GB', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  return (
    <div className="space-y-8">
      <section className="overflow-hidden rounded-2xl border border-primary/15 bg-gradient-to-br from-primary-dark via-primary to-primary-light/80 p-6 text-white shadow-card sm:p-8">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div>
            <p className="text-sm font-medium text-white/75">{today}</p>
            <h1 className="mt-1 font-display text-2xl font-bold sm:text-3xl">
              {t(language, 'adminWelcomeBack', { name: user?.fullName?.split(' ')[0] || 'Admin' })}
            </h1>
            <p className="mt-2 max-w-xl text-sm text-white/85">{t(language, 'adminIntro')}</p>
          </div>
          <Link
            to="/admin/trees/new"
            className="inline-flex items-center gap-2 rounded-xl bg-white px-4 py-2.5 text-sm font-semibold text-primary-dark shadow-md transition hover:bg-white/95"
          >
            <IconPlus className="h-4 w-4" />
            {t(language, 'adminAddTree')}
          </Link>
        </div>
      </section>

      {error && (
        <p className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800" role="alert">
          {error}
        </p>
      )}

      <section>
        <h2 className="text-sm font-semibold uppercase tracking-wide text-gray-500">{t(language, 'adminTodayOverview')}</h2>
        <div className="mt-4 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          <KpiCard
            label={t(language, 'treesDocumented')}
            value={siteStats?.treesDocumented}
            to="/admin/trees"
            icon="tree"
            tone="green"
          />
          <KpiCard
            label={t(language, 'adminMessagesTotal')}
            value={stats?.total}
            to="/admin/requests"
            icon="mail"
            tone="blue"
          />
          <KpiCard
            label={t(language, 'statusNew')}
            value={stats?.newCount}
            to="/admin/requests?status=NEW"
            icon="mail"
            tone="amber"
          />
          <KpiCard
            label={t(language, 'adminQrRequests')}
            value={stats?.qrRequestCount}
            to="/admin/requests?type=QR_CODE_REQUEST"
            icon="qr"
            tone="slate"
          />
        </div>
      </section>

      <div className="grid gap-6 xl:grid-cols-[1.1fr_1fr]">
        <section className="rounded-2xl border border-gray-200/80 bg-white shadow-card">
          <div className="border-b border-gray-100 px-5 py-4">
            <h2 className="font-display text-lg font-bold text-primary-dark">{t(language, 'adminQuickActions')}</h2>
            <p className="mt-1 text-sm text-gray-500">{t(language, 'adminHubTitle')}</p>
          </div>
          <ul className="divide-y divide-gray-100">
            {QUICK_ACTIONS.map((action) => {
              const Icon = ADMIN_ICONS[action.icon];
              return (
                <li key={action.to}>
                  <Link
                    to={action.to}
                    className="group flex items-center gap-4 px-5 py-4 transition hover:bg-primary/[0.03]"
                  >
                    <span className={`flex h-10 w-10 shrink-0 items-center justify-center rounded-xl text-white ${action.accent}`}>
                      {Icon && <Icon className="h-5 w-5" />}
                    </span>
                    <div className="min-w-0 flex-1">
                      <p className="font-semibold text-gray-900 group-hover:text-primary-dark">{t(language, action.key)}</p>
                      <p className="text-sm text-gray-500">{t(language, action.descKey)}</p>
                    </div>
                    <IconChevronRight className="text-gray-300 transition group-hover:text-primary" />
                  </Link>
                </li>
              );
            })}
          </ul>
        </section>

        <section className="rounded-2xl border border-gray-200/80 bg-white shadow-card">
          <div className="flex flex-wrap items-center justify-between gap-3 border-b border-gray-100 px-5 py-4">
            <div>
              <h2 className="font-display text-lg font-bold text-primary-dark">{t(language, 'recentMessages')}</h2>
              <p className="text-sm text-gray-500">{t(language, 'adminDashboardOverview')}</p>
            </div>
            <Link
              to="/admin/requests"
              className="text-sm font-semibold text-primary hover:underline"
            >
              {t(language, 'viewAllRequests')}
            </Link>
          </div>

          {recent.length === 0 ? (
            <p className="px-5 py-16 text-center text-sm text-gray-500">{t(language, 'noMessagesYet')}</p>
          ) : (
            <ul className="divide-y divide-gray-100">
              {recent.map((msg) => (
                <li key={msg.id}>
                  <Link
                    to={`/admin/requests?id=${msg.id}`}
                    className="block px-5 py-4 transition hover:bg-primary/[0.03]"
                  >
                    <div className="flex items-start justify-between gap-3">
                      <div className="min-w-0">
                        <p className="truncate font-semibold text-gray-900">{msg.fullName}</p>
                        <p className="mt-0.5 text-sm text-gray-600">
                          {t(language, `requestType_${msg.requestType}`)}
                          {msg.treeName ? ` · ${msg.treeName}` : ''}
                        </p>
                      </div>
                      <span className={`shrink-0 rounded-full px-2.5 py-0.5 text-xs font-semibold ${statusBadgeClass(msg.status)}`}>
                        {t(language, `status_${msg.status}`)}
                      </span>
                    </div>
                    <p className="mt-2 line-clamp-2 text-sm text-gray-500">{msg.message}</p>
                    <p className="mt-2 text-xs text-gray-400">{formatDate(msg.createdAt, language)}</p>
                  </Link>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </div>
  );
}
