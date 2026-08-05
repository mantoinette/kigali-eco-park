import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { MapContainer, TileLayer, Marker } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import { fetchTrees } from '../api/client';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { resolveMediaUrl } from '../utils/mediaUrl';
import LoadingSpinner from './LoadingSpinner';

function isYoutubeEmbed(url) {
  return url && (url.includes('youtube.com') || url.includes('youtu.be'));
}

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: markerIcon2x,
  iconUrl: markerIcon,
  shadowUrl: markerShadow,
});

function InfoSection({ title, content, icon }) {
  if (!content) return null;
  const paragraphs = content.split('\n\n').filter(Boolean);
  return (
    <section className="card">
      <div className="mb-4 flex items-center gap-3">
        <span className="text-2xl" aria-hidden="true">{icon}</span>
        <h2 className="text-xl font-bold text-primary-dark">{title}</h2>
      </div>
      <div className="space-y-3 text-gray-700 leading-relaxed">
        {paragraphs.map((p) => <p key={p.slice(0, 40)}>{p}</p>)}
      </div>
    </section>
  );
}

function BulletSection({ title, content, icon }) {
  if (!content) return null;
  const items = content.split('\n').filter(Boolean);
  return (
    <section className="card">
      <div className="mb-4 flex items-center gap-3">
        <span className="text-2xl" aria-hidden="true">{icon}</span>
        <h2 className="text-xl font-bold text-primary-dark">{title}</h2>
      </div>
      <ul className="grid gap-2 sm:grid-cols-2">
        {items.map((item) => (
          <li key={item} className="flex items-start gap-2 text-sm text-gray-700">
            <span className="mt-1 text-primary">✓</span>
            <span>{item.replace(/^[•\-]\s*/, '')}</span>
          </li>
        ))}
      </ul>
    </section>
  );
}

function resolveAudioUrl(audioUrl, language) {
  if (!audioUrl) return null;
  const resolved = resolveMediaUrl(audioUrl);
  const match = resolved.match(/^(.*\/media\/audio\/[^/]+)-([a-z]{2})\.mp3$/i);
  if (!match) return resolved;
  return `${match[1]}-${language}.mp3`;
}

function resolveVideoUrl(videoUrl, language) {
  if (!videoUrl || videoUrl.startsWith('internal:')) return videoUrl;
  if (isYoutubeEmbed(videoUrl)) return videoUrl;

  const resolved = resolveMediaUrl(videoUrl);
  const withLang = resolved.match(/^(.*\/media\/video\/[^/]+)-([a-z]{2})\.mp4$/i);
  if (withLang) {
    return `${withLang[1]}-${language}.mp4`;
  }

  const bare = resolved.match(/^(.*\/media\/video\/[^/]+)\.mp4$/i);
  if (bare) {
    return `${bare[1]}-${language}.mp4`;
  }

  return resolved;
}

function TreeHeroImage({ src, alt }) {
  const [failed, setFailed] = useState(false);
  if (!src || failed) {
    return (
      <div className="flex h-full items-center justify-center bg-primary/10 text-8xl">🌳</div>
    );
  }
  return (
    <img
      src={src}
      alt={alt}
      className="h-full w-full object-cover"
      onError={() => setFailed(true)}
    />
  );
}

function GalleryImage({ src, alt, className }) {
  const [failed, setFailed] = useState(false);
  if (failed) {
    return (
      <div className={`flex items-center justify-center bg-primary/10 text-3xl ${className}`}>🌿</div>
    );
  }
  return (
    <img src={src} alt={alt} className={className} onError={() => setFailed(true)} loading="lazy" />
  );
}
function TreeLocationMap({ lat, lng, name, language }) {
  if (lat == null || lng == null) return null;
  return (
    <section className="card overflow-hidden !p-0">
      <div className="p-6 pb-0">
        <h2 className="text-xl font-bold text-primary-dark">📍 {t(language, 'treeLocation')}</h2>
        <p className="mt-1 text-sm text-gray-600">{name}</p>
      </div>
      <MapContainer center={[lat, lng]} zoom={17} scrollWheelZoom={false} className="mt-4 h-64 w-full">
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        <Marker position={[lat, lng]} />
      </MapContainer>
    </section>
  );
}

