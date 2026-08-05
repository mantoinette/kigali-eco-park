import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function PrivacyPage() {
  const { language } = useLanguage();

  return (
    <div className="bg-surface py-12">
      <div className="section-container max-w-3xl prose prose-green">
        <h1 className="section-title">{t(language, 'privacyPolicy')}</h1>
        <p className="mt-6 text-gray-700 leading-relaxed">{t(language, 'privacyText1')}</p>
        <p className="mt-4 text-gray-700 leading-relaxed">{t(language, 'privacyText2')}</p>
      </div>
    </div>
  );
}
