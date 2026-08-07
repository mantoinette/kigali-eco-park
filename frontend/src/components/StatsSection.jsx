import { useEffect, useState } from 'react';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { fetchSiteStats } from '../api/client';

const FALLBACK = {
  treesGoal: 22000,
  speciesGoal: 45,
  totalVisitors: 5000,
  languagesSupported: 3,
};

function formatStat(value, language) {
  const n = Number(value);
  if (!Number.isFinite(n)) return value;
  const locale = language === 'fr' ? 'fr-FR' : language === 'rw' ? 'en-US' : 'en-US';
  return n.toLocaleString(locale);
}

export default function StatsSection() {
  const { language } = useLanguage();
  const [stats, setStats] = useState(FALLBACK);

  useEffect(() => {
    fetchSiteStats()
      .then((data) => {
        if (data) {
          setStats({
            treesGoal: data.treesGoal ?? FALLBACK.treesGoal,
            speciesGoal: data.speciesGoal ?? FALLBACK.speciesGoal,
            totalVisitors: data.totalVisitors ?? FALLBACK.totalVisitors,
            languagesSupported: data.languagesSupported ?? FALLBACK.languagesSupported,
          });
        }
      })
      .catch(() => setStats(FALLBACK));
  }, []);

  const items = [
    { value: formatStat(stats.treesGoal, language), label: t(language, 'statTreesLabel') },
    { value: formatStat(stats.speciesGoal, language), label: t(language, 'statSpeciesLabel') },
    { value: formatStat(stats.totalVisitors, language), label: t(language, 'statVisitorsLabel') },
    { value: formatStat(stats.languagesSupported, language), label: t(language, 'statLanguagesLabel') },
  ];

  return (
    <section className="relative overflow-hidden bg-primary-dark py-16 text-white sm:py-20" aria-label={t(language, 'parkHighlights')}>
      <div
        className="pointer-events-none absolute inset-0 opacity-30"
        style={{
          background:
            'radial-gradient(ellipse at 20% 0%, rgba(255,255,255,0.18), transparent 55%), radial-gradient(ellipse at 80% 100%, rgba(16,185,129,0.25), transparent 50%)',
        }}
        aria-hidden="true"
      />
      <div className="section-container relative">
        <div className="mx-auto max-w-3xl text-center">
          <p className="text-xs font-semibold uppercase tracking-[0.22em] text-emerald-200/90">
            {t(language, 'parkHighlights')}
          </p>
        </div>

        <div className="mt-10 grid gap-0 sm:grid-cols-2 lg:grid-cols-4 lg:divide-x lg:divide-white/15">
          {items.map((item) => (
            <div
              key={item.label}
              className="border-b border-white/10 px-4 py-8 text-center last:border-b-0 sm:border-b sm:odd:border-r sm:odd:border-white/10 lg:border-b-0 lg:border-r-0 lg:px-8"
            >
              <div className="font-display text-4xl font-semibold tracking-tight text-white sm:text-5xl">
                {item.value}
              </div>
              <div className="mt-3 text-sm font-medium uppercase tracking-[0.16em] text-white/70">
                {item.label}
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
