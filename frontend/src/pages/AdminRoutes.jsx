import { Navigate, Route, Routes } from 'react-router-dom';
import AdminLayout from '../components/AdminLayout';
import RequireAdmin from '../components/RequireAdmin';
import AdminLoginPage from './AdminLoginPage';
import AdminDashboardHome from './AdminDashboardHome';
import AdminContactPage from './AdminContactPage';
import AdminTreesPage from './AdminTreesPage';
import AdminQrPage from './AdminQrPage';
import AdminSettingsPage from './AdminSettingsPage';
import QrLabelPage from './QrLabelPage';

export default function AdminRoutes() {
  return (
    <Routes>
      <Route index element={<Navigate to="dashboard" replace />} />
      <Route path="login" element={<AdminLoginPage />} />
      <Route
        path="qr-label/:slug"
        element={(
          <RequireAdmin>
            <QrLabelPage />
          </RequireAdmin>
        )}
      />

      <Route
        element={(
          <RequireAdmin>
            <AdminLayout />
          </RequireAdmin>
        )}
      >
        <Route index element={<Navigate to="dashboard" replace />} />
        <Route path="dashboard" element={<AdminDashboardHome />} />
        <Route path="requests" element={<AdminContactPage />} />
        <Route path="messages" element={<Navigate to="/admin/requests" replace />} />
        <Route path="trees" element={<AdminTreesPage />} />
        <Route path="trees/new" element={<Navigate to="/admin/trees" replace />} />
        <Route path="qr" element={<AdminQrPage />} />
        <Route path="settings" element={<AdminSettingsPage />} />
        <Route path="languages" element={<Navigate to="/admin/settings" replace />} />
        <Route path="users" element={<Navigate to="/admin/settings" replace />} />
        <Route path="stats" element={<Navigate to="/admin/dashboard" replace />} />
      </Route>

      <Route path="*" element={<Navigate to="/admin/login" replace />} />
    </Routes>
  );
}
