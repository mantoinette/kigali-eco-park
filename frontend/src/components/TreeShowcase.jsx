import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchQrCode } from '../api/client';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { resolveMediaUrl } from '../utils/mediaUrl';

/**
 * Unified park interpretive sign — tree illustration and QR label
 * are one visual unit, like the physical signage at Kigali Eco Park.
 */
export default function TreeShowcase({ tree }) {
  const { language } = useLanguage();
  const [qr, setQr] = useState(null);
  const [imgFailed, setImgFailed] = useState(false);
  const imageUrl = resolveMediaUrl(tree.primaryImageUrl);

  useEffect(() => {
    setImgFailed(false);
  }, [tree.primaryImageUrl]);

  useEffect(() => {
    fetchQrCode(tree.slug)
      .then(setQr)
      .catch(() => setQr(null));
  }, [tree.slug]);

  return (
    <article className="mx-auto max-w-4xl overflow-hidden rounded-[1.75rem] bg-white shadow-showcase ring-1 ring-gray-200/60">
      {/* Park sign header */}
      <div className="flex items-center justify-between gap-3 bg-gradient-to-r from-primary-dark to-primary px-5 py-3.5 text-white sm:px-7">
        <div className="flex items-center gap-2.5">
          <span className="flex h-8 w-8 items-center justify-center rounded-full bg-white/15 text-sm" aria-hidden="true">🌿</span>
          <span className="text-xs font-bold tracking-[0.12em] sm:text-sm">TREE SCAN RWANDA</span>
        </div>
        <span className="rounded-full bg-white/15 px-3 py-1 text-[10px] font-semibold uppercase tracking-wider sm:text-xs">
          {t(language, 'indigenousTree')}
        </span>
      </div>

      {/* Tree + QR — single specimen panel */}
      <div className="relative bg-gradient-to-b from-cream to-[#ebe6dc]">
        <div className="flex items-center justify-center px-6 pb-2 pt-8 sm:px-12 sm:pt-10 sm:pb-4">
          {imageUrl && !imgFailed ? (
            <img
              src={imageUrl}
              alt={tree.commonName}
              className="max-h-[min(52vw,22rem)] w-full max-w-lg object-contain drop-shadow-lg sm:max-h-[26rem]"
              onError={() => setImgFailed(true)}
            />
          ) : (
            <span className="py-20 text-8xl opacity-30" aria-hidden="true">🌳</span>
          )}
        </div>

        {/* Integrated label strip — part of the same panel as the tree */}
        <div className="mx-4 mb-4 overflow-hidden rounded-2xl border border-primary/20 bg-white shadow-md sm:mx-8 sm:mb-8">
          <div className="flex items-stretch">
            <div className="flex w-[5px] shrink-0 bg-primary" aria-hidden="true" />

            <div className="flex flex-1 flex-col gap-4 p-4 sm:flex-row sm:items-center sm:gap-6 sm:p-5">
              <div className="flex shrink-0 items-center justify-center self-center rounded-xl border-2 border-primary/10 bg-cream p-2 sm:self-auto">
                {qr ? (
                  <img
                    src={qr.qrCodeBase64}
                    alt={t(language, 'scanWithPhoneAlt')}
                    className="h-20 w-20 sm:h-24 sm:w-24"
                  />
                ) : (
                  <div className="flex h-20 w-20 items-center justify-center sm:h-24 sm:w-24">
                    <div className="h-7 w-7 animate-spin rounded-full border-2 border-primary/20 border-t-primary" />
                  </div>
                )}
              </div>

              <div className="min-w-0 flex-1 text-center sm:text-left">
                <h2 className="font-display text-2xl font-semibold leading-tight text-primary-dark sm:text-3xl">
                  {tree.commonName}
                </h2>
                <p className="mt-1 font-display text-base italic text-gray-500 sm:text-lg">
                  {tree.scientificName}
                </p>
                {tree.family && (
                  <p className="mt-2 text-sm text-gray-500">{tree.family}</p>
                )}
              </div>

              {qr?.treeId && (
                <div className="hidden shrink-0 flex-col items-center justify-center rounded-xl bg-primary/5 px-4 py-3 sm:flex">
                  <span className="text-[10px] font-semibold uppercase tracking-widest text-gray-400">ID</span>
                  <span className="font-mono text-sm font-bold text-primary-dark">{qr.treeId}</span>
                </div>
              )}
            </div>
          </div>

          <div className="border-t border-gray-100 bg-primary/[0.03] px-4 py-3 sm:px-5">
            <p className="flex items-center justify-center gap-2 text-center text-sm text-primary-dark sm:justify-start">
              <svg className="h-4 w-4 shrink-0 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2} aria-hidden="true">
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
              </svg>
              <span>{t(language, 'qrLabelDescription')}</span>
            </p>
          </div>
        </div>
      </div>

      {/* Description & actions */}
      <div className="border-t border-gray-100 px-6 py-7 sm:px-8 sm:py-8">
        <p className="text-center text-base leading-relaxed text-gray-600 sm:text-left">
          {tree.shortDescription || t(language, 'featuredTreeFallback')}
        </p>

        <div className="mt-7 flex flex-col gap-3 sm:flex-row sm:justify-center">
          <Link
            to={qr?.treeId ? `/scan/${qr.treeId}` : `/trees/${tree.slug}`}
            className="btn btn-primary !rounded-xl !py-3 text-center sm:min-w-[180px]"
          >
            {t(language, 'viewDetails')}
          </Link>
          <Link
            to="/map"
            className="inline-flex items-center justify-center rounded-xl border border-gray-200 px-6 py-3 text-sm font-semibold text-primary-dark transition hover:border-primary/30 hover:bg-primary/5 sm:min-w-[180px]"
          >
            {t(language, 'viewOnMap')}
          </Link>
        </div>
      </div>
    </article>
  );
}
