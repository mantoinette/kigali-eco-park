import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function AdminSettingsPage() {
  const { language } = useLanguage();

  return (
    <div>
      <h1 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'adminSettings')}</h1>
      <p className="mt-2 text-sm text-gray-600">{t(language, 'adminSettingsDesc')}</p>
      <div className="mt-8 rounded-2xl border border-dashed border-gray-300 bg-white px-6 py-12 text-center text-sm text-gray-500">
        {t(language, 'adminComingSoon')}
      </div>
    </div>
  );
}
