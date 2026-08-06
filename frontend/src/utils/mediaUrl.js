/**
 * Rewrite absolute API media URLs to same-origin paths so Vite's /uploads
 * and /media proxies work (avoids broken LAN IPs like 192.168.x:8082).
 */
export function resolveMediaUrl(url) {
  if (!url || typeof url !== 'string') return url;
  if (url.startsWith('/') || url.startsWith('data:') || url.startsWith('blob:')) {
    return url;
  }
  try {
    // In production (Vercel), we cannot rely on same-domain proxying.
    // Keep absolute /media and /uploads URLs intact so audio/video work.
    if (import.meta.env.PROD) return url;

    const parsed = new URL(url);
    if (
      parsed.pathname.startsWith('/uploads/')
      || parsed.pathname.startsWith('/media/')
    ) {
      return `${parsed.pathname}${parsed.search}`;
    }
  } catch {
    // keep original
  }
  return url;
}
