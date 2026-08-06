import { useEffect, useMemo, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchQrCode, fetchTreeByQrCode, fetchTreeBySlug, fetchTrees } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import TreeDetail from '../components/TreeDetail';
import TreeA4PrintTemplate from '../components/TreeA4PrintTemplate';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

/**
 * Dedicated page for one tree only — same full guide as a QR scan,
 * addressed by slug for catalog links and future species pages.
 */
export default function TreePreviewPage() {
  const { slug } = useParams();
  const { language } = useLanguage();
  const [tree, setTree] = useState(null);
  const [allTrees, setAllTrees] = useState([]);
  const [qr, setQr] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    setError(null);
    Promise.all([
      fetchTreeBySlug(slug, language).catch(() => fetchTreeByQrCode(slug, language)),
      fetchTrees(language).catch(() => []),
    ])
      .then(([detail, list]) => {
        setTree(detail);
        setAllTrees(list || []);
      })
      .catch((err) => {
        setTree(null);
        setError(err.message);
      })
      .finally(() => setLoading(false));
  }, [slug, language]);

  useEffect(() => {
    if (!tree?.slug) return;
    setQr(null);
    fetchQrCode(tree.slug)
      .then(setQr)
      .catch(() => setQr(null));
  }, [tree?.slug]);

  const siblings = useMemo(() => {
    if (!allTrees.length) return { prev: null, next: null, index: -1 };
    // Support both URLs:
    // - /trees/{slug} (species slug)
    // - /trees/{qrCodeId} (TREE-001, TREE-002, ...)
    const identifierLower = String(slug || '').toLowerCase();
    const isSlugUrl = allTrees.some((item) => String(item.slug || '').toLowerCase() === identifierLower);
    const index = isSlugUrl
      ? allTrees.findIndex((item) => String(item.slug || '').toLowerCase() === identifierLower)
      : allTrees.findIndex((item) => String(item.qrCodeId || '').toLowerCase() === identifierLower);

    return {
      index,
      prev: index > 0 ? allTrees[index - 1] : null,
      next: index >= 0 && index < allTrees.length - 1 ? allTrees[index + 1] : null,
    };
  }, [allTrees, slug]);

  if (loading) return <LoadingSpinner />;
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
      <div className="border-b border-primary/10 bg-white">
        <div className="section-container flex flex-col gap-4 py-6 sm:flex-row sm:items-start sm:justify-between">
          <div>
            <p className="text-xs text-gray-500">{tree?.qrCodeId ? `Tree ${tree.qrCodeId}` : null}</p>
            <h1 className="mt-1 text-2xl font-bold text-gray-900">{tree?.commonName}</h1>
            <p className="mt-1 text-sm text-gray-600">
              <em>{tree?.scientificName}</em>
            </p>
          </div>

          <div className="no-print flex flex-col gap-3 sm:items-end">
            <button
              type="button"
              className="btn btn-primary"
              onClick={() => window.print()}
              disabled={!qr}
              title={!qr ? 'Loading QR code…' : 'Print this tree sign'}
            >
              {qr ? 'Print / Save as PDF' : 'Loading QR…'}
            </button>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => window.print()}
              disabled={!qr}
              title={!qr ? 'Loading QR code…' : 'Download (save) as PDF via your browser print dialog'}
            >
              {qr ? 'Download PDF' : 'Loading…'}
            </button>
            <p className="text-xs text-gray-500">Optimized for A4 portrait lamination</p>
          </div>
        </div>
      </div>

      <div className="section-container py-8">
        {/* Printable A4 sign (this is what will appear when you print) */}
        <TreeA4PrintTemplate tree={tree} qr={qr} />
      </div>

      <div className="border-b border-primary/10 bg-white">
        <div className="section-container flex flex-wrap items-center justify-between gap-3 py-4">
          <nav className="flex flex-wrap items-center gap-2 text-sm text-gray-500" aria-label="Breadcrumb">
            <Link to="/" className="hover:text-primary-dark">{t(language, 'navHome')}</Link>
            <span aria-hidden="true">/</span>
            <Link to="/trees" className="hover:text-primary-dark">{t(language, 'navTrees')}</Link>
            <span aria-hidden="true">/</span>
            <span className="font-medium text-primary-dark">{tree.commonName}</span>
          </nav>

          {siblings.index >= 0 && (
            <p className="text-xs text-gray-400">
              {t(language, 'treeOfTotal')
                .replace('{current}', String(siblings.index + 1))
                .replace('{total}', String(allTrees.length))}
            </p>
          )}
        </div>
      </div>

      <TreeDetail tree={tree} loading={false} error={null} />

      <div className="border-t border-gray-200 bg-surface py-8">
        <div className="section-container flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          {siblings.prev ? (
            <Link
              to={allTrees.some((item) => String(item.slug || '').toLowerCase() === String(slug || '').toLowerCase())
                ? `/trees/${siblings.prev.slug}`
                : `/trees/${siblings.prev.qrCodeId}`}
              className="rounded-xl border border-gray-200 bg-white px-4 py-3 text-sm font-semibold text-primary-dark transition hover:border-primary/30"
            >
              ← {siblings.prev.commonName}
            </Link>
          ) : (
            <span />
          )}

          <Link to="/trees" className="btn btn-secondary !rounded-xl text-center">
            {t(language, 'backToList')}
          </Link>

          {siblings.next ? (
            <Link
              to={allTrees.some((item) => String(item.slug || '').toLowerCase() === String(slug || '').toLowerCase())
                ? `/trees/${siblings.next.slug}`
                : `/trees/${siblings.next.qrCodeId}`}
              className="rounded-xl border border-gray-200 bg-white px-4 py-3 text-right text-sm font-semibold text-primary-dark transition hover:border-primary/30 sm:text-left"
            >
              {siblings.next.commonName} →
            </Link>
          ) : (
            <span />
          )}
        </div>
      </div>
    </div>
  );
}
