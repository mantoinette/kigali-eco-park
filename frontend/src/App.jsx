import { useEffect, useState } from 'react';
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';
import Layout from './components/Layout';
import HomePage from './pages/HomePage';
import TreesPage from './pages/TreesPage';
import TreePreviewPage from './pages/TreePreviewPage';
import ScanTreePage from './pages/ScanTreePage';
import TreeByIdPage from './pages/TreeByIdPage';
import MapPage from './pages/MapPage';
import ContactPage from './pages/ContactPage';
import PrivacyPage from './pages/PrivacyPage';
import FaqPage from './pages/FaqPage';
import AboutPage from './pages/AboutPage';
import AdminRoutes from './pages/AdminRoutes';
import { fetchTrees } from './api/client';
import { useLanguage } from './context/LanguageContext';

function SearchRedirect() {
  const location = useLocation();
  return <Navigate to={{ pathname: '/trees', search: location.search }} replace />;
}

function PublicRoutes({ trees }) {
  return (
    <Routes>
      <Route path="/" element={<HomePage trees={trees} />} />
      <Route path="/trees" element={<TreesPage />} />
      <Route path="/trees/:slug" element={<TreePreviewPage />} />
      <Route path="/map" element={<MapPage />} />
      <Route path="/search" element={<SearchRedirect />} />
      <Route path="/contact" element={<ContactPage />} />
      <Route path="/privacy" element={<PrivacyPage />} />
      <Route path="/scan/:qrCodeId" element={<ScanTreePage mode="legacy" />} />
      <Route path="/t/:token" element={<ScanTreePage mode="token" />} />
      <Route path="/tree/:id" element={<TreeByIdPage />} />
      <Route path="/plantlist" element={<Navigate to="/trees" replace />} />
      <Route path="/qr-label/:slug" element={<Navigate to="/admin/qr" replace />} />
      <Route path="/about" element={<AboutPage />} />
      <Route path="/faq" element={<FaqPage />} />
      <Route path="/login" element={<Navigate to="/admin/login" replace />} />
      <Route path="/register" element={<Navigate to="/contact" replace />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default function App() {
  const { language } = useLanguage();
  const [trees, setTrees] = useState([]);

  useEffect(() => {
    fetchTrees(language)
      .then(setTrees)
      .catch(() => setTrees([]));
  }, [language]);

  return (
    <Routes>
      <Route path="/admin/*" element={<AdminRoutes />} />
      <Route
        path="*"
        element={(
          <Layout>
            <PublicRoutes trees={trees} />
          </Layout>
        )}
      />
    </Routes>
  );
}
