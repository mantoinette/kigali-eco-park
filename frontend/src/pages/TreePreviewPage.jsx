import { useEffect, useMemo, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchQrCode, fetchTreeByQrCode, fetchTreeBySlug, fetchTrees } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import TreeDetail from '../components/TreeDetail';
import TreeA4PrintTemplate from '../components/TreeA4PrintTemplate';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

/**
 * Dedicated Tree Details page structure:
 * Information → Images → Uses → Print Tree Sign (A4) → QR Code
 * Accepts /trees/{qrCodeId} (TREE-001) or /trees/{slug}.
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
    const looksLikeTreeCode = /^tree[-_]?\d+$/i.test(String(slug || ''));
    const loadTree = looksLikeTreeCode
      ? fetchTreeByQrCode(slug, language).catch(() => fetchTreeBySlug(slug, language))
      : fetchTreeBySlug(slug, language).catch(() => fetchTreeByQrCode(slug, language));

    Promise.all([
      loadTree,
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
      <div className="no-print border-b border-primary/10 bg-white">
        <div className="section-container flex flex-wrap items-center justify-between gap-3 py-4">
          <nav className="flex flex-wrap items-center gap-2 text-sm text-gray-500" aria-label="Breadcrumb">
            <Link to="/" className="hover:text-primary-dark">{t(language, 'navHome')}</Link>
            <span aria-hidden="true">/</span>
            <Link to="/trees" className="hover:text-primary-dark">{t(language, 'exploreTrees')}</Link>
            <span aria-hidden="true">/</span>
            <span className="font-medium text-primary-dark">{tree.commonName}</span>
          </nav>

          <div className="flex flex-wrap items-center gap-3">
            {siblings.index >= 0 && (
              <p className="text-xs text-gray-400">
                {t(language, 'treeOfTotal')
                  .replace('{current}', String(siblings.index + 1))
                  .replace('{total}', String(allTrees.length))}
              </p>
            )}
            <a
              href="#print-tree-sign"
              className="rounded-xl border border-primary/25 bg-primary/5 px-3 py-1.5 text-xs font-semibold text-primary-dark hover:bg-primary/10"
            >
              {t(language, 'jumpToPrintSign')}
            </a>
          </div>
        </div>
      </div>

      {/* Tree Details: Information · Images · Uses · (media / map) */}
      <TreeDetail tree={tree} loading={false} error={null} />

      {/* Print Tree Sign (A4) + QR Code — not shown on Explore Trees listing */}
      <section id="print-tree-sign" className="border-t border-primary/10 bg-white py-12">
        <div className="section-container">
          <div className="mx-auto max-w-4xl">
            <div className="no-print mb-8 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
              <div>
                <p className="text-xs font-semibold uppercase tracking-[0.2em] text-primary">
                  {t(language, 'treeDetailsTitle')}
                </p>
                <h2 className="mt-2 font-display text-2xl font-semibold text-primary-dark sm:text-3xl">
                  {t(language, 'printTreeSign')}
                </h2>
                <p className="mt-2 max-w-xl text-sm text-gray-600">{t(language, 'printTreeSignHint')}</p>
              </div>
              <div className="flex flex-wrap gap-3">
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={() => window.print()}
                  disabled={!qr}
                >
                  {qr ? 'Print / Save as PDF' : 'Loading QR…'}
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => window.print()}
                  disabled={!qr}
                >
                  {qr ? 'Download PDF' : 'Loading…'}
                </button>
              </div>
            </div>

            <div className="grid gap-8 lg:grid-cols-[1fr_220px] lg:items-start">
              <TreeA4PrintTemplate tree={tree} qr={qr} />

              <aside className="no-print rounded-2xl border border-gray-200 bg-surface p-5">
                <h3 className="text-lg font-bold text-primary-dark">{t(language, 'treeQrCode')}</h3>
                <p className="mt-2 text-sm text-gray-600">{t(language, 'treeQrCodeHint')}</p>
                <div className="mt-4 flex justify-center rounded-xl bg-white p-3 ring-1 ring-gray-100">
                  {qr?.qrCodeBase64 ? (
                    <img
                      src={qr.qrCodeBase64}
                      alt={t(language, 'scanWithPhoneAlt')}
                      className="h-40 w-40"
                    />
                  ) : (
                    <div className="flex h-40 w-40 items-center justify-center">
                      <div className="h-8 w-8 animate-spin rounded-full border-2 border-primary/30 border-t-primary" />
                    </div>
                  )}
                </div>
                {tree.qrCodeId && (
                  <p className="mt-3 text-center font-mono text-sm font-bold text-primary-dark">
                    {tree.qrCodeId}
                  </p>
                )}
                {qr?.url && (
                  <code className="mt-3 block break-all rounded-lg bg-white px-2 py-2 text-[10px] text-gray-500 ring-1 ring-gray-100">
                    {qr.url}
                  </code>
                )}
              </aside>
            </div>
          </div>
        </div>
      </section>

      <div className="no-print border-t border-gray-200 bg-surface py-8">
        <div className="section-container flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          {siblings.prev ? (
            <Link
              to={`/trees/${siblings.prev.qrCodeId || siblings.prev.slug}`}
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
              to={`/trees/${siblings.next.qrCodeId || siblings.next.slug}`}
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
