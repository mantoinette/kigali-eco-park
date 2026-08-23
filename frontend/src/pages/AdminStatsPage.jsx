import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchContactStats, fetchSiteStats } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function AdminStatsPage() {
  const { language } = useLanguage();
  const { user } = useAuth();
  const [siteStats, setSiteStats] = useState(null);
  const [contactStats, setContactStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    Promise.all([fetchSiteStats(), fetchContactStats(user.token)])
      .then(([site, contact]) => {
        setSiteStats(site);
        setContactStats(contact);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [user.token]);

  if (loading) return <LoadingSpinner />;

  const cards = [
    { label: t(language, 'treesDocumented'), value: siteStats?.treesDocumented ?? '—' },
    { label: t(language, 'speciesDocumented'), value: siteStats?.speciesDocumented ?? '—' },
    { label: t(language, 'languagesSupported'), value: siteStats?.languagesSupported ?? '—' },
    { label: t(language, 'adminMessagesTotal'), value: contactStats?.total ?? '—', to: '/admin/requests' },
    { label: t(language, 'adminQrRequests'), value: contactStats?.qrRequestCount ?? '—', to: '/admin/requests?type=QR_CODE_REQUEST' },
    { label: t(language, 'statusResolved'), value: contactStats?.resolvedCount ?? '—', to: '/admin/requests?status=RESOLVED' },
  ];

  return (
    <div>
      <h1 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'adminStats')}</h1>
      <p className="mt-2 text-sm text-gray-600">{t(language, 'adminStatsDesc')}</p>

      {error && (
        <p className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
      )}

      <div className="mt-8 grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
        {cards.map((card) => {
          const inner = (
            <>
              <p className="text-xs font-semibold uppercase tracking-wide text-gray-500">{card.label}</p>
              <p className="mt-2 text-3xl font-bold text-primary-dark">{card.value}</p>
            </>
          );
          return card.to ? (
            <Link
              key={card.label}
              to={card.to}
              className="rounded-2xl border border-gray-200 bg-white p-5 shadow-sm transition hover:border-primary/30 hover:shadow-md"
            >
              {inner}
            </Link>
          ) : (
            <div key={card.label} className="rounded-2xl border border-gray-200 bg-white p-5 shadow-sm">
              {inner}
            </div>
          );
        })}
      </div>
    </div>
  );
}
