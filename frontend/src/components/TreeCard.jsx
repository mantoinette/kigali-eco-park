import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchQrCode } from '../api/client';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { resolveMediaUrl } from '../utils/mediaUrl';

/** One tree = one card = one dedicated page. */
export default function TreeCard({ tree }) {
  const { language } = useLanguage();
  const [qr, setQr] = useState(null);
  const [imgFailed, setImgFailed] = useState(false);

  useEffect(() => {
    setImgFailed(false);
  }, [tree.primaryImageUrl, tree.slug]);

  useEffect(() => {
    fetchQrCode(tree.slug)
      .then(setQr)
      .catch(() => setQr(null));
  }, [tree.slug]);

  const treePage = `/trees/${tree.slug}`;
  const imageUrl = resolveMediaUrl(tree.primaryImageUrl);

  return (
    <article className="group flex h-full flex-col overflow-hidden rounded-2xl bg-white shadow-card ring-1 ring-gray-200/60 transition hover:-translate-y-0.5 hover:shadow-showcase">
      <div className="flex items-center justify-between gap-2 bg-gradient-to-r from-primary-dark to-primary px-4 py-2.5">
        <span className="text-[10px] font-bold tracking-widest text-white/90">KIGALI ECO-PARK</span>
        {qr?.treeId && (
          <span className="rounded-full bg-white/20 px-2.5 py-0.5 font-mono text-[10px] font-bold text-white">
            {qr.treeId}
          </span>
        )}
      </div>

      <Link to={treePage} className="block bg-cream">
        <div className="flex aspect-[4/3] items-center justify-center p-4">
          {imageUrl && !imgFailed ? (
            <img
              src={imageUrl}
              alt={tree.commonName}
              className="max-h-full max-w-full object-contain transition duration-300 group-hover:scale-[1.02]"
              onError={() => setImgFailed(true)}
            />
          ) : (
            <span className="text-5xl opacity-30">🌳</span>
          )}
        </div>
      </Link>

      <div className="flex flex-1 flex-col p-4">
        <div className="mb-3 flex items-start gap-3">
          {qr && (
            <img
              src={qr.qrCodeBase64}
              alt=""
              className="h-14 w-14 shrink-0 rounded-lg border border-gray-100 bg-white p-0.5"
            />
          )}
          <div className="min-w-0 flex-1">
            <h3 className="font-display text-lg font-semibold leading-snug text-primary-dark">
              {tree.commonName}
            </h3>
            <p className="mt-0.5 text-sm italic text-gray-500">{tree.scientificName}</p>
            {tree.family && (
              <p className="mt-1 truncate text-xs text-gray-400">{tree.family}</p>
            )}
          </div>
        </div>

        {tree.shortDescription && (
          <p className="mb-4 line-clamp-2 text-sm leading-relaxed text-gray-600">
            {tree.shortDescription}
          </p>
        )}

        <div className="mt-auto space-y-2">
          <Link
            to={treePage}
            className="block w-full rounded-xl bg-primary py-2.5 text-center text-sm font-semibold text-white transition hover:bg-primary-dark"
          >
            {t(language, 'openTreePage')}
          </Link>
          {qr?.treeId && (
            <Link
              to={`/scan/${qr.treeId}`}
              className="block w-full rounded-xl border border-primary/25 py-2 text-center text-xs font-semibold text-primary-dark transition hover:bg-primary/5"
            >
              {t(language, 'fullGuideAfterScan')}
            </Link>
          )}
        </div>
      </div>
    </article>
  );
}
