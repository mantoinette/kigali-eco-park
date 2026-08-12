import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import TreeDetail from '../components/TreeDetail';
import { fetchTreeById } from '../api/client';
import { useLanguage } from '../context/LanguageContext';

/** Alias route matching eco-park.rw/tree/{id} */
export default function TreeByIdPage() {
  const { id } = useParams();
  const { language } = useLanguage();
  const [tree, setTree] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    fetchTreeById(id, language)
      .then(setTree)
      .catch((err) => {
        setTree(null);
        setError(err.message);
      })
      .finally(() => setLoading(false));
  }, [id, language]);

  return <TreeDetail key={`${id}-${language}`} tree={tree} loading={loading} error={error} />;
}
