import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const CARDS = [
  { key: 'whyIdentify', icon: '🌳' },
  { key: 'whyEducational', icon: '📖' },
  { key: 'whyMultilingual', icon: '🌍' },
  { key: 'whyMobile', icon: '📱' },
];

export default function WhyUse() {
  const { language } = useLanguage();

  return (
    <section className="bg-white py-20">
      <div className="section-container">
        <div className="text-center">
          <h2 className="section-title">{t(language, 'whyUseTitle')}</h2>
          <p className="section-subtitle mx-auto">{t(language, 'whyUseIntro')}</p>
        </div>

        <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {CARDS.map(({ key, icon }) => (
            <article key={key} className="card text-center">
              <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-primary/10 text-3xl">
                {icon}
              </div>
              <h3 className="text-lg font-bold text-primary-dark">{t(language, `${key}Title`)}</h3>
              <p className="mt-2 text-sm leading-relaxed text-gray-600">{t(language, `${key}Text`)}</p>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
