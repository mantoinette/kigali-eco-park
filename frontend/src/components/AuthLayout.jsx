import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const HERO_IMG = 'https://images.unsplash.com/photo-1542601906990-b46d7cb70d06?auto=format&fit=crop&w=1920&q=80';

const BENEFITS = ['authBenefit1', 'authBenefit2', 'authBenefit3'];

export default function AuthLayout({ title, subtitle, footer, children }) {
  const { language } = useLanguage();

  return (
    <div className="min-h-[calc(100vh-4rem)] bg-surface lg:grid lg:grid-cols-2">
      {/* Brand panel */}
      <div className="relative hidden overflow-hidden lg:flex lg:flex-col lg:justify-between">
        <img src={HERO_IMG} alt="" className="absolute inset-0 h-full w-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-br from-primary-dark/95 via-primary-dark/85 to-primary/70" />
        <div className="relative flex flex-1 flex-col justify-center p-12 xl:p-16">
          <Link to="/" className="mb-8 inline-flex items-center text-white/90 transition hover:opacity-90">
            <img
              src="/kigali-eco-park-logo.png"
              alt="Kigali Eco-Park"
              className="h-14 w-auto object-contain"
            />
          </Link>
          <h1 className="font-display text-3xl font-bold leading-tight text-white xl:text-4xl">
            {t(language, 'authHeroTitle')}
          </h1>
          <p className="mt-4 max-w-md text-lg text-white/85">{t(language, 'authHeroSubtitle')}</p>
          <ul className="mt-10 space-y-4">
            {BENEFITS.map((key) => (
              <li key={key} className="flex items-start gap-3 text-white/90">
                <span className="mt-1 flex h-5 w-5 shrink-0 items-center justify-center rounded-full bg-primary-light/30 text-xs">✓</span>
                <span className="text-sm leading-relaxed">{t(language, key)}</span>
              </li>
            ))}
          </ul>
        </div>
        <p className="relative p-8 text-xs text-white/50">© {new Date().getFullYear()} Kigali Eco-Park</p>
      </div>

      {/* Form panel */}
      <div className="flex items-center justify-center px-4 py-12 sm:px-8">
        <div className="w-full max-w-md">
          <div className="mb-8 text-center lg:text-left">
            <img
              src="/kigali-eco-park-brand.png"
              alt="Kigali Eco-Park"
              className="mx-auto h-14 w-auto object-contain lg:mx-0"
            />
            <h2 className="mt-3 font-display text-2xl font-bold text-primary-dark sm:text-3xl">{title}</h2>
            <p className="mt-2 text-gray-600">{subtitle}</p>
          </div>
          <div className="rounded-2xl border border-gray-100 bg-white p-8 shadow-card">
            {children}
          </div>
          {footer && <div className="mt-6 text-center text-sm text-gray-600">{footer}</div>}
        </div>
      </div>
    </div>
  );
}
