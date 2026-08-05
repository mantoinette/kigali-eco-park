import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const HERO_IMAGE = 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=1920&q=80';

export default function HeroSection() {
  const { language } = useLanguage();

  return (
    <section className="relative flex min-h-[88vh] items-center overflow-hidden">
      <img
        src={HERO_IMAGE}
        alt=""
        className="absolute inset-0 h-full w-full object-cover"
      />
      <div className="absolute inset-0 bg-gradient-to-r from-primary-dark/90 via-primary-dark/70 to-primary/40" />

      <div className="section-container relative z-10 py-20 text-white">
        <p className="mb-4 inline-flex items-center gap-2 rounded-full border border-white/30 bg-white/10 px-4 py-1.5 text-sm backdrop-blur-sm">
          <span aria-hidden="true">🌳</span>
          {t(language, 'heroBadge')}
        </p>
        <h1 className="max-w-3xl font-display text-4xl font-bold leading-tight sm:text-5xl lg:text-6xl">
          {t(language, 'welcome')}
        </h1>
        <p className="mt-6 max-w-2xl text-lg text-white/90 sm:text-xl">
          {t(language, 'heroSubtitleNew')}
        </p>
        <p className="mt-4 max-w-2xl rounded-xl border border-white/20 bg-white/10 px-4 py-3 text-sm text-white/90 backdrop-blur-sm">
          <span className="mr-2" aria-hidden="true">📱</span>
          {t(language, 'scanAtParkHero')}
        </p>
        <div className="mt-10 flex flex-wrap gap-4">
          <Link to="/trees" className="btn btn-primary">
            {t(language, 'exploreTrees')}
          </Link>
          <Link to="/faq" className="btn btn-outline">
            {t(language, 'howItWorks')}
          </Link>
        </div>
      </div>
    </section>
  );
}
