import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Link } from 'react-router-dom';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: markerIcon2x,
  iconUrl: markerIcon,
  shadowUrl: markerShadow,
});

const PARK_CENTER = [-1.9686, 30.1045];

export default function TreeMap({ markers }) {
  const { language } = useLanguage();

  return (
    <MapContainer
      center={PARK_CENTER}
      zoom={15}
      scrollWheelZoom
      className="h-[min(70vh,560px)] w-full shadow-card"
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {markers.map((tree) => (
        <Marker key={tree.id} position={[tree.latitude, tree.longitude]}>
          <Popup>
            <div className="min-w-[180px]">
              <strong>{tree.commonName}</strong>
              <p className="text-xs italic text-gray-600">{tree.scientificName}</p>
              <Link to={`/trees/${tree.qrCodeId || tree.slug}`} className="mt-2 inline-block text-sm text-primary">
                {t(language, 'howToUnlock')}
              </Link>
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}
