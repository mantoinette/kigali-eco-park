import { useCallback, useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import {
  deleteContactRequest,
  fetchContactRequests,
  fetchContactStats,
  updateContactRequest,
} from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const STATUSES = ['NEW', 'IN_PROGRESS', 'RESOLVED'];
const REQUEST_TYPES = [
  '',
  'QR_CODE_REQUEST',
  'TREE_INFORMATION',
  'GENERAL_INQUIRY',
  'PARTNERSHIP',
  'OTHER',
];

function formatDate(iso, language) {
  if (!iso) return '—';
  return new Date(iso).toLocaleString(language === 'rw' ? 'rw-RW' : language === 'fr' ? 'fr-FR' : 'en-GB', {
    dateStyle: 'medium',
    timeStyle: 'short',
  });
}

function statusBadgeClass(status) {
  if (status === 'NEW') return 'bg-blue-100 text-blue-800';
  if (status === 'IN_PROGRESS') return 'bg-amber-100 text-amber-900';
  return 'bg-green-100 text-green-800';
}

function AdminContactContent() {
  const { language } = useLanguage();
  const { user } = useAuth();
  const [searchParams] = useSearchParams();
  const [messages, setMessages] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [typeFilter, setTypeFilter] = useState('');
  const [selectedId, setSelectedId] = useState(null);
  const [adminNotes, setAdminNotes] = useState('');
  const [linkedTreeSlug, setLinkedTreeSlug] = useState('');
  const [saving, setSaving] = useState(false);

  const selected = messages.find((m) => m.id === selectedId) || null;

  const load = useCallback(() => {
    setLoading(true);
    setError('');
    Promise.all([
      fetchContactRequests(user.token, { status: statusFilter || undefined, requestType: typeFilter || undefined }),
      fetchContactStats(user.token),
    ])
      .then(([list, summary]) => {
        setMessages(list);
        setStats(summary);
      })
      .catch((err) => setError(err.message || 'Failed to load messages'))
      .finally(() => setLoading(false));
  }, [user.token, statusFilter, typeFilter]);

  useEffect(() => {
    const status = searchParams.get('status');
    const type = searchParams.get('type');
    const id = searchParams.get('id');
    if (status) setStatusFilter(status);
    if (type) setTypeFilter(type);
    if (id) setSelectedId(Number(id));
  }, [searchParams]);

  useEffect(() => {
    load();
  }, [load]);

  useEffect(() => {
    if (selected) {
      setAdminNotes(selected.adminNotes || '');
      setLinkedTreeSlug(selected.linkedTreeSlug || '');
    }
  }, [selected]);

  const handleStatusChange = async (id, status) => {
    setSaving(true);
    try {
      const updated = await updateContactRequest(user.token, id, { status });
      setMessages((prev) => prev.map((m) => (m.id === id ? updated : m)));
      load();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  const handleSaveNotes = async () => {
    if (!selected) return;
    setSaving(true);
    try {
      const updated = await updateContactRequest(user.token, selected.id, {
        adminNotes,
        linkedTreeSlug: linkedTreeSlug.trim() || null,
      });
      setMessages((prev) => prev.map((m) => (m.id === selected.id ? updated : m)));
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm(t(language, 'confirmDeleteMessage'))) return;
    setSaving(true);
    try {
      await deleteContactRequest(user.token, id);
      if (selectedId === id) setSelectedId(null);
      load();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      <div className="mb-8">
        <h1 className="font-display text-3xl font-bold text-primary-dark">
          {t(language, 'adminMessages')}
        </h1>
        <p className="mt-2 max-w-2xl text-sm text-gray-600">{t(language, 'adminMessagesDesc')}</p>
      </div>

        {stats && (
          <div className="mb-6 grid gap-3 sm:grid-cols-2 lg:grid-cols-5">
            {[
              { label: t(language, 'adminMessagesTotal'), value: stats.total },
              { label: t(language, 'statusNew'), value: stats.newCount },
              { label: t(language, 'statusInProgress'), value: stats.inProgressCount },
              { label: t(language, 'statusResolved'), value: stats.resolvedCount },
              { label: t(language, 'adminQrRequests'), value: stats.qrRequestCount },
            ].map((item) => (
              <div key={item.label} className="rounded-xl border border-gray-200 bg-white px-4 py-3 shadow-sm">
                <p className="text-xs font-semibold uppercase tracking-wide text-gray-500">{item.label}</p>
                <p className="mt-1 text-2xl font-bold text-primary-dark">{item.value}</p>
              </div>
            ))}
          </div>
        )}

        <div className="mb-4 flex flex-wrap gap-3">
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="rounded-xl border border-gray-300 bg-white px-3 py-2 text-sm"
          >
            <option value="">{t(language, 'filterAllStatuses')}</option>
            {STATUSES.map((s) => (
              <option key={s} value={s}>{t(language, `status_${s}`)}</option>
            ))}
          </select>
          <select
            value={typeFilter}
            onChange={(e) => setTypeFilter(e.target.value)}
            className="rounded-xl border border-gray-300 bg-white px-3 py-2 text-sm"
          >
            {REQUEST_TYPES.map((type) => (
              <option key={type || 'all'} value={type}>
                {type ? t(language, `requestType_${type}`) : t(language, 'filterAllTypes')}
              </option>
            ))}
          </select>
        </div>

        {loading && <LoadingSpinner />}
        {error && (
          <p className="mb-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
        )}

        {!loading && !error && (
          <div className="grid gap-6 lg:grid-cols-[1fr_1.1fr]">
            <div className="space-y-3">
              {messages.length === 0 ? (
                <p className="rounded-xl border border-dashed border-gray-300 bg-white px-6 py-12 text-center text-sm text-gray-500">
                  {t(language, 'noMessagesYet')}
                </p>
              ) : (
                messages.map((msg) => (
                  <button
                    key={msg.id}
                    type="button"
                    onClick={() => setSelectedId(msg.id)}
                    className={`w-full rounded-xl border bg-white p-4 text-left shadow-sm transition hover:shadow-md ${
                      selectedId === msg.id ? 'border-primary ring-2 ring-primary/20' : 'border-gray-200'
                    }`}
                  >
                    <div className="flex flex-wrap items-start justify-between gap-2">
                      <div>
                        <p className="font-semibold text-primary-dark">{msg.fullName}</p>
                        <p className="text-xs text-gray-500">{msg.email}</p>
                      </div>
                      <span className={`rounded-full px-2.5 py-0.5 text-xs font-semibold ${statusBadgeClass(msg.status)}`}>
                        {t(language, `status_${msg.status}`)}
                      </span>
                    </div>
                    <p className="mt-2 text-sm font-medium text-gray-800">
                      {t(language, `requestType_${msg.requestType}`)}
                      {msg.treeName ? ` · ${msg.treeName}` : ''}
                    </p>
                    <p className="mt-1 line-clamp-2 text-xs text-gray-600">{msg.message}</p>
                    <p className="mt-2 text-xs text-gray-400">{formatDate(msg.createdAt, language)}</p>
                  </button>
                ))
              )}
            </div>

            <div className="rounded-2xl border border-gray-200 bg-white p-5 shadow-sm lg:sticky lg:top-24 lg:self-start">
              {!selected ? (
                <p className="py-16 text-center text-sm text-gray-500">{t(language, 'selectMessageHint')}</p>
              ) : (
                <div className="space-y-4">
                  <div className="flex flex-wrap items-start justify-between gap-3">
                    <div>
                      <h2 className="font-display text-xl font-bold text-primary-dark">{selected.fullName}</h2>
                      <a href={`mailto:${selected.email}`} className="text-sm text-primary hover:underline">{selected.email}</a>
                      {selected.phone && (
                        <p className="text-sm text-gray-600">
                          <a href={`tel:${selected.phone}`} className="hover:underline">{selected.phone}</a>
                        </p>
                      )}
                    </div>
                    {selected.requestType === 'QR_CODE_REQUEST' && (
                      <span className="rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary-dark">
                        {t(language, 'qrRequestBadge')}
                      </span>
                    )}
                  </div>

                  <dl className="grid gap-2 text-sm">
                    <div>
                      <dt className="text-xs font-semibold uppercase text-gray-500">{t(language, 'requestType')}</dt>
                      <dd className="text-gray-800">{t(language, `requestType_${selected.requestType}`)}</dd>
                    </div>
                    {selected.subject && (
                      <div>
                        <dt className="text-xs font-semibold uppercase text-gray-500">{t(language, 'subject')}</dt>
                        <dd className="text-gray-800">{selected.subject}</dd>
                      </div>
                    )}
                    {selected.treeName && (
                      <div>
                        <dt className="text-xs font-semibold uppercase text-gray-500">{t(language, 'treeNameLabel')}</dt>
                        <dd className="text-gray-800">{selected.treeName}</dd>
                      </div>
                    )}
                    <div>
                      <dt className="text-xs font-semibold uppercase text-gray-500">{t(language, 'submittedAt')}</dt>
                      <dd className="text-gray-800">{formatDate(selected.createdAt, language)}</dd>
                    </div>
                  </dl>

                  <div>
                    <p className="text-xs font-semibold uppercase text-gray-500">{t(language, 'yourMessage')}</p>
                    <p className="mt-1 whitespace-pre-line rounded-xl bg-gray-50 px-4 py-3 text-sm text-gray-800">{selected.message}</p>
                  </div>

                  <label className="block">
                    <span className="text-xs font-semibold uppercase text-gray-500">{t(language, 'messageStatus')}</span>
                    <select
                      value={selected.status}
                      disabled={saving}
                      onChange={(e) => handleStatusChange(selected.id, e.target.value)}
                      className="mt-1 w-full rounded-xl border border-gray-300 px-3 py-2 text-sm"
                    >
                      {STATUSES.map((s) => (
                        <option key={s} value={s}>{t(language, `status_${s}`)}</option>
                      ))}
                    </select>
                  </label>

                  {selected.requestType === 'QR_CODE_REQUEST' && (
                    <>
                      <label className="block">
                        <span className="text-xs font-semibold uppercase text-gray-500">{t(language, 'linkedTreeSlug')}</span>
                        <input
                          type="text"
                          value={linkedTreeSlug}
                          onChange={(e) => setLinkedTreeSlug(e.target.value)}
                          placeholder="e.g. syzygium-guineense"
                          className="mt-1 w-full rounded-xl border border-gray-300 px-3 py-2 text-sm"
                        />
                      </label>
                      <div className="flex flex-wrap gap-2">
                        <Link
                          to={linkedTreeSlug ? `/admin/qr?slug=${encodeURIComponent(linkedTreeSlug)}` : '/admin/qr'}
                          className="btn btn-primary !rounded-xl !px-4 !py-2 text-xs"
                        >
                          {t(language, 'generateQrForRequest')}
                        </Link>
                        <button
                          type="button"
                          className="btn btn-secondary !rounded-xl !px-4 !py-2 text-xs"
                          disabled={saving}
                          onClick={() => handleStatusChange(selected.id, 'RESOLVED')}
                        >
                          {t(language, 'markResolved')}
                        </button>
                      </div>
                    </>
                  )}

                  <label className="block">
                    <span className="text-xs font-semibold uppercase text-gray-500">{t(language, 'adminNotes')}</span>
                    <textarea
                      rows={3}
                      value={adminNotes}
                      onChange={(e) => setAdminNotes(e.target.value)}
                      className="mt-1 w-full rounded-xl border border-gray-300 px-3 py-2 text-sm"
                    />
                  </label>

                  <div className="flex flex-wrap gap-2 pt-2">
                    <button
                      type="button"
                      disabled={saving}
                      onClick={handleSaveNotes}
                      className="btn btn-secondary !rounded-xl !px-4 !py-2 text-xs"
                    >
                      {saving ? t(language, 'pleaseWait') : t(language, 'saveNotes')}
                    </button>
                    <button
                      type="button"
                      disabled={saving}
                      onClick={() => handleDelete(selected.id)}
                      className="rounded-xl border border-red-200 px-4 py-2 text-xs font-semibold text-red-700 hover:bg-red-50"
                    >
                      {t(language, 'deleteMessage')}
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        )}
    </div>
  );
}

export default AdminContactContent;
