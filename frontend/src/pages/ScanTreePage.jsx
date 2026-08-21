import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchTreeByAccessToken, fetchTreeByQrCode } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import TreeDetail from '../components/TreeDetail';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

/**
 * Public tree information opened by scanning a park QR label.
 * Accepts opaque access tokens (/t/:token) or legacy park IDs (/scan/TREE-001).
 * Does not expose QR generate/print tools (admin-only).
 */
export default function ScanTreePage({ mode = 'legacy' }) {
  const { qrCodeId, token } = useParams();
  const { language } = useLanguage();
  const [tree, setTree] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const lookupKey = mode === 'token' ? token : qrCodeId;

  useEffect(() => {
    setLoading(true);
    setError(null);
    const loader =
      mode === 'token'
        ? fetchTreeByAccessToken(lookupKey, language)
        : fetchTreeByQrCode(lookupKey, language);

    loader
      .then(setTree)
      .catch((err) => {
        setTree(null);
        setError(err.message);
      })
      .finally(() => setLoading(false));
  }, [lookupKey, language, mode]);

  if (loading) return <LoadingSpinner label={t(language, 'loading')} />;
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
      <div className="border-b border-primary/15 bg-primary/5">
        <div className="section-container flex flex-wrap items-center justify-between gap-3 py-3 text-sm">
          <p className="font-medium text-primary-dark">
            {t(language, 'treeInformation')}
            {tree.qrCodeId ? ` · ${tree.qrCodeId}` : ''}
          </p>
          <Link to="/trees" className="font-semibold text-primary-dark hover:underline">
            {t(language, 'backToList')}
          </Link>
        </div>
      </div>

      <TreeDetail key={`${lookupKey}-${language}`} tree={tree} loading={false} error={null} />
    </div>
  );
}
