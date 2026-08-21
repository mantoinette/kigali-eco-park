import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

function IconPin({ className = 'h-4 w-4' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path
        d="M12 21s7-5.2 7-11a7 7 0 1 0-14 0c0 5.8 7 11 7 11Z"
        stroke="currentColor"
        strokeWidth="1.75"
      />
      <circle cx="12" cy="10" r="2.25" stroke="currentColor" strokeWidth="1.75" />
    </svg>
  );
}

function IconMail({ className = 'h-4 w-4' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <rect x="3.5" y="5.5" width="17" height="13" rx="2" stroke="currentColor" strokeWidth="1.75" />
      <path d="m5 8 7 5 7-5" stroke="currentColor" strokeWidth="1.75" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function IconPhone({ className = 'h-4 w-4' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path
        d="M8.5 4.5h3l1.2 3.2-1.8 1.2a11.5 11.5 0 0 0 4.2 4.2l1.2-1.8 3.2 1.2v3a1.8 1.8 0 0 1-1.9 1.8A14.8 14.8 0 0 1 4.7 6.4 1.8 1.8 0 0 1 6.5 4.5Z"
        stroke="currentColor"
        strokeWidth="1.75"
        strokeLinejoin="round"
      />
    </svg>
  );
}

function SocialLink({ href, label, children }) {
  return (
    <a
      href={href}
      target="_blank"
      rel="noreferrer"
      aria-label={label}
      className="inline-flex h-10 w-10 items-center justify-center rounded-full border border-white/20 text-white/85 transition hover:border-white/45 hover:bg-white/10 hover:text-white"
    >
      {children}
    </a>
  );
}

export default function Footer() {
  const { language } = useLanguage();
  const year = new Date().getFullYear();

  const exploreLinks = [
    { to: '/', label: t(language, 'navHome') },
    { to: '/trees', label: t(language, 'exploreTrees') },
    { to: '/map', label: t(language, 'navMap') },
    { to: '/about', label: t(language, 'aboutUs') },
  ];

  const visitorLinks = [
    { to: '/contact', label: t(language, 'navContact') },
    { to: '/privacy', label: t(language, 'privacyPolicy') },
    { to: '/qr-label/syzygium-guineense', label: t(language, 'openQrLabel') },
  ];

  return (
    <footer className="site-footer no-print mt-auto bg-[#143d18] text-white">
      <div className="border-t border-white/10 bg-gradient-to-b from-[#1b5e20] to-[#143d18]">
        <div className="section-container py-14 sm:py-16">
          <div className="grid gap-12 lg:grid-cols-[1.35fr_1fr_1fr_1fr]">
            <div className="max-w-sm">
              <Link to="/" className="inline-block transition-opacity hover:opacity-90">
                <img
                  src="/treescan-rwanda-logo.png"
                  alt={t(language, 'siteName')}
                  className="h-14 w-auto max-w-[240px] object-contain object-left brightness-0 invert drop-shadow-sm sm:h-16"
                />
              </Link>
              <p className="mt-5 text-sm leading-relaxed text-white/75">
                {t(language, 'footerTagline')}
              </p>
              <p className="mt-4 text-xs leading-relaxed text-white/55">
                {t(language, 'footerMission')}
              </p>
            </div>

            <div>
              <h3 className="text-xs font-semibold uppercase tracking-[0.18em] text-emerald-200/90">
                {t(language, 'footerExplore')}
              </h3>
              <ul className="mt-5 space-y-3 text-sm">
                {exploreLinks.map((item) => (
                  <li key={item.to}>
                    <Link
                      to={item.to}
                      className="text-white/75 transition hover:text-white"
                    >
                      {item.label}
                    </Link>
                  </li>
                ))}
              </ul>
            </div>

            <div>
              <h3 className="text-xs font-semibold uppercase tracking-[0.18em] text-emerald-200/90">
                {t(language, 'footerVisit')}
              </h3>
              <ul className="mt-5 space-y-4 text-sm text-white/75">
                <li className="flex gap-3">
                  <span className="mt-0.5 text-emerald-200/80">
                    <IconPin />
                  </span>
                  <span>{t(language, 'footerAddress')}</span>
                </li>
                <li className="flex gap-3">
                  <span className="mt-0.5 text-emerald-200/80">
                    <IconMail />
                  </span>
                  <a href="mailto:ateliernagaa@gmail.com" className="transition hover:text-white">
                    ateliernagaa@gmail.com
                  </a>
                </li>
                <li className="flex gap-3">
                  <span className="mt-0.5 text-emerald-200/80">
                    <IconPhone />
                  </span>
                  <a href="tel:+250785553044" className="transition hover:text-white">
                    +250 785 553 044
                  </a>
                </li>
              </ul>
            </div>

            <div>
              <h3 className="text-xs font-semibold uppercase tracking-[0.18em] text-emerald-200/90">
                {t(language, 'footerVisitor')}
              </h3>
              <ul className="mt-5 space-y-3 text-sm">
                {visitorLinks.map((item) => (
                  <li key={item.to}>
                    <Link
                      to={item.to}
                      className="text-white/75 transition hover:text-white"
                    >
                      {item.label}
                    </Link>
                  </li>
                ))}
              </ul>

              <p className="mt-8 text-xs font-semibold uppercase tracking-[0.18em] text-emerald-200/90">
                {t(language, 'footerFollow')}
              </p>
              <div className="mt-4 flex gap-3">
                <SocialLink href="https://facebook.com" label="Facebook">
                  <svg className="h-4 w-4" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                    <path d="M14 9h3V6h-3c-2.2 0-4 1.8-4 4v2H7v3h3v7h3v-7h3l1-3h-4V10c0-.6.4-1 1-1Z" />
                  </svg>
                </SocialLink>
                <SocialLink href="https://instagram.com" label="Instagram">
                  <svg className="h-4 w-4" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <rect x="3.5" y="3.5" width="17" height="17" rx="5" stroke="currentColor" strokeWidth="1.75" />
                    <circle cx="12" cy="12" r="3.75" stroke="currentColor" strokeWidth="1.75" />
                    <circle cx="17.2" cy="6.8" r="1" fill="currentColor" />
                  </svg>
                </SocialLink>
                <SocialLink href="https://twitter.com" label="X">
                  <svg className="h-4 w-4" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                    <path d="M4 4h4.2l4.1 5.7L17.2 4H20l-6.1 7.3L20.5 20H16.3l-4.5-6.2L6.5 20H4l6.4-7.7L4 4Z" />
                  </svg>
                </SocialLink>
              </div>
            </div>
          </div>
        </div>

        <div className="border-t border-white/10">
          <div className="section-container flex flex-col gap-3 py-5 text-center text-xs text-white/55 sm:flex-row sm:items-center sm:justify-between sm:text-left">
            <p>
              © {year} {t(language, 'siteName')}. {t(language, 'allRightsReserved')}
            </p>
            <div className="flex flex-col items-center gap-1 sm:items-end">
              <p className="tracking-wide text-white/45">{t(language, 'footerGuided')}</p>
              <p className="font-medium text-white/60">{t(language, 'footerDevelopedBy')}</p>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}
