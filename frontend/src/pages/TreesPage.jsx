import { useCallback, useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import TreeDirectory from '../components/TreeDirectory';
import LoadingSpinner from '../components/LoadingSpinner';
import { fetchTreeCatalog, fetchTreeFilters, wakeApi } from '../api/client';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const PAGE_SIZE = 12;

const CATEGORY_LABELS = {
  en: {
    MEDICINAL: 'Medicinal',
    FRUIT: 'Fruit trees',
    SHADE: 'Shade',
    TIMBER: 'Timber',
    FIBRE: 'Fibre / barkcloth',
    ORNAMENTAL: 'Ornamental',
    WILDLIFE: 'Wildlife habitat',
  },
  rw: {
    MEDICINAL: 'Ubuvuzi',
    FRUIT: 'Ibiti by\'imbuto',
    SHADE: 'Igicucu',
    TIMBER: 'Ibiti by\'ubwubatsi',
    FIBRE: 'Igiti / barkcloth',
    ORNAMENTAL: 'Ubwiza',
    WILDLIFE: 'Aho inyamaswa zibera',
  },
  fr: {
    MEDICINAL: 'Médicinal',
    FRUIT: 'Arbres fruitiers',
    SHADE: 'Ombrage',
    TIMBER: 'Bois d\'œuvre',
    FIBRE: 'Fibre / écorce',
    ORNAMENTAL: 'Ornemental',
    WILDLIFE: 'Habitat faune',
  },
};

function categoryLabel(language, code) {
  return CATEGORY_LABELS[language]?.[code] || CATEGORY_LABELS.en[code] || code;
}

function shortFamily(family) {
  if (!family) return '';
  return family.split('(')[0].trim();
}

const selectClass =
  'w-full rounded-xl border border-gray-300 bg-white px-3 py-2.5 text-sm font-medium text-gray-800 focus:border-primary focus:outline-none';

/**
 * Tree directory — search, filters, 12-per-page table, pagination.
 */
export default function TreesPage() {
  const { language } = useLanguage();
  const [searchParams, setSearchParams] = useSearchParams();

  const q = searchParams.get('q') || '';
  const family = searchParams.get('family') || '';
  const category = searchParams.get('category') || '';
  const sort = searchParams.get('sort') || 'park';
  const page = Math.max(0, Number(searchParams.get('page') || 0));

  const [searchInput, setSearchInput] = useState(q);
  const [filters, setFilters] = useState({ families: [], categories: [] });
  const [catalog, setCatalog] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setSearchInput(q);
  }, [q]);

  useEffect(() => {
    fetchTreeFilters()
      .then(setFilters)
      .catch(() => setFilters({ families: [], categories: [] }));
  }, []);

  useEffect(() => {
    let cancelled = false;

    const loadCatalog = async () => {
      setLoading(true);
      setError('');
      try {
        await wakeApi(300000);
        if (cancelled) return;
        const data = await fetchTreeCatalog({
          lang: language,
          q,
          family,
          category,
          sort,
          page,
          size: PAGE_SIZE,
        });
        if (cancelled) return;
        setCatalog(data);
      } catch (err) {
        if (cancelled) return;
        setCatalog(null);
        setError(err.message || t(language, 'error'));
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    loadCatalog();
    return () => {
      cancelled = true;
    };
  }, [language, q, family, category, sort, page]);

  const updateParams = useCallback(
    (patch, { resetPage = true } = {}) => {
      const next = new URLSearchParams(searchParams);
      Object.entries(patch).forEach(([key, value]) => {
        if (value == null || value === '') next.delete(key);
        else next.set(key, String(value));
      });
      if (resetPage) next.delete('page');
      setSearchParams(next, { replace: true });
    },
    [searchParams, setSearchParams]
  );

  useEffect(() => {
    const handle = setTimeout(() => {
      const next = searchInput.trim();
      if (next !== q) updateParams({ q: next });
    }, 320);
    return () => clearTimeout(handle);
  }, [searchInput, q, updateParams]);

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    updateParams({ q: searchInput.trim() });
  };

  const clearFilters = () => {
    setSearchInput('');
    setSearchParams({}, { replace: true });
  };

  const hasActiveFilters = Boolean(q || family || category || (sort && sort !== 'park'));
  const trees = catalog?.content || [];
  const totalPages = catalog?.totalPages || 0;
  const totalElements = catalog?.totalElements || 0;
  const from = totalElements === 0 ? 0 : page * PAGE_SIZE + 1;
  const to = Math.min((page + 1) * PAGE_SIZE, totalElements);

  const pageNumbers = useMemo(() => {
    if (totalPages <= 1) return [];
    const window = 5;
    let start = Math.max(0, page - Math.floor(window / 2));
    let end = Math.min(totalPages - 1, start + window - 1);
    start = Math.max(0, end - window + 1);
    const nums = [];
    for (let i = start; i <= end; i += 1) nums.push(i);
    return nums;
  }, [page, totalPages]);

  return (
    <div className="bg-surface">
      <section className="border-b border-gray-200 bg-white py-12 sm:py-16">
        <div className="section-container text-center">
          <h1 className="font-display text-3xl font-semibold uppercase tracking-[0.18em] text-primary-dark sm:text-4xl">
            {t(language, 'treesCatalogTitle')}
          </h1>
          <p className="mx-auto mt-3 max-w-2xl text-base text-gray-500 sm:text-lg">
            {t(language, 'treesCatalogSubtitle')}
          </p>
        </div>
      </section>

      <section className="border-b border-gray-200 bg-white py-6">
        <div className="section-container space-y-4">
          <form onSubmit={handleSearchSubmit} className="relative">
            <label className="sr-only" htmlFor="explore-search">
              {t(language, 'searchTrees')}
            </label>
            <span className="pointer-events-none absolute inset-y-0 left-3 flex items-center text-gray-400" aria-hidden="true">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" className="h-5 w-5">
                <path fillRule="evenodd" d="M9 3.5a5.5 5.5 0 1 0 3.44 9.74l3.16 3.16a.75.75 0 1 0 1.06-1.06l-3.16-3.16A5.5 5.5 0 0 0 9 3.5ZM5.5 9a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0Z" clipRule="evenodd" />
              </svg>
            </span>
            <input
              id="explore-search"
              type="search"
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              placeholder={t(language, 'exploreSearchPlaceholder')}
              className="w-full rounded-xl border border-gray-300 py-3 pl-11 pr-4 text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
            />
          </form>

          <div className="grid gap-3 sm:grid-cols-3">
            <label className="sr-only" htmlFor="filter-all-trees">
              {t(language, 'filterCategory')}
            </label>
            <select
              id="filter-all-trees"
              value={category}
              onChange={(e) => updateParams({ category: e.target.value })}
              className={selectClass}
            >
              <option value="">{t(language, 'filterAllTrees')}</option>
              {filters.categories?.map((item) => (
                <option key={item} value={item}>
                  {categoryLabel(language, item)}
                </option>
              ))}
            </select>

            <label className="sr-only" htmlFor="filter-family">
              {t(language, 'filterFamily')}
            </label>
            <select
              id="filter-family"
              value={family}
              onChange={(e) => updateParams({ family: e.target.value })}
              className={selectClass}
            >
              <option value="">{t(language, 'filterFamily')}</option>
              {filters.families?.map((item) => (
                <option key={item} value={shortFamily(item)}>
                  {shortFamily(item)}
                </option>
              ))}
            </select>

            <label className="sr-only" htmlFor="filter-sort">
              {t(language, 'sortTrees')}
            </label>
            <select
              id="filter-sort"
              value={sort}
              onChange={(e) => updateParams({ sort: e.target.value === 'park' ? '' : e.target.value })}
              className={selectClass}
            >
              <option value="park">{t(language, 'sortParkOrder')}</option>
              <option value="az">{t(language, 'sortAZ')}</option>
              <option value="za">{t(language, 'sortZA')}</option>
            </select>
          </div>

          {hasActiveFilters && (
            <div className="flex flex-wrap items-center gap-2">
              <span className="text-xs text-gray-500">{t(language, 'activeFilters')}</span>
              {q && (
                <button type="button" onClick={() => updateParams({ q: '' })} className="rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary-dark">
                  “{q}” ×
                </button>
              )}
              {family && (
                <button type="button" onClick={() => updateParams({ family: '' })} className="rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary-dark">
                  {family} ×
                </button>
              )}
              {category && (
                <button type="button" onClick={() => updateParams({ category: '' })} className="rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary-dark">
                  {categoryLabel(language, category)} ×
                </button>
              )}
              {sort && sort !== 'park' && (
                <button type="button" onClick={() => updateParams({ sort: '' })} className="rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary-dark">
                  {sort === 'za' ? t(language, 'sortZA') : t(language, 'sortAZ')} ×
                </button>
              )}
              <button type="button" onClick={clearFilters} className="text-xs font-semibold text-gray-600 underline">
                {t(language, 'clearFilters')}
              </button>
            </div>
          )}
        </div>
      </section>

      <section className="py-10 sm:py-14">
        <div className="section-container">
          <p className="mb-4 text-sm text-gray-500">
            {t(language, 'showingTreesRange')
              .replace('{from}', String(from))
              .replace('{to}', String(to))
              .replace('{total}', String(totalElements))}
          </p>

          {loading && (
            <div className="py-8 text-center">
              <LoadingSpinner />
              <p className="mt-4 text-sm text-gray-500">{t(language, 'loadingTreesHint')}</p>
            </div>
          )}
          {!loading && error && (
            <div className="rounded-2xl border border-red-200 bg-red-50 px-5 py-8 text-center text-red-800">
              <p>{error}</p>
              <p className="mt-2 text-sm text-red-700/80">{t(language, 'apiWakeHint')}</p>
              <button
                type="button"
                className="btn btn-primary mt-4 !rounded-xl"
                onClick={() => window.location.reload()}
              >
                {t(language, 'retryLoad')}
              </button>
            </div>
          )}
          {!loading && !error && trees.length === 0 && (
            <div className="rounded-3xl border border-dashed border-gray-300 bg-white py-20 text-center">
              <p className="text-gray-500">{t(language, 'searchNoResults')}</p>
              {hasActiveFilters && (
                <button type="button" onClick={clearFilters} className="btn btn-secondary mt-4 !rounded-xl">
                  {t(language, 'clearFilters')}
                </button>
              )}
            </div>
          )}
          {!loading && !error && trees.length > 0 && (
            <TreeDirectory trees={trees} />
          )}

          {!loading && totalPages > 1 && (
            <nav className="mt-10 flex flex-wrap items-center justify-center gap-2" aria-label={t(language, 'pagination')}>
              <button
                type="button"
                disabled={page <= 0}
                onClick={() => updateParams({ page: page - 1 }, { resetPage: false })}
                className="rounded-xl border border-gray-200 bg-white px-4 py-2 text-sm font-semibold text-primary-dark disabled:opacity-40"
              >
                ← {t(language, 'prevPage')}
              </button>
              {pageNumbers.map((n) => (
                <button
                  key={n}
                  type="button"
                  onClick={() => updateParams({ page: n }, { resetPage: false })}
                  className={`min-w-[2.5rem] rounded-xl px-3 py-2 text-sm font-semibold ${
                    n === page
                      ? 'bg-primary text-white'
                      : 'border border-gray-200 bg-white text-primary-dark'
                  }`}
                >
                  {n + 1}
                </button>
              ))}
              <button
                type="button"
                disabled={page >= totalPages - 1}
                onClick={() => updateParams({ page: page + 1 }, { resetPage: false })}
                className="rounded-xl border border-gray-200 bg-white px-4 py-2 text-sm font-semibold text-primary-dark disabled:opacity-40"
              >
                {t(language, 'nextPage')} →
              </button>
            </nav>
          )}
        </div>
      </section>
    </div>
  );
}
