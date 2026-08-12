import { resolveMediaUrl } from './mediaUrl';

const MEDIA_LANGS = ['en', 'rw', 'fr'];

function normalizeLang(language) {
  const code = (language || 'en').toLowerCase();
  return MEDIA_LANGS.includes(code) ? code : 'en';
}

function mediaOriginFromTree(tree) {
  const sample = tree?.audioUrl || tree?.videoUrl;
  if (!sample || !sample.startsWith('http')) return '';
  try {
    return new URL(sample).origin;
  } catch {
    return '';
  }
}

function appendTreeQuery(url, treeId) {
  if (!url || !treeId) return url;
  const sep = url.includes('?') ? '&' : '?';
  return `${url}${sep}tree=${encodeURIComponent(treeId)}`;
}

/** Swap -en.mp3 / -fr.mp4 suffix to match visitor language. */
function swapMediaLanguage(url, language, ext) {
  if (!url) return null;
  const resolved = resolveMediaUrl(url);
  const lang = normalizeLang(language);
  const suffixPattern = new RegExp(`-([a-z]{2})\\.${ext}$`, 'i');
  if (suffixPattern.test(resolved)) {
    return resolved.replace(suffixPattern, `-${lang}.${ext}`);
  }
  const barePattern = new RegExp(`\\.${ext}$`, 'i');
  if (barePattern.test(resolved)) {
    return resolved.replace(barePattern, `-${lang}.${ext}`);
  }
  return resolved;
}

function buildFromQrCodeId(tree, language, kind, ext) {
  if (!tree?.qrCodeId) return null;
  const lang = normalizeLang(language);
  const path = `/media/${kind}/${tree.qrCodeId}-${lang}.${ext}`;
  const origin = mediaOriginFromTree(tree);
  return resolveMediaUrl(origin ? `${origin}${path}` : path);
}

export function resolveTreeAudioUrl(tree, language = 'en') {
  if (!tree?.audioUrl && !tree?.qrCodeId) return null;
  const fromQr = buildFromQrCodeId(tree, language, 'audio', 'mp3');
  const fromBase = tree?.audioUrl
    ? swapMediaLanguage(tree.audioUrl, language, 'mp3')
    : null;
  const url = fromQr || fromBase;
  return appendTreeQuery(url, tree?.qrCodeId);
}

export function resolveTreeVideoUrl(tree, language = 'en') {
  if (!tree?.videoUrl && !tree?.qrCodeId) return null;
  if (tree?.videoUrl?.startsWith('internal:')) return tree.videoUrl;
  if (tree?.videoUrl && (tree.videoUrl.includes('youtube.com') || tree.videoUrl.includes('youtu.be'))) {
    return tree.videoUrl;
  }
  const fromQr = buildFromQrCodeId(tree, language, 'video', 'mp4');
  const fromBase = tree?.videoUrl
    ? swapMediaLanguage(tree.videoUrl, language, 'mp4')
    : null;
  const url = fromQr || fromBase;
  return appendTreeQuery(url, tree?.qrCodeId);
}

/** Fallback order when a language file is missing on the server. */
export function nextAudioFallbackLang(language, tried) {
  const order = [normalizeLang(language), ...MEDIA_LANGS.filter((l) => l !== normalizeLang(language))];
  return order.find((l) => !tried.has(l)) || null;
}

export function nextVideoFallbackLang(language, tried) {
  return nextAudioFallbackLang(language, tried);
}

export function isYoutubeEmbed(url) {
  return url && (url.includes('youtube.com') || url.includes('youtu.be'));
}
