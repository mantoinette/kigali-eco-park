import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchQrCode } from '../api/client';
import QrCodeActions from '../components/admin/QrCodeActions';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function QrLabelPage() {
  const { slug } = useParams();
  const { language } = useLanguage();
  const { user } = useAuth();
  const [qr, setQr] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!slug || !user?.token) return;
    setError('');
    setQr(null);
    fetchQrCode(slug, user.token)
      .then(setQr)
      .catch((err) => setError(err.message));
  }, [slug, user?.token]);

  if (!qr && !error) {
    return <LoadingSpinner label={t(language, 'loadingQr')} />;
  }

  return (
    <div>
      <Link to="/admin/qr" className="text-sm font-medium text-primary hover:underline">
        ← {t(language, 'adminQrCodes')}
      </Link>
      <h1 className="mt-3 font-display text-3xl font-bold text-primary-dark">
        {t(language, 'generateQrCode')}
      </h1>
      <p className="mt-2 text-sm text-gray-600">{t(language, 'qrDownloadPrintHint')}</p>

      {error && (
        <p className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
      )}

      {qr && (
        <div className="mt-8 flex flex-col items-center gap-6 rounded-2xl border border-gray-200 bg-white p-8 shadow-card">
          <img
            src={qr.qrCodeBase64}
            alt={`QR code for ${qr.scientificName}`}
            className="h-64 w-64 sm:h-80 sm:w-80"
            style={{ imageRendering: 'pixelated' }}
          />
          <p className="text-center">
            <span className="block font-semibold italic text-primary-dark">{qr.scientificName}</span>
            <span className="mt-1 block break-all font-mono text-xs text-gray-500">{qr.url}</span>
          </p>
          <QrCodeActions qr={qr} language={language} />
        </div>
      )}
    </div>
  );
}
