import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchTrees } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { RequireAdmin } from './AdminDashboard';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { displayCommonName } from '../utils/treeDisplay';

/**
 * Admin-only QR label workspace: list park trees and open printable labels.
 * Generation/download/print happen on the protected label page.
 */
export default function AdminQrPage() {
  const { language } = useLanguage();
  const [trees, setTrees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    fetchTrees(language)
      .then(setTrees)
      .catch((err) => setError(err.message || 'Failed to load trees'))
      .finally(() => setLoading(false));
  }, [language]);

  return (
    <RequireAdmin>
      <div className="bg-surface py-12">
        <div className="section-container">
          <div className="mb-8 flex flex-wrap items-end justify-between gap-4">
            <div>
              <p className="text-xs font-semibold uppercase tracking-[0.18em] text-primary">
                {t(language, 'admin')}
              </p>
              <h1 className="mt-2 font-display text-3xl font-bold text-primary-dark">
                {t(language, 'adminQrCodes')}
              </h1>
              <p className="mt-2 max-w-2xl text-sm text-gray-600">
                {t(language, 'adminQrCodesDesc')}
              </p>
            </div>
            <Link to="/admin" className="btn btn-secondary !rounded-xl">
              {t(language, 'adminDashboard')}
            </Link>
          </div>

          {loading && <LoadingSpinner />}
          {error && (
            <p className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
          )}

          {!loading && !error && (
            <div className="overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
              <table className="min-w-full text-left text-sm">
                <thead className="bg-primary/5 text-xs uppercase tracking-wide text-primary-dark">
                  <tr>
                    <th className="px-4 py-3">{t(language, 'directoryColumnId')}</th>
                    <th className="px-4 py-3">{t(language, 'directoryColumnName')}</th>
                    <th className="px-4 py-3">{t(language, 'directoryColumnScientific')}</th>
                    <th className="px-4 py-3">{t(language, 'printLabel')}</th>
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
                        <Link
                          to={`/admin/qr-label/${tree.slug}`}
                          className="font-semibold text-primary hover:underline"
                        >
                          {t(language, 'openQrLabel')}
                        </Link>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </RequireAdmin>
  );
}
