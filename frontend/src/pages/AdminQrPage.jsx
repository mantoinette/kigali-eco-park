import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { fetchQrCode, fetchTrees } from '../api/client';
import QrCodeActions from '../components/admin/QrCodeActions';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { displayCommonName } from '../utils/treeDisplay';

export default function AdminQrPage() {
  const { language } = useLanguage();
  const { user } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  const [trees, setTrees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [qr, setQr] = useState(null);
  const [generatingSlug, setGeneratingSlug] = useState('');

  useEffect(() => {
    setLoading(true);
    fetchTrees(language)
      .then(setTrees)
      .catch((err) => setError(err.message || 'Failed to load trees'))
      .finally(() => setLoading(false));
  }, [language]);

  const generateFor = (slug) => {
    setError('');
    setGeneratingSlug(slug);
    setQr(null);
    setSearchParams({ slug }, { replace: true });
    fetchQrCode(slug, user?.token)
      .then(setQr)
      .catch((err) => setError(err.message))
      .finally(() => setGeneratingSlug(''));
  };

  useEffect(() => {
    const slug = searchParams.get('slug');
    if (slug && user?.token && !qr) {
      generateFor(slug);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user?.token]);

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <div className="mb-8">
        <h1 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'adminQrCodes')}</h1>
        <p className="mt-2 text-sm text-gray-600">{t(language, 'qrOnlyHint')}</p>
      </div>

      {error && (
        <p className="mb-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
      )}

      {qr && (
        <div className="mb-8 rounded-2xl border border-primary/20 bg-white p-6 shadow-card">
          <div className="flex flex-col items-center gap-6 lg:flex-row lg:items-start lg:justify-center lg:gap-10">
            <div className="rounded-2xl border border-gray-100 bg-white p-4">
              <img
                src={qr.qrCodeBase64}
                alt={`QR code for ${qr.scientificName}`}
                className="h-56 w-56 sm:h-72 sm:w-72"
                style={{ imageRendering: 'pixelated' }}
              />
            </div>
            <div className="w-full max-w-md">
              <h2 className="font-display text-xl font-bold italic text-primary-dark">{qr.scientificName}</h2>
              <p className="mt-1 break-all font-mono text-xs text-gray-500">{qr.url}</p>
              <p className="mt-4 text-sm text-gray-600">{t(language, 'qrDownloadPrintHint')}</p>
              <div className="mt-5">
                <QrCodeActions qr={qr} language={language} />
              </div>
            </div>
          </div>
        </div>
      )}

      <div className="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
        <table className="min-w-full text-left text-sm">
          <thead className="bg-primary/5 text-xs uppercase tracking-wide text-primary-dark">
            <tr>
              <th className="px-4 py-3">{t(language, 'directoryColumnId')}</th>
              <th className="px-4 py-3">{t(language, 'directoryColumnName')}</th>
              <th className="px-4 py-3">{t(language, 'directoryColumnScientific')}</th>
              <th className="px-4 py-3">{t(language, 'actions')}</th>
            </tr>
          </thead>
          <tbody>
            {trees.map((tree) => (
              <tr key={tree.slug} className="border-t border-gray-100">
                <td className="px-4 py-3 font-mono text-xs text-gray-600">{tree.qrCodeId}</td>
                <td className="px-4 py-3 font-medium text-primary-dark">
                  {displayCommonName(tree, language)}
                </td>
                <td className="px-4 py-3 italic text-gray-600">{tree.scientificName}</td>
                <td className="px-4 py-3">
                  <button
                    type="button"
                    className="inline-flex rounded-lg bg-primary px-3 py-1.5 text-xs font-semibold text-white hover:bg-primary-dark disabled:opacity-60"
                    disabled={generatingSlug === tree.slug}
                    onClick={() => generateFor(tree.slug)}
                  >
                    {generatingSlug === tree.slug ? t(language, 'pleaseWait') : t(language, 'generateQrCode')}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
