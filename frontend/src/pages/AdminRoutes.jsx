import { Navigate, Route, Routes } from 'react-router-dom';
import AdminLayout from '../components/AdminLayout';
import RequireAdmin from '../components/RequireAdmin';
import AdminLoginPage from './AdminLoginPage';
import AdminDashboardHome from './AdminDashboardHome';
import AdminContactPage from './AdminContactPage';
import AdminTreesPage from './AdminTreesPage';
import AdminTreeFormPage from './AdminTreeFormPage';
import AdminQrPage from './AdminQrPage';
import AdminLanguagesPage from './AdminLanguagesPage';
import AdminUsersPage from './AdminUsersPage';
import AdminStatsPage from './AdminStatsPage';
import QrLabelPage from './QrLabelPage';

export default function AdminRoutes() {
  return (
    <Routes>
      <Route index element={<Navigate to="dashboard" replace />} />
      <Route path="login" element={<AdminLoginPage />} />

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
        <Route path="trees/new" element={<AdminTreeFormPage />} />
        <Route path="trees/:id/edit" element={<AdminTreeFormPage />} />
        <Route path="qr" element={<AdminQrPage />} />
        <Route path="qr-label/:slug" element={<QrLabelPage />} />
        <Route path="languages" element={<AdminLanguagesPage />} />
        <Route path="users" element={<AdminUsersPage />} />
        <Route path="stats" element={<AdminStatsPage />} />
        <Route path="settings" element={<Navigate to="/admin/languages" replace />} />
      </Route>

      <Route path="*" element={<Navigate to="/admin/login" replace />} />
    </Routes>
  );
}
