import { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { searchTrees } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { displayCommonName } from '../utils/treeDisplay';

export default function SearchPage() {
  const { language } = useLanguage();
  const [params, setParams] = useSearchParams();
  const [query, setQuery] = useState(params.get('q') || '');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const q = params.get('q') || '';
    setQuery(q);
    if (!q.trim()) {
      setResults([]);
      return;
    }
    setLoading(true);
    searchTrees(q, language)
      .then(setResults)
      .catch(() => setResults([]))
      .finally(() => setLoading(false));
  }, [params, language]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setParams(query.trim() ? { q: query.trim() } : {});
  };

  return (
    <div className="bg-surface py-12">
      <div className="section-container max-w-3xl">
        <h1 className="section-title">{t(language, 'searchTitle')}</h1>
        <p className="section-subtitle">{t(language, 'searchIntro')}</p>

        <form onSubmit={handleSubmit} className="mt-8 flex gap-2">
          <input
            type="search"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder={t(language, 'searchPlaceholder')}
            className="flex-1 rounded-full border border-gray-300 px-5 py-3 text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
          <button type="submit" className="btn btn-primary !px-5">
            {t(language, 'search')}
          </button>
        </form>

        <div className="mt-8">
          {loading && <LoadingSpinner />}
          {!loading && params.get('q') && results.length === 0 && (
            <p className="text-gray-600">{t(language, 'searchNoResults')}</p>
          )}
          <ul className="space-y-3">
            {results.map((tree) => (
              <li key={tree.id}>
                <Link
                  to={`/trees/${tree.slug}`}
                  className="card flex items-center gap-4 !p-4 hover:!translate-y-0"
                >
                  {tree.primaryImageUrl ? (
                    <img src={tree.primaryImageUrl} alt="" className="h-16 w-16 rounded-xl object-cover" />
                  ) : (
                    <div className="flex h-16 w-16 items-center justify-center rounded-xl bg-primary/10 text-2xl">🌳</div>
                  )}
                  <div>
                    <p className="font-semibold text-primary-dark">{displayCommonName(tree, language)}</p>
                    <p className="text-sm italic text-gray-500">{tree.scientificName}</p>
                    {tree.family && <p className="text-xs text-gray-500">{tree.family}</p>}
                  </div>
                </Link>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}
