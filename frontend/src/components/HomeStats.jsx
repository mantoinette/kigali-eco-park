import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function HomeStats({ treeCount }) {
  const { language } = useLanguage();

  const stats = [
    { value: treeCount || '—', label: t(language, 'statTrees') },
    { value: '3+', label: t(language, 'statLanguages') },
    { value: '24/7', label: t(language, 'statAccess') },
  ];

  return (
    <section className="home-stats" aria-label={t(language, 'parkHighlights')}>
      <div className="section-container">
        <div className="stats-grid">
          {stats.map((stat) => (
            <div key={stat.label} className="stat-card">
              <span className="stat-value">{stat.value}</span>
              <span className="stat-label">{stat.label}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
