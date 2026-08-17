import { useCallback, useEffect, useMemo, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import TreeDirectory from '../components/TreeDirectory';
import LoadingSpinner from '../components/LoadingSpinner';
import { fetchTreeCatalog, fetchTreeFilters, wakeApi } from '../api/client';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const HERO_IMG = 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=1920&q=80';
const PAGE_SIZE = 25;

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

const NATIVE_LABELS = {
  en: { NATIVE: 'Native', INTRODUCED: 'Introduced', UNKNOWN: 'Unspecified' },
  rw: { NATIVE: 'By\'igihugu', INTRODUCED: 'Byinjijwe', UNKNOWN: 'Ntibizwi' },
  fr: { NATIVE: 'Indigène', INTRODUCED: 'Introduit', UNKNOWN: 'Non précisé' },
};

function categoryLabel(language, code) {
  return CATEGORY_LABELS[language]?.[code] || CATEGORY_LABELS.en[code] || code;
}

function nativeLabel(language, code) {
  return NATIVE_LABELS[language]?.[code] || NATIVE_LABELS.en[code] || code;
}

function shortFamily(family) {
  if (!family) return '';
  return family.split('(')[0].trim();
}

/**
 * Explore Trees — compact searchable directory (not a card grid).
 * Each name opens /trees/{qrCodeId}; new DB trees appear automatically.
 */
export default function TreesPage() {
  const { language } = useLanguage();
  const [searchParams, setSearchParams] = useSearchParams();

  const q = searchParams.get('q') || '';
  const family = searchParams.get('family') || '';
  const category = searchParams.get('category') || '';
  const nativeStatus = searchParams.get('nativeStatus') || '';
  const page = Math.max(0, Number(searchParams.get('page') || 0));

  const [searchInput, setSearchInput] = useState(q);
  const [filters, setFilters] = useState({ families: [], categories: [], nativeStatuses: [] });
  const [catalog, setCatalog] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setSearchInput(q);
  }, [q]);

  useEffect(() => {
    fetchTreeFilters()
      .then(setFilters)
      .catch(() => setFilters({ families: [], categories: [], nativeStatuses: [] }));
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
          nativeStatus,
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
  }, [language, q, family, category, nativeStatus, page]);

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

  const hasActiveFilters = Boolean(q || family || category || nativeStatus);
  const trees = catalog?.content || [];
  const totalPages = catalog?.totalPages || 0;
  const totalElements = catalog?.totalElements || 0;

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
      <section className="relative flex min-h-[300px] items-end overflow-hidden sm:min-h-[360px]">
        <img src={HERO_IMG} alt="" className="absolute inset-0 h-full w-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-t from-primary-dark via-primary-dark/75 to-primary-dark/40" />

        <div className="section-container relative w-full pb-10 pt-24 sm:pb-12">
          <nav className="mb-6 flex items-center gap-2 text-sm text-white/70" aria-label="Breadcrumb">
            <Link to="/" className="transition hover:text-white">{t(language, 'navHome')}</Link>
            <span aria-hidden="true">/</span>
            <span className="text-white">{t(language, 'navTrees')}</span>
          </nav>

          <p className="text-xs font-semibold uppercase tracking-[0.25em] text-primary-light">
            {t(language, 'treesPageEyebrow')}
          </p>
          <h1 className="mt-2 max-w-3xl font-display text-4xl font-semibold leading-tight text-white sm:text-5xl">
            {t(language, 'exploreTrees')}
          </h1>
          <p className="mt-4 max-w-2xl text-base leading-relaxed text-white/85 sm:text-lg">
            {t(language, 'exploreTreesIntro')}
          </p>
        </div>
      </section>

      <section className="border-b border-gray-200 bg-white py-6">
        <div className="section-container space-y-4">
          <form onSubmit={handleSearchSubmit} className="flex flex-col gap-3 sm:flex-row">
            <label className="sr-only" htmlFor="explore-search">
              {t(language, 'searchTrees')}
            </label>
            <input
              id="explore-search"
              type="search"
              value={searchInput}
              onChange={(e) => setSearchInput(e.target.value)}
              placeholder={t(language, 'exploreSearchPlaceholder')}
              className="flex-1 rounded-xl border border-gray-300 px-4 py-3 text-sm focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
            />
            <button type="submit" className="btn btn-primary !rounded-xl !px-6">
              {t(language, 'search')}
            </button>
          </form>

          <div className="grid gap-3 sm:grid-cols-3">
            <label className="block text-xs font-semibold uppercase tracking-wide text-gray-500">
              {t(language, 'filterFamily')}
              <select
                value={family}
                onChange={(e) => updateParams({ family: e.target.value })}
                className="mt-1.5 w-full rounded-xl border border-gray-300 bg-white px-3 py-2.5 text-sm font-medium text-gray-800 focus:border-primary focus:outline-none"
              >
                <option value="">{t(language, 'filterAll')}</option>
                {filters.families?.map((item) => (
                  <option key={item} value={shortFamily(item)}>
                    {item}
                  </option>
                ))}
              </select>
            </label>

            <label className="block text-xs font-semibold uppercase tracking-wide text-gray-500">
              {t(language, 'filterCategory')}
              <select
                value={category}
                onChange={(e) => updateParams({ category: e.target.value })}
                className="mt-1.5 w-full rounded-xl border border-gray-300 bg-white px-3 py-2.5 text-sm font-medium text-gray-800 focus:border-primary focus:outline-none"
              >
                <option value="">{t(language, 'filterAll')}</option>
                {filters.categories?.map((item) => (
                  <option key={item} value={item}>
                    {categoryLabel(language, item)}
                  </option>
                ))}
              </select>
            </label>

            <label className="block text-xs font-semibold uppercase tracking-wide text-gray-500">
              {t(language, 'filterOrigin')}
              <select
                value={nativeStatus}
                onChange={(e) => updateParams({ nativeStatus: e.target.value })}
                className="mt-1.5 w-full rounded-xl border border-gray-300 bg-white px-3 py-2.5 text-sm font-medium text-gray-800 focus:border-primary focus:outline-none"
              >
                <option value="">{t(language, 'filterAll')}</option>
                {filters.nativeStatuses?.map((item) => (
                  <option key={item} value={item}>
                    {nativeLabel(language, item)}
                  </option>
                ))}
              </select>
            </label>
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
              {nativeStatus && (
                <button type="button" onClick={() => updateParams({ nativeStatus: '' })} className="rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary-dark">
                  {nativeLabel(language, nativeStatus)} ×
                </button>
              )}
              <button type="button" onClick={clearFilters} className="text-xs font-semibold text-gray-600 underline">
                {t(language, 'clearFilters')}
              </button>
            </div>
          )}
        </div>
      </section>

      <section className="py-12 sm:py-16">
        <div className="section-container">
          <div className="mb-6 flex flex-wrap items-end justify-between gap-3">
            <div>
              <h2 className="font-display text-2xl font-semibold text-primary-dark sm:text-3xl">
                {t(language, 'treesCatalogTitle')}
              </h2>
              <p className="mt-2 max-w-xl text-sm text-gray-500 sm:text-base">
                {t(language, 'treesCatalogSubtitle')}
              </p>
            </div>
            <span className="rounded-full bg-primary/10 px-4 py-2 text-sm font-semibold text-primary-dark">
              {t(language, 'showingTreesCount')
                .replace('{shown}', String(trees.length))
                .replace('{total}', String(totalElements))}
            </span>
          </div>

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
                {t(language, 'prevPage')}
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
                {t(language, 'nextPage')}
              </button>
            </nav>
          )}
        </div>
      </section>
    </div>
  );
}
