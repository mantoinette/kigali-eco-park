import { useState } from 'react';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const HERO_IMG = 'https://images.unsplash.com/photo-1426604966848-d7ad8d697227?auto=format&fit=crop&w=1920&q=80';

const CONTACT_ITEMS = [
  { icon: '📍', labelKey: 'contactAddressLabel', value: 'Kigali Eco Park, Kigali, Rwanda' },
  { icon: '🕐', labelKey: 'contactHoursTitle', valueKey: 'parkHours' },
  { icon: '📧', labelKey: 'contactEmailLabel', value: 'ateliernagaa@gmail.com', href: 'mailto:ateliernagaa@gmail.com' },
  { icon: '📞', labelKey: 'contactPhoneLabel', value: '+250 785 553 044', href: 'tel:+250785553044' },
];

export default function ContactPage() {
  const { language } = useLanguage();
  const [sent, setSent] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setSent(true);
  };

  return (
    <div className="bg-surface">
      {/* Hero */}
      <section className="relative flex min-h-[280px] items-end overflow-hidden sm:min-h-[320px]">
        <img src={HERO_IMG} alt="" className="absolute inset-0 h-full w-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-t from-primary-dark via-primary-dark/75 to-primary/40" />
        <div className="section-container relative w-full pb-10 pt-24">
          <p className="text-xs font-semibold uppercase tracking-[0.25em] text-primary-light">
            {t(language, 'contactHeroEyebrow')}
          </p>
          <h1 className="mt-2 font-display text-4xl font-semibold text-white sm:text-5xl">
            {t(language, 'contactTitle')}
          </h1>
          <p className="mt-4 max-w-xl text-white/90">{t(language, 'contactIntro')}</p>
        </div>
      </section>

      <div className="section-container py-12 lg:py-16">
        <div className="grid gap-8 lg:grid-cols-5">
          {/* Contact cards */}
          <div className="space-y-4 lg:col-span-2">
            <h2 className="font-display text-xl font-bold text-primary-dark">
              {t(language, 'contactVisitTitle')}
            </h2>
            {CONTACT_ITEMS.map((item) => (
              <div
                key={item.labelKey}
                className="flex gap-4 rounded-2xl border border-gray-100 bg-white p-5 shadow-sm transition hover:shadow-md"
              >
                <span className="flex h-11 w-11 shrink-0 items-center justify-center rounded-xl bg-primary/10 text-xl">
                  {item.icon}
                </span>
                <div>
                  <p className="text-xs font-semibold uppercase tracking-wide text-gray-500">
                    {t(language, item.labelKey)}
                  </p>
                  {item.href ? (
                    <a href={item.href} className="mt-1 block font-medium text-primary-dark hover:text-primary">
                      {item.value}
                    </a>
                  ) : (
                    <p className="mt-1 font-medium text-gray-800">
                      {item.valueKey ? t(language, item.valueKey) : item.value}
                    </p>
                  )}
                </div>
              </div>
            ))}
          </div>

          {/* Form */}
          <div className="lg:col-span-3">
            <div className="rounded-2xl bg-white p-8 shadow-card">
              <h2 className="font-display text-2xl font-bold text-primary-dark">
                {t(language, 'sendMessage')}
              </h2>
              <p className="mt-2 text-sm text-gray-600">{t(language, 'contactFormIntro')}</p>

              {sent ? (
                <div className="mt-8 rounded-xl border border-primary/20 bg-primary/5 px-6 py-8 text-center">
                  <span className="text-4xl" aria-hidden="true">✉️</span>
                  <p className="mt-4 font-medium text-primary-dark">
                    {t(language, 'contactMessageThanks')}
                  </p>
                  <p className="mt-2 text-sm text-gray-600">{t(language, 'contactFormIntro')}</p>
                </div>
              ) : (
                <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
                  <div className="grid gap-4 sm:grid-cols-2">
                    <label className="block">
                      <span className="text-sm font-medium text-gray-700">{t(language, 'yourName')}</span>
                      <input
                        type="text"
                        required
                        className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                      />
                    </label>
                    <label className="block">
                      <span className="text-sm font-medium text-gray-700">{t(language, 'yourEmail')}</span>
                      <input
                        type="email"
                        required
                        className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                      />
                    </label>
                  </div>
                  <label className="block">
                    <span className="text-sm font-medium text-gray-700">{t(language, 'yourMessage')}</span>
                    <textarea
                      rows={5}
                      required
                      className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                    />
                  </label>
                  <button type="submit" className="btn btn-primary w-full !rounded-xl py-3.5 sm:w-auto sm:px-10">
                    {t(language, 'sendMessage')}
                  </button>
                </form>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
