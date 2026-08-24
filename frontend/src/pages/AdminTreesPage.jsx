import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { deleteAdminTree, fetchAdminTrees } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function AdminTreesPage() {
  const { language } = useLanguage();
  const { user } = useAuth();
  const [trees, setTrees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = () => {
    setLoading(true);
    fetchAdminTrees(user.token, language)
      .then(setTrees)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, [user.token, language]);

  const handleDelete = async (tree) => {
    if (!window.confirm(t(language, 'confirmDeleteTree'))) return;
    try {
      await deleteAdminTree(user.token, tree.id);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <div className="mb-8 flex flex-wrap items-start justify-between gap-4">
        <div>
          <h1 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'adminManageTrees')}</h1>
          <p className="mt-2 text-sm text-gray-600">{t(language, 'adminManageTreesDesc')}</p>
        </div>
        <Link to="/admin/trees/new" className="btn btn-primary !rounded-xl !px-4 !py-2 text-sm">
          {t(language, 'adminAddTree')}
        </Link>
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
              <th className="px-4 py-3">{t(language, 'status')}</th>
              <th className="px-4 py-3">{t(language, 'actions')}</th>
            </tr>
          </thead>
          <tbody>
            {trees.map((tree) => (
              <tr key={tree.id} className="border-t border-gray-100">
                <td className="px-4 py-3 font-mono text-xs text-gray-600">{tree.qrCodeId}</td>
                <td className="px-4 py-3 font-medium">{tree.commonName}</td>
                <td className="px-4 py-3 italic text-gray-600">{tree.scientificName}</td>
                <td className="px-4 py-3">
                  <span className={`rounded-full px-2.5 py-0.5 text-xs font-semibold ${
                    tree.published ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-600'
                  }`}
                  >
                    {tree.published ? t(language, 'publishedOnSite') : t(language, 'inactive')}
                  </span>
                </td>
                <td className="px-4 py-3">
                  <div className="flex flex-wrap gap-2">
                    <Link to={`/admin/trees/${tree.id}/edit`} className="text-xs font-semibold text-primary hover:underline">
                      {t(language, 'edit')}
                    </Link>
                    <Link to={`/trees/${tree.slug}`} className="text-xs font-semibold text-primary hover:underline">
                      {t(language, 'viewTreeDetails')}
                    </Link>
                    <Link
                      to={`/admin/qr?slug=${encodeURIComponent(tree.slug)}`}
                      className="text-xs font-semibold text-primary hover:underline"
                    >
                      {t(language, 'generateQrCode')}
                    </Link>
                    <button
                      type="button"
                      onClick={() => handleDelete(tree)}
                      className="text-xs font-semibold text-red-600 hover:underline"
                    >
                      {t(language, 'delete')}
                    </button>
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
