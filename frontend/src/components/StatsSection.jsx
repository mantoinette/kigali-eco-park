import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import CountUp from './CountUp';

/** Official park highlight figures (shown on the home page). */
const PARK_STATS = {
  trees: 22000,
  species: 45,
  visitors: 5000,
  languages: 3,
};

export default function StatsSection() {
  const { language } = useLanguage();

  const items = [
    { value: PARK_STATS.trees, label: t(language, 'statTreesLabel'), duration: 2200 },
    { value: PARK_STATS.species, label: t(language, 'statSpeciesLabel'), duration: 1400 },
    { value: PARK_STATS.visitors, label: t(language, 'statVisitorsLabel'), duration: 1800 },
    { value: PARK_STATS.languages, label: t(language, 'statLanguagesLabel'), duration: 1000 },
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
              <CountUp
                value={item.value}
                language={language}
                duration={item.duration}
                suffix="+"
                className="block font-display text-4xl font-semibold tracking-tight text-white sm:text-5xl"
              />
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
