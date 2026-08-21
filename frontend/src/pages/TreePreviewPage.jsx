import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchTreeBySlug, fetchTreeByQrCode } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { resolveMediaUrl } from '../utils/mediaUrl';
import { displayCommonName } from '../utils/treeDisplay';
import { TreeSpeciesHeading } from '../components/TreeSpeciesHeading';

/**
 * Public tree information page — no QR generate/print/download (admin-only assets).
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
  const commonName = displayCommonName(tree, language);
  const summary = tree.shortDescription || tree.description || t(language, 'featuredTreeFallback');

  return (
    <div className="bg-surface">
      <div className="border-b border-primary/10 bg-white">
        <div className="section-container flex flex-wrap items-center gap-2 py-4 text-sm text-gray-500">
          <Link to="/" className="hover:text-primary-dark">{t(language, 'navHome')}</Link>
          <span aria-hidden="true">/</span>
          <Link to="/trees" className="hover:text-primary-dark">{t(language, 'exploreTrees')}</Link>
          <span aria-hidden="true">/</span>
          <span className="font-medium text-primary-dark">{tree.scientificName || commonName}</span>
        </div>
      </div>

      <section className="section-container py-10 sm:py-14">
        <div className="mx-auto grid max-w-5xl gap-8 lg:grid-cols-2 lg:items-start">
          <div className="relative overflow-hidden rounded-3xl bg-cream ring-1 ring-gray-200/70">
            <div className="flex aspect-[4/3] items-center justify-center p-6">
              {imageUrl && !imgFailed ? (
                <img
                  src={imageUrl}
                  alt={tree.scientificName || commonName}
                  className="max-h-full max-w-full object-contain"
                  onError={() => setImgFailed(true)}
                />
              ) : (
                <span className="text-7xl opacity-30" aria-hidden="true">🌳</span>
              )}
            </div>
            <div className="absolute inset-x-0 bottom-0 bg-gradient-to-t from-primary-dark/95 via-primary-dark/70 to-transparent px-5 pb-5 pt-16 sm:px-6 sm:pb-6">
              <TreeSpeciesHeading
                scientificName={tree.scientificName}
                commonName={commonName}
                family={tree.family}
                variant="hero"
                inverted
              />
            </div>
          </div>

          <div>
            <TreeSpeciesHeading
              scientificName={tree.scientificName}
              commonName={commonName}
              family={tree.family}
              variant="inline"
            />
            <p className="mt-4 text-sm leading-relaxed text-gray-600 whitespace-pre-line">{summary}</p>
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
