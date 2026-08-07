import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import CountUp from './CountUp';

export default function HomeStats({ treeCount }) {
  const { language } = useLanguage();
  const numericCount = Number(treeCount);

  const stats = [
    {
      value: Number.isFinite(numericCount) ? numericCount : null,
      label: t(language, 'statTrees'),
      fallback: '—',
    },
    { value: 3, label: t(language, 'statLanguages'), suffix: '+' },
    { value: null, label: t(language, 'statAccess'), fallback: '24/7' },
  ];

  return (
    <section className="home-stats" aria-label={t(language, 'parkHighlights')}>
      <div className="section-container">
        <div className="stats-grid">
          {stats.map((stat) => (
            <div key={stat.label} className="stat-card">
              {stat.value == null ? (
                <span className="stat-value">{stat.fallback}</span>
              ) : (
                <CountUp
                  value={stat.value}
                  language={language}
                  suffix={stat.suffix || ''}
                  className="stat-value"
                />
              )}
              <span className="stat-label">{stat.label}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
