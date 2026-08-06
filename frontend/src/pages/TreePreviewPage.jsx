import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchTreeByQrCode, fetchTreeBySlug } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import TreeDetail from '../components/TreeDetail';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { resolveMediaUrl } from '../utils/mediaUrl';

/**
 * Public preview only — full guide unlocks by scanning the park QR (/scan/TREE-001).
 */
export default function TreePreviewPage() {
  const { slug } = useParams();
  const { language } = useLanguage();
  const [tree, setTree] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [imgFailed, setImgFailed] = useState(false);

  useEffect(() => {
    setLoading(true);
    setError(null);
    setImgFailed(false);
    const looksLikeTreeCode = /^tree[-_]?\d+$/i.test(String(slug || ''));
    const loadTree = looksLikeTreeCode
      ? fetchTreeByQrCode(slug, language).catch(() => fetchTreeBySlug(slug, language))
      : fetchTreeBySlug(slug, language).catch(() => fetchTreeByQrCode(slug, language));

    loadTree
      .then(setTree)
      .catch((err) => {
        setTree(null);
        setError(err.message);
      })
      .finally(() => setLoading(false));
  }, [slug, language]);

  if (loading) return <LoadingSpinner />;
  if (error || !tree) {
    return (
      <div className="section-container py-20 text-center">
        <p className="text-gray-600">{t(language, 'notFound')}</p>
        <Link to="/trees" className="btn btn-primary mt-4">{t(language, 'backToList')}</Link>
      </div>
    );
  }

  const imageUrl = resolveMediaUrl(tree.images?.find((img) => img.primary)?.url || tree.images?.[0]?.url);

  return (
    <div className="bg-surface">
      <div className="border-b border-primary/10 bg-white">
        <div className="section-container flex flex-wrap items-center gap-2 py-4 text-sm text-gray-500">
          <Link to="/" className="hover:text-primary-dark">{t(language, 'navHome')}</Link>
          <span aria-hidden="true">/</span>
          <Link to="/trees" className="hover:text-primary-dark">{t(language, 'exploreTrees')}</Link>
          <span aria-hidden="true">/</span>
          <span className="font-medium text-primary-dark">{tree.commonName}</span>
        </div>
      </div>

      <section className="section-container py-10 sm:py-14">
        <div className="mx-auto grid max-w-5xl gap-8 lg:grid-cols-2 lg:items-center">
          <div className="overflow-hidden rounded-3xl bg-cream ring-1 ring-gray-200/70">
            <div className="flex aspect-[4/3] items-center justify-center p-6">
              {imageUrl && !imgFailed ? (
                <img
                  src={imageUrl}
                  alt={tree.commonName}
                  className="max-h-full max-w-full object-contain"
                  onError={() => setImgFailed(true)}
                />
              ) : (
                <span className="text-7xl opacity-30" aria-hidden="true">🌳</span>
              )}
            </div>
          </div>

          <div>
            {tree.qrCodeId && (
              <p className="font-mono text-sm font-bold tracking-wide text-primary">{tree.qrCodeId}</p>
            )}
            <h1 className="mt-2 font-display text-3xl font-semibold text-primary-dark sm:text-4xl">
              {tree.commonName}
            </h1>
            <p className="mt-2 text-lg italic text-gray-500">{tree.scientificName}</p>
            {tree.family && <p className="mt-2 text-sm text-gray-400">{tree.family}</p>}

            <div className="mt-8 rounded-2xl border border-amber-200 bg-amber-50 px-5 py-5">
              <p className="text-xs font-semibold uppercase tracking-[0.2em] text-amber-800">
                {t(language, 'previewNoticeTitle')}
              </p>
              <h2 className="mt-2 text-xl font-bold text-amber-950">{t(language, 'scanAtParkTitle')}</h2>
              <p className="mt-3 text-sm leading-relaxed text-amber-950/90">
                {t(language, 'previewNoticeText')}
              </p>
              <p className="mt-4 text-sm font-medium text-amber-900">{t(language, 'scanAtParkSteps')}</p>
            </div>

            <div className="mt-6 flex flex-wrap gap-3">
              <Link to="/trees" className="btn btn-secondary !rounded-xl">
                {t(language, 'backToList')}
              </Link>
              <Link to="/map" className="btn btn-primary !rounded-xl">
                {t(language, 'navMap')}
              </Link>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
