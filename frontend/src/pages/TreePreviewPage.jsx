import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { fetchQrCode, fetchTreeByQrCode, fetchTreeBySlug } from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { resolveMediaUrl } from '../utils/mediaUrl';
import { displayCommonName } from '../utils/treeDisplay';
import { TreeSpeciesHeading } from '../components/TreeSpeciesHeading';

/**
 * Public preview only — full guide unlocks by scanning the park QR (/scan/TREE-001).
 * Shows the same QR image that is printed and attached to the tree.
 */
export default function TreePreviewPage() {
  const { slug } = useParams();
  const { language } = useLanguage();
  const [tree, setTree] = useState(null);
  const [qr, setQr] = useState(null);
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
      .then((detail) => {
        setTree(detail);
        if (detail?.slug) {
          return fetchQrCode(detail.slug).then(setQr).catch(() => setQr(null));
        }
        setQr(null);
        return null;
      })
      .catch((err) => {
        setTree(null);
        setQr(null);
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
        <div className="mx-auto grid max-w-5xl gap-8 lg:grid-cols-2 lg:items-center">
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

            <div className="mt-8 rounded-2xl border border-primary/20 bg-white px-5 py-5 shadow-sm ring-1 ring-primary/10">
              <p className="text-xs font-semibold uppercase tracking-[0.2em] text-primary">
                {t(language, 'qrOnTreeLabel')}
              </p>
              <h2 className="mt-2 text-xl font-bold text-primary-dark">{t(language, 'scanWithPhoneTitle')}</h2>
              <p className="mt-2 text-sm leading-relaxed text-gray-600">
                {t(language, 'treeQrAttachHint')}
              </p>

              <div className="mt-5 flex flex-col items-center gap-4 sm:flex-row sm:items-start">
                <div className="rounded-2xl border-2 border-primary/25 bg-white p-3 shadow-sm">
                  {qr?.qrCodeBase64 ? (
                    <img
                      src={qr.qrCodeBase64}
                      alt={t(language, 'scanWithPhoneAlt')}
                      className="h-44 w-44 sm:h-48 sm:w-48"
                    />
                  ) : (
                    <div className="flex h-44 w-44 items-center justify-center sm:h-48 sm:w-48">
                      <div className="h-8 w-8 animate-spin rounded-full border-2 border-primary/30 border-t-primary" />
                    </div>
                  )}
                </div>
                <div className="min-w-0 flex-1 text-center sm:pt-2 sm:text-left">
                  <p className="text-sm font-medium text-primary-dark">{t(language, 'scanWithPhoneHint')}</p>
                  <p className="mt-3 font-mono text-sm font-bold tracking-wide text-primary">
                    {tree.qrCodeId}
                  </p>
                  {qr?.url && (
                    <p className="mt-2 break-all text-xs text-gray-500">{qr.url}</p>
                  )}
                  <div className="mt-4 flex flex-wrap justify-center gap-2 sm:justify-start">
                    {tree.slug && (
                      <Link to={`/qr-label/${tree.slug}`} className="btn btn-primary !rounded-xl !px-4 !py-2 text-xs">
                        {t(language, 'printLabel')}
                      </Link>
                    )}
                    {qr?.treeId && (
                      <Link to={`/scan/${qr.treeId}`} className="btn btn-secondary !rounded-xl !px-4 !py-2 text-xs">
                        {t(language, 'fullGuideAfterScan')}
                      </Link>
                    )}
                  </div>
                </div>
              </div>
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
