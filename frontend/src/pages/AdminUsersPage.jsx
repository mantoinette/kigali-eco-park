import { useCallback, useEffect, useState } from 'react';
import {
  createAdminUser,
  deleteAdminUser,
  fetchAdminUsers,
  updateAdminUser,
} from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function AdminUsersPage() {
  const { language } = useLanguage();
  const { user } = useAuth();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ fullName: '', email: '', password: '', role: 'VISITOR' });
  const [saving, setSaving] = useState(false);

  const load = useCallback(() => {
    setLoading(true);
    fetchAdminUsers(user.token)
      .then(setItems)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [user.token]);

  useEffect(() => {
    load();
  }, [load]);

  const handleCreate = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      await createAdminUser(user.token, form);
      setForm({ fullName: '', email: '', password: '', role: 'VISITOR' });
      load();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  const changeRole = async (item) => {
    const nextRole = item.role === 'ADMIN' ? 'VISITOR' : 'ADMIN';
    try {
      await updateAdminUser(user.token, item.id, { role: nextRole });
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async (item) => {
    if (!window.confirm(t(language, 'confirmDeleteUser'))) return;
    try {
      await deleteAdminUser(user.token, item.id);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <h1 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'adminUsers')}</h1>
      <p className="mt-2 text-sm text-gray-600">{t(language, 'adminUsersDesc')}</p>

      {error && (
        <p className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
      )}

      <form onSubmit={handleCreate} className="mt-8 grid gap-3 rounded-2xl border border-gray-200 bg-white p-5 md:grid-cols-5">
        <input
          required
          placeholder={t(language, 'fullName')}
          value={form.fullName}
          onChange={(e) => setForm((f) => ({ ...f, fullName: e.target.value }))}
          className="rounded-xl border border-gray-300 px-3 py-2 text-sm"
        />
        <input
          required
          type="email"
          placeholder={t(language, 'email')}
          value={form.email}
          onChange={(e) => setForm((f) => ({ ...f, email: e.target.value }))}
          className="rounded-xl border border-gray-300 px-3 py-2 text-sm"
        />
        <input
          required
          type="password"
          minLength={6}
          placeholder={t(language, 'password')}
          value={form.password}
          onChange={(e) => setForm((f) => ({ ...f, password: e.target.value }))}
          className="rounded-xl border border-gray-300 px-3 py-2 text-sm"
        />
        <select
          value={form.role}
          onChange={(e) => setForm((f) => ({ ...f, role: e.target.value }))}
          className="rounded-xl border border-gray-300 px-3 py-2 text-sm"
        >
          <option value="VISITOR">Visitor</option>
          <option value="ADMIN">Admin</option>
        </select>
        <button type="submit" className="btn btn-primary !rounded-xl" disabled={saving}>
          {t(language, 'addUser')}
        </button>
      </form>

      <div className="mt-6 overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
        <table className="min-w-full text-left text-sm">
          <thead className="bg-primary/5 text-xs uppercase tracking-wide text-primary-dark">
            <tr>
              <th className="px-4 py-3">{t(language, 'fullName')}</th>
              <th className="px-4 py-3">{t(language, 'email')}</th>
              <th className="px-4 py-3">{t(language, 'role')}</th>
              <th className="px-4 py-3">{t(language, 'actions')}</th>
            </tr>
          </thead>
          <tbody>
            {items.map((item) => (
              <tr key={item.id} className="border-t border-gray-100">
                <td className="px-4 py-3">{item.fullName}</td>
                <td className="px-4 py-3">{item.email}</td>
                <td className="px-4 py-3">
                  <button
                    type="button"
                    onClick={() => changeRole(item)}
                    className={`rounded-full px-2.5 py-0.5 text-xs font-semibold ${
                      item.role === 'ADMIN' ? 'bg-primary/10 text-primary-dark' : 'bg-gray-100 text-gray-700'
                    }`}
                  >
                    {item.role}
                  </button>
                </td>
                <td className="px-4 py-3">
                  {item.email !== 'admin@treescan.rw' && (
                    <button
                      type="button"
                      onClick={() => handleDelete(item)}
                      className="text-xs font-semibold text-red-600 hover:underline"
                    >
                      {t(language, 'delete')}
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
