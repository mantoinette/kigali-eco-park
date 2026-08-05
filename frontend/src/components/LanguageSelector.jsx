import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const OPTIONS = [
  { code: 'en', label: 'English' },
  { code: 'rw', label: 'Kinyarwanda' },
  { code: 'fr', label: 'French' },
];

export default function LanguageSelector() {
  const { language, setLanguage } = useLanguage();

  return (
    <div className="flex items-center gap-1 rounded-full border border-gray-200 bg-surface p-1 text-xs" role="group" aria-label={t(language, 'chooseLanguage')}>
      <span className="px-2 text-base" aria-hidden="true">🌐</span>
      {OPTIONS.map((opt) => (
        <button
          key={opt.code}
          type="button"
          onClick={() => setLanguage(opt.code)}
          className={`rounded-full px-2.5 py-1 font-medium transition-colors ${
            language === opt.code
              ? 'bg-primary text-white shadow-sm'
              : 'text-gray-600 hover:bg-white hover:text-primary-dark'
          }`}
          aria-pressed={language === opt.code}
        >
          {opt.label}
        </button>
      ))}
    </div>
  );
}
