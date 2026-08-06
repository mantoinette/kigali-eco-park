import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import TreeDetail from '../components/TreeDetail';
import { fetchTreeByQrCode } from '../api/client';
import { useLanguage } from '../context/LanguageContext';

/**
 * Full tree profile — only reachable by scanning a park QR code
 * (/scan/{qrCodeId}).
 */
export default function ScanTreePage() {
  const { qrCodeId } = useParams();
  const { language } = useLanguage();
  const [tree, setTree] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    fetchTreeByQrCode(qrCodeId, language)
      .then(setTree)
      .catch((err) => {
        setTree(null);
        setError(err.message);
      })
      .finally(() => setLoading(false));
  }, [qrCodeId, language]);

  return <TreeDetail key={qrCodeId} tree={tree} loading={loading} error={error} />;
}
