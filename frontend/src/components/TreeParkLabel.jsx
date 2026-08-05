import { useEffect, useState } from 'react';
import { fetchQrCode } from '../api/client';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

/** Park-style label strip — sits below the tree image, never covering it. */
export default function TreeParkLabel({ slug, compact = false }) {
  const { language } = useLanguage();
  const [qr, setQr] = useState(null);

  useEffect(() => {
    if (!slug) return;
    fetchQrCode(slug)
      .then(setQr)
      .catch(() => setQr(null));
  }, [slug]);

  return (
    <div
      className={`flex items-center gap-3 border-t-4 border-primary bg-gradient-to-r from-primary/5 to-white ${
        compact ? 'px-3 py-2.5' : 'px-4 py-3 sm:gap-4 sm:px-5 sm:py-4'
      }`}
    >
      <div className="shrink-0 rounded-lg border border-primary/20 bg-white p-1.5 shadow-sm">
        {qr ? (
          <img
            src={qr.qrCodeBase64}
            alt={t(language, 'scanWithPhoneAlt')}
            className={`rounded ${compact ? 'h-14 w-14' : 'h-16 w-16 sm:h-[4.5rem] sm:w-[4.5rem]'}`}
          />
        ) : (
          <div
            className={`flex items-center justify-center rounded bg-gray-50 ${
              compact ? 'h-14 w-14' : 'h-16 w-16 sm:h-[4.5rem] sm:w-[4.5rem]'
            }`}
          >
            <div className="h-5 w-5 animate-spin rounded-full border-2 border-primary/30 border-t-primary" />
          </div>
        )}
      </div>

      <div className="min-w-0 flex-1">
        <p className={`font-bold uppercase tracking-wide text-primary ${compact ? 'text-[10px]' : 'text-xs'}`}>
          {t(language, 'qrOnTreeLabel')}
        </p>
        <p className={`mt-0.5 font-medium text-primary-dark ${compact ? 'text-xs' : 'text-sm'}`}>
          {t(language, 'scanAtParkTitle')}
        </p>
        <p className={`mt-1 text-gray-600 ${compact ? 'text-[10px] leading-snug' : 'text-xs leading-relaxed sm:text-sm'}`}>
          {t(language, 'pointCamera')}
        </p>
      </div>

      {qr?.treeId && (
        <div className="hidden shrink-0 text-right sm:block">
          <span className="text-[10px] font-semibold uppercase tracking-wider text-gray-400">
            ID
          </span>
          <p className="font-mono text-sm font-bold text-primary-dark">{qr.treeId}</p>
        </div>
      )}
    </div>
  );
}
