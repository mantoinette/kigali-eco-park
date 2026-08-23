import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchTrees } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { displayCommonName } from '../utils/treeDisplay';

/** Admin tree list — links to public preview and QR label tools. */
export default function AdminTreesPage() {
  const { language } = useLanguage();
  const [trees, setTrees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchTrees(language)
      .then(setTrees)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [language]);

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <div className="mb-8">
        <h1 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'adminManageTrees')}</h1>
        <p className="mt-2 text-sm text-gray-600">{t(language, 'adminManageTreesDesc')}</p>
      </div>

      {error && (
        <p className="mb-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
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
                <td className="px-4 py-3 font-medium">{displayCommonName(tree, language)}</td>
                <td className="px-4 py-3 italic text-gray-600">{tree.scientificName}</td>
                <td className="px-4 py-3">
                  <div className="flex flex-wrap gap-2">
                    <Link to={`/trees/${tree.slug}`} className="text-xs font-semibold text-primary hover:underline">
                      {t(language, 'viewTreeDetails')}
                    </Link>
                    <Link to={`/admin/qr-label/${tree.slug}`} className="text-xs font-semibold text-primary hover:underline">
                      {t(language, 'openQrLabel')}
                    </Link>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
