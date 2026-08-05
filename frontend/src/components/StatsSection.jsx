import { useEffect, useState } from 'react';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { fetchSiteStats } from '../api/client';

const STAT_ICONS = ['🌳', '📚', '👨‍👩‍👧‍👦', '🌍'];

export default function StatsSection() {
  const { language } = useLanguage();
  const [stats, setStats] = useState(null);

  useEffect(() => {
    fetchSiteStats()
      .then(setStats)
      .catch(() => setStats(null));
  }, []);

  const items = stats
    ? [
        { value: stats.treesGoal, label: t(language, 'statTreesLabel') },
        { value: stats.speciesGoal, label: t(language, 'statSpeciesLabel') },
        { value: stats.totalVisitors.toLocaleString(), label: t(language, 'statVisitorsLabel') },
        { value: stats.languagesSupported, label: t(language, 'statLanguagesLabel') },
      ]
    : [];

  if (!items.length) return null;

  return (
    <section className="bg-primary py-16 text-white">
      <div className="section-container">
        <div className="grid gap-8 sm:grid-cols-2 lg:grid-cols-4">
          {items.map((item, index) => (
            <div key={item.label} className="text-center">
              <div className="text-4xl" aria-hidden="true">{STAT_ICONS[index]}</div>
              <div className="mt-2 font-display text-4xl font-bold">{item.value}</div>
              <div className="mt-1 text-sm text-white/85">{item.label}</div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