export default function TreeDetail({ tree, loading, error }) {
  const { language } = useLanguage();
  const [activeImage, setActiveImage] = useState(null);
  const [audioSrc, setAudioSrc] = useState(null);
  const [videoSrc, setVideoSrc] = useState(null);
  const [otherTrees, setOtherTrees] = useState([]);

  useEffect(() => {
    if (tree?.images?.length) {
      const primary = tree.images.find((img) => img.primary) || tree.images[0];
      setActiveImage(primary);
    }
  }, [tree]);

  useEffect(() => {
    if (!tree?.audioUrl) {
      setAudioSrc(null);
      return;
    }
    setAudioSrc(resolveAudioUrl(tree.audioUrl, language));
  }, [tree, language]);

  useEffect(() => {
    if (!tree?.videoUrl || tree.videoUrl.startsWith('internal:')) {
      setVideoSrc(null);
      return;
    }
    setVideoSrc(resolveVideoUrl(tree.videoUrl, language));
  }, [tree, language]);

  useEffect(() => {
    if (!tree?.slug) {
      setOtherTrees([]);
      return;
    }
    fetchTrees(language)
      .then((list) => setOtherTrees((list || []).filter((item) => item.slug !== tree.slug)))
      .catch(() => setOtherTrees([]));
  }, [tree?.slug, language]);

  if (loading) return <LoadingSpinner />;
  if (error) {
    return (
      <div className="section-container py-20 text-center">
        <p className="text-gray-600">{t(language, 'error')}</p>
      </div>
    );
  }
  if (!tree) {
    return (
      <div className="section-container py-20 text-center">
        <p className="text-gray-600">{t(language, 'notFound')}</p>
        <Link to="/" className="btn btn-primary mt-4">{t(language, 'backHome')}</Link>
      </div>
    );
  }

  const meta = [
    { label: t(language, 'family'), value: tree.family },
    { label: t(language, 'origin'), value: tree.origin },
    { label: t(language, 'age'), value: tree.ageEstimate },
    { label: t(language, 'height'), value: tree.typicalHeight },
  ].filter((m) => m.value);

  return (
    <article className="bg-surface">
      <div className="relative h-[min(70vh,520px)] overflow-hidden">
        {activeImage ? (
          <TreeHeroImage src={resolveMediaUrl(activeImage.url)} alt={tree.commonName} />
        ) : (
          <div className="flex h-full items-center justify-center bg-primary/10 text-8xl">🌳</div>
        )}
        <div className="absolute inset-0 bg-gradient-to-t from-primary-dark/90 via-primary-dark/30 to-transparent" />
        <div className="absolute bottom-0 left-0 right-0 p-6 text-white sm:p-10">
          <span className="rounded-full bg-white/20 px-3 py-1 text-xs font-semibold backdrop-blur-sm">
            {t(language, 'indigenousTree')}
          </span>
          <h1 className="mt-3 font-display text-4xl font-bold sm:text-5xl">{tree.commonName}</h1>
          <p className="mt-2 text-lg italic opacity-90">{tree.scientificName}</p>
        </div>
      </div>

      <div className="section-container py-10">
        <div className="mx-auto max-w-4xl space-y-8">
          <div className="rounded-2xl border border-primary/20 bg-primary/5 p-4 text-sm text-primary-dark">
            <span className="mr-2" aria-hidden="true">📱</span>
            {t(language, 'scanQr')}
          </div>

          {meta.length > 0 && (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
              {meta.map((item) => (
                <div key={item.label} className="rounded-xl bg-white p-4 shadow-sm">
                  <p className="text-xs font-semibold uppercase tracking-wide text-gray-500">{item.label}</p>
                  <p className="mt-1 font-medium text-primary-dark">{item.value}</p>
                </div>
              ))}
            </div>
          )}

          <InfoSection title={t(language, 'description')} content={tree.description} icon="📋" />
          <InfoSection title={t(language, 'uses')} content={tree.uses} icon="🛠️" />
          <InfoSection title={t(language, 'ecologicalImportance')} content={tree.ecologicalImportance} icon="🌍" />
          <BulletSection title={t(language, 'interestingFacts')} content={tree.interestingFacts} icon="✨" />

          {audioSrc && (
            <section className="card">
              <h2 className="text-xl font-bold text-primary-dark">🎧 {t(language, 'listenDescription')}</h2>
              <p className="mt-2 text-sm text-gray-600">{t(language, 'listenDescriptionSubtitle')}</p>
              <audio
                key={audioSrc}
                controls
                className="mt-4 w-full"
                src={audioSrc}
                onError={() => {
                  if (tree.audioUrl && audioSrc !== tree.audioUrl) {
                    setAudioSrc(tree.audioUrl);
                  }
                }}
              >
                <track kind="captions" />
              </audio>
            </section>
          )}

          {videoSrc && (
            <section className="card overflow-hidden !p-0">
              <div className="p-6 pb-0">
                <h2 className="text-xl font-bold text-primary-dark">🎬 {t(language, 'watchVideo')}</h2>
                <p className="mt-2 text-sm text-gray-600">{t(language, 'watchVideoTreeSubtitle')}</p>
              </div>
              <div className="mt-4 aspect-video bg-black">
                {isYoutubeEmbed(videoSrc) ? (
                  <iframe
                    title={tree.commonName}
                    src={videoSrc}
                    className="h-full w-full"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                    allowFullScreen
                    loading="lazy"
                  />
                ) : (
                  <video
                    key={videoSrc}
                    controls
                    playsInline
                    preload="metadata"
                    className="h-full w-full"
                    src={videoSrc}
                    onError={() => {
                      if (tree.videoUrl && videoSrc !== tree.videoUrl) {
                        setVideoSrc(tree.videoUrl);
                      }
                    }}
                  >
                    <track kind="captions" />
                  </video>
                )}
              </div>
            </section>
          )}

          {tree.images?.length > 0 && (
            <section className="card">
              <h2 className="mb-4 text-xl font-bold text-primary-dark">{t(language, 'gallery')}</h2>
              <div className="grid grid-cols-2 gap-3 sm:grid-cols-4">
                {tree.images.map((img) => (
                  <button
                    key={img.id}
                    type="button"
                    onClick={() => setActiveImage(img)}
                    className={`overflow-hidden rounded-xl border-2 ${
                      activeImage?.id === img.id ? 'border-primary' : 'border-transparent'
                    }`}
                  >
                    <GalleryImage
                      src={resolveMediaUrl(img.url)}
                      alt={img.caption || ''}
                      className="aspect-square w-full object-cover"
                    />
                  </button>
                ))}
              </div>
              {activeImage?.caption && (
                <p className="mt-3 text-sm text-gray-600">{activeImage.caption}</p>
              )}
            </section>
          )}

          <TreeLocationMap lat={tree.latitude} lng={tree.longitude} name={tree.commonName} language={language} />

          <InfoSection title={t(language, 'benefits')} content={tree.benefitsToPeopleAndWildlife} icon="🤝" />
          <InfoSection title={t(language, 'commonAreas')} content={tree.commonAreas} icon="📍" />
          <InfoSection title={t(language, 'additionalInfo')} content={tree.additionalInfo} icon="ℹ️" />

          {otherTrees.length > 0 && (
            <section className="card">
              <h2 className="mb-2 text-xl font-bold text-primary-dark">{t(language, 'otherParkTrees')}</h2>
              <p className="mb-4 text-sm text-gray-500">{t(language, 'singleTreePageHint')}</p>
              <div className="grid gap-3 sm:grid-cols-2">
                {otherTrees.map((item) => (
                  <Link
                    key={item.id}
                    to={`/trees/${item.slug}`}
                    className="flex items-center gap-3 rounded-xl border border-gray-100 bg-surface px-4 py-3 transition hover:border-primary/30 hover:bg-primary/5"
                  >
                    {item.primaryImageUrl ? (
                      <img
                        src={resolveMediaUrl(item.primaryImageUrl)}
                        alt=""
                        className="h-12 w-12 rounded-lg object-cover"
                      />
                    ) : (
                      <span className="flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10 text-xl">🌳</span>
                    )}
                    <div className="min-w-0">
                      <p className="truncate font-semibold text-primary-dark">{item.commonName}</p>
                      <p className="truncate text-xs italic text-gray-500">{item.scientificName}</p>
                    </div>
                  </Link>
                ))}
              </div>
            </section>
          )}
        </div>
      </div>
    </article>
  );
}
