import { useEffect, useState } from 'react';
import TreeMap from '../components/TreeMap';
import LoadingSpinner from '../components/LoadingSpinner';
import { fetchMapMarkers } from '../api/client';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function MapPage() {
  const { language } = useLanguage();
  const [markers, setMarkers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetchMapMarkers(language)
      .then(setMarkers)
      .catch(() => setMarkers([]))
      .finally(() => setLoading(false));
  }, [language]);

  return (
    <div className="bg-surface py-12">
      <div className="section-container">
        <h1 className="section-title">{t(language, 'mapTitle')}</h1>
        <p className="section-subtitle">{t(language, 'mapIntro')}</p>

        <div className="mt-8 overflow-hidden rounded-2xl border border-gray-200 bg-white p-2">
          {loading ? <LoadingSpinner /> : <TreeMap markers={markers} />}
        </div>

        {!loading && markers.length === 0 && (
          <p className="mt-4 text-center text-gray-600">{t(language, 'mapEmpty')}</p>
        )}
      </div>
    </div>
  );
}
