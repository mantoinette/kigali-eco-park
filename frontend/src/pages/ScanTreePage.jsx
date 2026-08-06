import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchQrCode, fetchTreeByQrCode } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import TreeDetail from '../components/TreeDetail';
import TreeA4PrintTemplate from '../components/TreeA4PrintTemplate';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

/**
 * Full tree guide — opened only by scanning the park QR code (/scan/TREE-001).
 */
export default function ScanTreePage() {
  const { qrCodeId } = useParams();
  const { language } = useLanguage();
  const [tree, setTree] = useState(null);
  const [qr, setQr] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    fetchTreeByQrCode(qrCodeId, language)
      .then((detail) => {
        setTree(detail);
        try {
          sessionStorage.setItem(`qr-unlock:${detail.qrCodeId}`, '1');
        } catch {
          // ignore storage errors
        }
        return fetchQrCode(detail.slug).catch(() => null);
      })
      .then((qrData) => setQr(qrData))
      .catch((err) => {
        setTree(null);
        setError(err.message);
      })
      .finally(() => setLoading(false));
  }, [qrCodeId, language]);

  if (loading) return <LoadingSpinner label={t(language, 'loadingQr')} />;
  if (error || !tree) {
    return (
      <div className="section-container py-20 text-center">
        <p className="text-gray-600">{t(language, 'notFound')}</p>
        <Link to="/trees" className="btn btn-primary mt-4">{t(language, 'backToList')}</Link>
      </div>
    );
  }

  return (
    <div>
      <div className="no-print border-b border-primary/15 bg-primary/5">
        <div className="section-container flex flex-wrap items-center justify-between gap-3 py-3 text-sm">
          <p className="font-medium text-primary-dark">
            {t(language, 'unlockedByQr')}
            {tree.qrCodeId ? ` · ${tree.qrCodeId}` : ''}
          </p>
          <Link to="/trees" className="font-semibold text-primary-dark hover:underline">
            {t(language, 'backToList')}
          </Link>
        </div>
      </div>

      <TreeDetail key={qrCodeId} tree={tree} loading={false} error={null} />

      <section id="print-tree-sign" className="border-t border-primary/10 bg-white py-12">
        <div className="section-container">
          <div className="mx-auto max-w-4xl">
            <div className="no-print mb-8 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
              <div>
                <p className="text-xs font-semibold uppercase tracking-[0.2em] text-primary">
                  {t(language, 'treeDetailsTitle')}
                </p>
                <h2 className="mt-2 font-display text-2xl font-semibold text-primary-dark sm:text-3xl">
                  {t(language, 'printTreeSign')}
                </h2>
                <p className="mt-2 max-w-xl text-sm text-gray-600">{t(language, 'printTreeSignHint')}</p>
              </div>
              <div className="flex flex-wrap gap-3">
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={() => window.print()}
                  disabled={!qr}
                >
                  {qr ? 'Print / Save as PDF' : 'Loading QR…'}
                </button>
              </div>
            </div>

            <div className="grid gap-8 lg:grid-cols-[1fr_220px] lg:items-start">
              <TreeA4PrintTemplate tree={tree} qr={qr} />
              <aside className="no-print rounded-2xl border border-gray-200 bg-surface p-5">
                <h3 className="text-lg font-bold text-primary-dark">{t(language, 'treeQrCode')}</h3>
                <p className="mt-2 text-sm text-gray-600">{t(language, 'treeQrCodeHint')}</p>
                <div className="mt-4 flex justify-center rounded-xl bg-white p-3 ring-1 ring-gray-100">
                  {qr?.qrCodeBase64 ? (
                    <img
                      src={qr.qrCodeBase64}
                      alt={t(language, 'scanWithPhoneAlt')}
                      className="h-40 w-40"
                    />
                  ) : (
                    <div className="flex h-40 w-40 items-center justify-center">
                      <div className="h-8 w-8 animate-spin rounded-full border-2 border-primary/30 border-t-primary" />
                    </div>
                  )}
                </div>
                {tree.qrCodeId && (
                  <p className="mt-3 text-center font-mono text-sm font-bold text-primary-dark">
                    {tree.qrCodeId}
                  </p>
                )}
              </aside>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
