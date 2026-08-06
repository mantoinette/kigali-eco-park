import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { resolveMediaUrl } from '../utils/mediaUrl';

/**
 * Catalogue summary card — no QR on the listing.
 * Detail URL uses tree code so it matches printed QR destinations (/trees/TREE-001).
 */
export default function TreeCard({ tree }) {
  const { language } = useLanguage();
  const [imgFailed, setImgFailed] = useState(false);

  useEffect(() => {
    setImgFailed(false);
  }, [tree.primaryImageUrl, tree.qrCodeId, tree.slug]);

  const treePage = `/trees/${tree.qrCodeId || tree.slug}`;
  const imageUrl = resolveMediaUrl(tree.primaryImageUrl);

  return (
    <article className="group flex h-full flex-col overflow-hidden rounded-2xl bg-white shadow-card ring-1 ring-gray-200/60 transition hover:-translate-y-0.5 hover:shadow-showcase">
      <div className="flex items-center justify-between gap-2 bg-gradient-to-r from-primary-dark to-primary px-4 py-2.5">
        <span className="text-[10px] font-bold tracking-widest text-white/90">KIGALI ECO-PARK</span>
        {tree.qrCodeId && (
          <span className="rounded-full bg-white/20 px-2.5 py-0.5 font-mono text-[10px] font-bold text-white">
            {tree.qrCodeId}
          </span>
        )}
      </div>

      <Link to={treePage} className="block bg-cream" aria-label={tree.commonName}>
        <div className="flex aspect-[4/3] items-center justify-center p-4">
          {imageUrl && !imgFailed ? (
            <img
              src={imageUrl}
              alt={tree.commonName}
              className="max-h-full max-w-full object-contain transition duration-300 group-hover:scale-[1.02]"
              onError={() => setImgFailed(true)}
            />
          ) : (
            <span className="text-5xl opacity-30" aria-hidden="true">🌳</span>
          )}
        </div>
      </Link>

      <div className="flex flex-1 flex-col p-4">
        <div className="min-w-0 flex-1">
          <h3 className="font-display text-lg font-semibold leading-snug text-primary-dark">
            {tree.commonName}
          </h3>
          <p className="mt-0.5 text-sm italic text-gray-500">{tree.scientificName}</p>
          {tree.family && (
            <p className="mt-1 truncate text-xs text-gray-400">{tree.family}</p>
          )}
        </div>

        {tree.shortDescription && (
          <p className="mt-3 mb-4 line-clamp-2 text-sm leading-relaxed text-gray-600">
            {tree.shortDescription}
          </p>
        )}

        <div className="mt-auto">
          <Link
            to={treePage}
            className="block w-full rounded-xl bg-primary py-2.5 text-center text-sm font-semibold text-white transition hover:bg-primary-dark"
          >
            {t(language, 'viewTreeDetails')}
          </Link>
        </div>
      </div>
    </article>
  );
}
