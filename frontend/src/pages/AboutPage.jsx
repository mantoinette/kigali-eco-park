import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const HERO_IMG = 'https://images.unsplash.com/photo-1511497584788-876760111969?auto=format&fit=crop&w=1920&q=80';

const VALUES = [
  { icon: '🌿', titleKey: 'value1Title', textKey: 'value1Text' },
  { icon: '📱', titleKey: 'value2Title', textKey: 'value2Text' },
  { icon: '🌍', titleKey: 'value3Title', textKey: 'value3Text' },
];

const OFFERS = ['offer1', 'offer2', 'offer3', 'offer4'];

export default function AboutPage() {
  const { language } = useLanguage();

  return (
    <div className="bg-surface">
      {/* Hero */}
      <section className="relative flex min-h-[360px] items-end overflow-hidden sm:min-h-[420px]">
        <img src={HERO_IMG} alt="" className="absolute inset-0 h-full w-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-t from-primary-dark via-primary-dark/80 to-primary-dark/50" />
        <div className="section-container relative w-full pb-12 pt-28 sm:pb-16">
          <p className="text-xs font-semibold uppercase tracking-[0.25em] text-primary-light">
            {t(language, 'aboutHeroEyebrow')}
          </p>
          <h1 className="mt-3 max-w-3xl font-display text-4xl font-semibold text-white sm:text-5xl">
            {t(language, 'aboutPageTitle')}
          </h1>
          <p className="mt-4 max-w-2xl text-lg leading-relaxed text-white/90">
            {t(language, 'aboutPageIntro')}
          </p>
        </div>
      </section>

      {/* Mission + Vision */}
      <section className="section-container -mt-8 relative z-10 pb-16">
        <div className="grid gap-6 lg:grid-cols-2">
          <div className="rounded-2xl bg-white p-8 shadow-card">
            <span className="text-3xl" aria-hidden="true">🎯</span>
            <h2 className="mt-4 font-display text-2xl font-bold text-primary-dark">
              {t(language, 'ourMission')}
            </h2>
            <p className="mt-4 leading-relaxed text-gray-700">{t(language, 'missionText')}</p>
          </div>
          <div className="rounded-2xl border border-primary/20 bg-primary/5 p-8">
            <span className="text-3xl" aria-hidden="true">🔭</span>
            <h2 className="mt-4 font-display text-2xl font-bold text-primary-dark">
              {t(language, 'aboutVisionTitle')}
            </h2>
            <p className="mt-4 leading-relaxed text-gray-700">{t(language, 'aboutVisionText')}</p>
          </div>
        </div>
      </section>

      {/* Values */}
      <section className="border-y border-gray-200/80 bg-white py-16">
        <div className="section-container">
          <h2 className="text-center font-display text-3xl font-bold text-primary-dark">
            {t(language, 'aboutValuesTitle')}
          </h2>
          <div className="mt-10 grid gap-6 md:grid-cols-3">
            {VALUES.map((item) => (
              <article key={item.titleKey} className="rounded-2xl border border-gray-100 bg-surface p-6 text-center">
                <span className="text-4xl" aria-hidden="true">{item.icon}</span>
                <h3 className="mt-4 font-display text-lg font-semibold text-primary-dark">
                  {t(language, item.titleKey)}
                </h3>
                <p className="mt-2 text-sm leading-relaxed text-gray-600">
                  {t(language, item.textKey)}
                </p>
              </article>
            ))}
          </div>
        </div>
      </section>

      {/* What we offer */}
      <section className="section-container py-16">
        <h2 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'whatWeOffer')}</h2>
        <ul className="mt-8 grid gap-4 sm:grid-cols-2">
          {OFFERS.map((key) => (
            <li
              key={key}
              className="flex items-start gap-3 rounded-xl bg-white p-5 shadow-sm"
            >
              <span className="mt-0.5 flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-primary/15 text-sm text-primary">
                ✓
              </span>
              <span className="text-gray-700">{t(language, key)}</span>
            </li>
          ))}
        </ul>
      </section>

      {/* CTA */}
      <section className="bg-primary py-16 text-white">
        <div className="section-container text-center">
          <h2 className="font-display text-3xl font-bold">{t(language, 'aboutCtaTitle')}</h2>
          <p className="mx-auto mt-4 max-w-xl text-white/90">{t(language, 'aboutCtaText')}</p>
          <div className="mt-8 flex flex-wrap justify-center gap-4">
            <Link to="/trees" className="rounded-xl bg-white px-6 py-3 text-sm font-semibold text-primary-dark shadow-lg transition hover:bg-white/95">
              {t(language, 'plantlist')}
            </Link>
            <Link to="/register" className="rounded-xl border-2 border-white/80 px-6 py-3 text-sm font-semibold text-white transition hover:bg-white/10">
              {t(language, 'createAccount')}
            </Link>
            <Link to="/contact" className="rounded-xl border-2 border-white/80 px-6 py-3 text-sm font-semibold text-white transition hover:bg-white/10">
              {t(language, 'navContact')}
            </Link>
          </div>
        </div>
      </section>

      {/* Staff QR tools */}
      <section className="section-container py-12">
        <div className="rounded-2xl border border-dashed border-primary/30 bg-primary/5 p-6 sm:p-8">
          <h2 className="font-display text-xl font-bold text-primary-dark">{t(language, 'treeQrLabel')}</h2>
          <p className="mt-2 text-sm text-gray-600">{t(language, 'treeQrLabelText')}</p>
          <p className="mt-2 text-xs text-gray-500">{t(language, 'qrStaffOnly')}</p>
          <div className="mt-4 flex flex-wrap gap-3">
            <Link to="/qr-label/syzygium-guineense" className="btn btn-primary">
              Umugote (TREE-001)
            </Link>
            <Link to="/qr-label/ficus-ovata" className="btn btn-secondary">
              Umurehe (TREE-002)
            </Link>
            <Link to="/qr-label/aeschynomene-elaphroxylon" className="btn btn-secondary">
              Umuburu (TREE-003)
            </Link>
            <Link to="/qr-label/albizia-versicolor" className="btn btn-secondary">
              Umububa (TREE-004)
            </Link>
            <Link to="/qr-label/bambusa-vulgaris" className="btn btn-secondary">
              Umugano (TREE-005)
            </Link>
            <Link to="/qr-label/erythrina-abyssinica" className="btn btn-secondary">
              Umuko (TREE-006)
            </Link>
            <Link to="/qr-label/olea-europaea-subsp-africana" className="btn btn-secondary">
              Umunzenze (TREE-007)
            </Link>
            <Link to="/qr-label/senegalia-polyacantha-campylacantha" className="btn btn-secondary">
              Umuharata (TREE-008)
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}
