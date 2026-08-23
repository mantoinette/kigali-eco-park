import { useCallback, useEffect, useState } from 'react';
import {
  createAdminLanguage,
  deleteAdminLanguage,
  fetchAdminLanguages,
  updateAdminLanguage,
} from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function AdminLanguagesPage() {
  const { language } = useLanguage();
  const { user } = useAuth();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [code, setCode] = useState('');
  const [name, setName] = useState('');
  const [active, setActive] = useState(true);
  const [saving, setSaving] = useState(false);

  const load = useCallback(() => {
    setLoading(true);
    fetchAdminLanguages(user.token)
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
      await createAdminLanguage(user.token, { code: code.trim(), name: name.trim(), active });
      setCode('');
      setName('');
      setActive(true);
      load();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  const toggleActive = async (item) => {
    try {
      await updateAdminLanguage(user.token, item.code, { active: !item.active });
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async (item) => {
    if (!window.confirm(t(language, 'confirmDeleteLanguage'))) return;
    try {
      await deleteAdminLanguage(user.token, item.code);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <h1 className="font-display text-3xl font-bold text-primary-dark">{t(language, 'adminLanguages')}</h1>
      <p className="mt-2 text-sm text-gray-600">{t(language, 'adminLanguagesDesc')}</p>

      {error && (
        <p className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
      )}

      <form onSubmit={handleCreate} className="mt-8 grid gap-3 rounded-2xl border border-gray-200 bg-white p-5 md:grid-cols-4">
        <input
          required
          placeholder={t(language, 'languageCode')}
          value={code}
          onChange={(e) => setCode(e.target.value)}
          className="rounded-xl border border-gray-300 px-3 py-2 text-sm"
        />
        <input
          required
          placeholder={t(language, 'languageName')}
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="rounded-xl border border-gray-300 px-3 py-2 text-sm md:col-span-2"
        />
        <button type="submit" className="btn btn-primary !rounded-xl" disabled={saving}>
          {t(language, 'addLanguage')}
        </button>
      </form>

      <div className="mt-6 overflow-hidden rounded-2xl border border-gray-200 bg-white shadow-sm">
        <table className="min-w-full text-left text-sm">
          <thead className="bg-primary/5 text-xs uppercase tracking-wide text-primary-dark">
            <tr>
              <th className="px-4 py-3">{t(language, 'languageCode')}</th>
              <th className="px-4 py-3">{t(language, 'languageName')}</th>
              <th className="px-4 py-3">{t(language, 'status')}</th>
              <th className="px-4 py-3">{t(language, 'actions')}</th>
            </tr>
          </thead>
          <tbody>
            {items.map((item) => (
              <tr key={item.code} className="border-t border-gray-100">
                <td className="px-4 py-3 font-mono text-xs">{item.code}</td>
                <td className="px-4 py-3">{item.name}</td>
                <td className="px-4 py-3">
                  <button
                    type="button"
                    onClick={() => toggleActive(item)}
                    className={`rounded-full px-2.5 py-0.5 text-xs font-semibold ${
                      item.active ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-600'
                    }`}
                  >
                    {item.active ? t(language, 'active') : t(language, 'inactive')}
                  </button>
                </td>
                <td className="px-4 py-3">
                  {!['en', 'rw', 'fr'].includes(item.code) && (
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
