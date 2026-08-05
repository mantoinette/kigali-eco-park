import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function LoadingSpinner({ label }) {
  const { language } = useLanguage();

  return (
    <div className="flex flex-col items-center justify-center gap-4 py-16" role="status">
      <div
        className="h-10 w-10 animate-spin rounded-full border-4 border-primary/20 border-t-primary"
        aria-hidden="true"
      />
      <p className="text-sm text-gray-600">{label || t(language, 'loading')}</p>
    </div>
  );
}
