import { useState } from 'react';
import { useLanguage } from '../context/LanguageContext';
import { submitContactRequest } from '../api/client';
import { t } from '../i18n/ui';

const HERO_IMG = 'https://images.unsplash.com/photo-1426604966848-d7ad8d697227?auto=format&fit=crop&w=1920&q=80';

const CONTACT_ITEMS = [
  { icon: '📍', labelKey: 'contactAddressLabel', value: 'Kigali Eco Park, Kigali, Rwanda' },
  { icon: '🕐', labelKey: 'contactHoursTitle', valueKey: 'parkHours' },
  { icon: '📧', labelKey: 'contactEmailLabel', value: 'ateliernagaa@gmail.com', href: 'mailto:ateliernagaa@gmail.com' },
  { icon: '📞', labelKey: 'contactPhoneLabel', value: '+250 785 553 044', href: 'tel:+250785553044' },
];

const REQUEST_TYPES = [
  'QR_CODE_REQUEST',
  'TREE_INFORMATION',
  'GENERAL_INQUIRY',
  'PARTNERSHIP',
  'OTHER',
];

export default function ContactPage() {
  const { language } = useLanguage();
  const [sent, setSent] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    phone: '',
    requestType: 'GENERAL_INQUIRY',
    subject: '',
    treeName: '',
    message: '',
  });

  const showTreeName = form.requestType === 'QR_CODE_REQUEST' || form.requestType === 'TREE_INFORMATION';

  const handleChange = (field) => (e) => {
    setForm((prev) => ({ ...prev, [field]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await submitContactRequest({
        fullName: form.fullName.trim(),
        email: form.email.trim(),
        phone: form.phone.trim() || undefined,
        requestType: form.requestType,
        subject: form.subject.trim() || undefined,
        message: form.message.trim(),
        treeName: showTreeName ? form.treeName.trim() || undefined : undefined,
      });
      setSent(true);
    } catch (err) {
      setError(err.message || t(language, 'contactSubmitError'));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="bg-surface">
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
          <div className="space-y-4 lg:col-span-2">
            <h2 className="font-display text-xl font-bold text-primary-dark">
              {t(language, 'contactVisitTitle')}
            </h2>
            <p className="text-sm text-gray-600">{t(language, 'contactQrHint')}</p>
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

          <div className="lg:col-span-3">
            <div className="rounded-2xl bg-white p-6 shadow-card sm:p-8">
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
                  <button
                    type="button"
                    className="btn btn-secondary mt-6 !rounded-xl"
                    onClick={() => {
                      setSent(false);
                      setForm({
                        fullName: '',
                        email: '',
                        phone: '',
                        requestType: 'GENERAL_INQUIRY',
                        subject: '',
                        treeName: '',
                        message: '',
                      });
                    }}
                  >
                    {t(language, 'sendAnotherMessage')}
                  </button>
                </div>
              ) : (
                <form className="mt-8 space-y-4" onSubmit={handleSubmit}>
                  {error && (
                    <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700" role="alert">
                      {error}
                    </div>
                  )}

                  <div className="grid gap-4 sm:grid-cols-2">
                    <label className="block">
                      <span className="text-sm font-medium text-gray-700">{t(language, 'yourName')}</span>
                      <input
                        type="text"
                        required
                        value={form.fullName}
                        onChange={handleChange('fullName')}
                        autoComplete="name"
                        className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-base sm:text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                      />
                    </label>
                    <label className="block">
                      <span className="text-sm font-medium text-gray-700">{t(language, 'yourEmail')}</span>
                      <input
                        type="email"
                        required
                        value={form.email}
                        onChange={handleChange('email')}
                        autoComplete="email"
                        className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-base sm:text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                      />
                    </label>
                  </div>

                  <label className="block">
                    <span className="text-sm font-medium text-gray-700">
                      {t(language, 'yourPhone')}
                      <span className="ml-1 font-normal text-gray-400">({t(language, 'optional')})</span>
                    </span>
                    <input
                      type="tel"
                      value={form.phone}
                      onChange={handleChange('phone')}
                      autoComplete="tel"
                      className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-base sm:text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                    />
                  </label>

                  <label className="block">
                    <span className="text-sm font-medium text-gray-700">{t(language, 'requestType')}</span>
                    <select
                      required
                      value={form.requestType}
                      onChange={handleChange('requestType')}
                      className="mt-1.5 w-full rounded-xl border border-gray-300 bg-white px-4 py-3 text-base sm:text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                    >
                      {REQUEST_TYPES.map((type) => (
                        <option key={type} value={type}>
                          {t(language, `requestType_${type}`)}
                        </option>
                      ))}
                    </select>
                  </label>

                  {form.requestType === 'QR_CODE_REQUEST' && (
                    <p className="rounded-xl border border-primary/20 bg-primary/5 px-4 py-3 text-xs leading-relaxed text-primary-dark">
                      {t(language, 'contactQrRequestHint')}
                    </p>
                  )}

                  <label className="block">
                    <span className="text-sm font-medium text-gray-700">
                      {t(language, 'subject')}
                      <span className="ml-1 font-normal text-gray-400">({t(language, 'optional')})</span>
                    </span>
                    <input
                      type="text"
                      value={form.subject}
                      onChange={handleChange('subject')}
                      className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-base sm:text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                    />
                  </label>

                  {showTreeName && (
                    <label className="block">
                      <span className="text-sm font-medium text-gray-700">
                        {t(language, 'treeNameLabel')}
                        {form.requestType === 'QR_CODE_REQUEST' ? '' : (
                          <span className="ml-1 font-normal text-gray-400">({t(language, 'optional')})</span>
                        )}
                      </span>
                      <input
                        type="text"
                        required={form.requestType === 'QR_CODE_REQUEST'}
                        value={form.treeName}
                        onChange={handleChange('treeName')}
                        placeholder={t(language, 'treeNamePlaceholder')}
                        className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-base sm:text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                      />
                    </label>
                  )}

                  <label className="block">
                    <span className="text-sm font-medium text-gray-700">{t(language, 'yourMessage')}</span>
                    <textarea
                      rows={5}
                      required
                      minLength={10}
                      value={form.message}
                      onChange={handleChange('message')}
                      className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-base sm:text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                    />
                  </label>

                  <button
                    type="submit"
                    disabled={submitting}
                    className="btn btn-primary w-full !rounded-xl py-3.5 text-base sm:w-auto sm:px-10"
                  >
                    {submitting ? t(language, 'pleaseWait') : t(language, 'sendMessage')}
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
