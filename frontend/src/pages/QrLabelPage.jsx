import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchQrCode } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

function fileStem(scientificName, slug) {
  const raw = scientificName || slug || 'tree-qr';
  return raw.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
}

function downloadDataUri(dataUri, filename) {
  const link = document.createElement('a');
  link.href = dataUri;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  link.remove();
}

function downloadSvg(svg, filename) {
  const blob = new Blob([svg], { type: 'image/svg+xml;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  downloadDataUri(url, filename);
  URL.revokeObjectURL(url);
}

export default function QrLabelPage() {
  const { slug } = useParams();
  const { language } = useLanguage();
  const { user } = useAuth();
  const [qr, setQr] = useState(null);
  const [error, setError] = useState('');
  const [generated, setGenerated] = useState(false);

  const generate = () => {
    setError('');
    setQr(null);
    setGenerated(false);
    fetchQrCode(slug, user?.token)
      .then((data) => {
        setQr(data);
        setGenerated(true);
      })
      .catch((err) => setError(err.message));
  };

  useEffect(() => {
    generate();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [slug, user?.token]);

  const stem = fileStem(qr?.scientificName, slug);

  const handlePrint = () => {
    const previousTitle = document.title;
    document.title = qr?.scientificName || 'QR';
    window.print();
    setTimeout(() => {
      document.title = previousTitle;
    }, 500);
  };

  if (!qr && !error) {
    return <LoadingSpinner label={t(language, 'loadingQr')} />;
  }

  return (
    <div className="qr-only-page min-h-screen bg-[#eef2ef] px-4 py-8 sm:px-6">
      <style>{`
        @media print {
          @page { size: A6 portrait; margin: 12mm; }
          body { background: #fff !important; }
          .no-print { display: none !important; }
          .qr-print-target {
            position: static;
            box-shadow: none;
            border: none;
            padding: 0;
            background: #fff;
          }
          .qr-print-target img {
            width: 80mm;
            height: 80mm;
            max-width: 100%;
          }
        }
      `}</style>

      <div className="no-print mx-auto mb-8 max-w-xl">
        <Link to="/admin/qr" className="text-sm font-medium text-primary hover:underline">
          ← {t(language, 'adminQrCodes')}
        </Link>
        <h1 className="mt-3 font-display text-3xl font-bold text-primary-dark">
          {t(language, 'generateQrCode')}
        </h1>
        <p className="mt-2 text-sm text-gray-600">{t(language, 'qrOnlyHint')}</p>
      </div>

      {error && (
        <div className="no-print mx-auto max-w-xl rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
          <p>{t(language, 'qrLoadError')}</p>
          <p className="mt-1">{error}</p>
          <button type="button" className="btn btn-primary mt-4 !rounded-xl" onClick={generate}>
            {t(language, 'generateQrCode')}
          </button>
        </div>
      )}

      {qr && (
        <div className="mx-auto flex max-w-xl flex-col items-center">
          <div className="qr-print-target rounded-3xl bg-white p-6 shadow-card sm:p-10">
            <img
              src={qr.qrCodeBase64}
              alt={`QR code for ${qr.scientificName}`}
              className="h-64 w-64 sm:h-80 sm:w-80"
              style={{ imageRendering: 'pixelated' }}
            />
          </div>

          {generated && (
            <div className="no-print mt-8 flex w-full max-w-md flex-col gap-3">
              <p className="text-center text-sm text-gray-600">
                <span className="font-semibold italic text-primary-dark">{qr.scientificName}</span>
                <span className="mt-1 block break-all font-mono text-xs text-gray-500">{qr.url}</span>
              </p>
              <button
                type="button"
                className="btn btn-primary !rounded-xl py-3"
                onClick={() => downloadDataUri(qr.qrCodeBase64, `${stem}-qr.png`)}
              >
                {t(language, 'downloadHdQr')}
              </button>
              <button
                type="button"
                className="btn btn-secondary !rounded-xl py-3"
                onClick={() => downloadSvg(qr.qrCodeSvg, `${stem}-qr.svg`)}
                disabled={!qr.qrCodeSvg}
              >
                {t(language, 'downloadSvgQr')}
              </button>
              <button
                type="button"
                className="btn btn-secondary !rounded-xl py-3"
                onClick={handlePrint}
              >
                {t(language, 'printQrCode')}
              </button>
              <button
                type="button"
                className="text-sm font-semibold text-primary hover:underline"
                onClick={generate}
              >
                {t(language, 'generateQrCode')}
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
