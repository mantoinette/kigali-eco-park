import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import {
  createAdminTree,
  fetchAdminTree,
  updateAdminTree,
} from '../api/client';
import LoadingSpinner from '../components/LoadingSpinner';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

function slugify(value) {
  return value
    .trim()
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '');
}

const emptyForm = {
  scientificName: '',
  slug: '',
  qrCodeId: '',
  family: '',
  nativeStatus: 'NATIVE',
  typicalHeight: '',
  origin: '',
  latitude: '',
  longitude: '',
  commonName: '',
  shortDescription: '',
  description: '',
  published: true,
};

export default function AdminTreeFormPage() {
  const { id } = useParams();
  const isEdit = Boolean(id);
  const { language } = useLanguage();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(isEdit);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!isEdit) return;
    fetchAdminTree(user.token, id, language)
      .then((tree) => {
        setForm({
          scientificName: tree.scientificName || '',
          slug: tree.slug || '',
          qrCodeId: tree.qrCodeId || '',
          family: tree.family || '',
          nativeStatus: tree.nativeStatus || 'UNKNOWN',
          typicalHeight: tree.typicalHeight || '',
          origin: tree.origin || '',
          latitude: tree.latitude ?? '',
          longitude: tree.longitude ?? '',
          commonName: tree.commonName || '',
          shortDescription: tree.shortDescription || '',
          description: tree.description || '',
          published: tree.published ?? true,
        });
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [isEdit, id, user.token, language]);

  const updateField = (key, value) => {
    setForm((prev) => ({ ...prev, [key]: value }));
  };

  const handleScientificBlur = () => {
    if (!form.slug && form.scientificName) {
      updateField('slug', slugify(form.scientificName));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);

    const translation = {
      languageCode: language,
      commonName: form.commonName.trim(),
      shortDescription: form.shortDescription.trim() || null,
      description: form.description.trim() || null,
    };

    const payload = {
      scientificName: form.scientificName.trim(),
      slug: form.slug.trim(),
      qrCodeId: form.qrCodeId.trim() || null,
      family: form.family.trim() || null,
      nativeStatus: form.nativeStatus,
      typicalHeight: form.typicalHeight.trim() || null,
      origin: form.origin.trim() || null,
      latitude: form.latitude === '' ? null : Number(form.latitude),
      longitude: form.longitude === '' ? null : Number(form.longitude),
      published: form.published,
      translation,
    };

    try {
      if (isEdit) {
        await updateAdminTree(user.token, id, payload);
      } else {
        await createAdminTree(user.token, payload);
      }
      navigate('/admin/trees');
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div>
      <div className="mb-8 flex flex-wrap items-start justify-between gap-4">
        <div>
          <h1 className="font-display text-3xl font-bold text-primary-dark">
            {isEdit ? t(language, 'adminEditTree') : t(language, 'adminAddTree')}
          </h1>
          <p className="mt-2 text-sm text-gray-600">
            {isEdit ? t(language, 'adminEditTreeDesc') : t(language, 'adminAddTreeDesc')}
          </p>
        </div>
        <Link to="/admin/trees" className="btn btn-secondary !rounded-xl !px-4 !py-2 text-sm">
          {t(language, 'adminBackToList')}
        </Link>
      </div>

      {error && (
        <p className="mb-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">{error}</p>
      )}

      <form onSubmit={handleSubmit} className="space-y-6 rounded-2xl border border-gray-200 bg-white p-6 shadow-sm">
        <div className="grid gap-4 md:grid-cols-2">
          <label className="block">
            <span className="text-sm font-medium text-gray-700">{t(language, 'directoryColumnScientific')}</span>
            <input
              required
              value={form.scientificName}
              onChange={(e) => updateField('scientificName', e.target.value)}
              onBlur={handleScientificBlur}
              className="mt-1 w-full rounded-xl border border-gray-300 px-4 py-2.5 text-sm"
            />
          </label>
          <label className="block">
            <span className="text-sm font-medium text-gray-700">{t(language, 'slug')}</span>
            <input
              required
              value={form.slug}
              onChange={(e) => updateField('slug', e.target.value)}
              className="mt-1 w-full rounded-xl border border-gray-300 px-4 py-2.5 text-sm"
            />
          </label>
          <label className="block">
            <span className="text-sm font-medium text-gray-700">{t(language, 'directoryColumnId')}</span>
            <input
              value={form.qrCodeId}
              onChange={(e) => updateField('qrCodeId', e.target.value)}
              placeholder="TREE-022"
              className="mt-1 w-full rounded-xl border border-gray-300 px-4 py-2.5 text-sm"
            />
          </label>
          <label className="block">
            <span className="text-sm font-medium text-gray-700">{t(language, 'family')}</span>
            <input
              value={form.family}
              onChange={(e) => updateField('family', e.target.value)}
              className="mt-1 w-full rounded-xl border border-gray-300 px-4 py-2.5 text-sm"
            />
          </label>
          <label className="block">
            <span className="text-sm font-medium text-gray-700">{t(language, 'directoryColumnName')}</span>
            <input
              required
              value={form.commonName}
              onChange={(e) => updateField('commonName', e.target.value)}
              className="mt-1 w-full rounded-xl border border-gray-300 px-4 py-2.5 text-sm"
            />
          </label>
          <label className="block">
            <span className="text-sm font-medium text-gray-700">{t(language, 'nativeStatus')}</span>
            <select
              value={form.nativeStatus}
              onChange={(e) => updateField('nativeStatus', e.target.value)}
              className="mt-1 w-full rounded-xl border border-gray-300 px-4 py-2.5 text-sm"
            >
              <option value="NATIVE">Native</option>
              <option value="INTRODUCED">Introduced</option>
              <option value="UNKNOWN">Unknown</option>
            </select>
          </label>
        </div>

        <label className="block">
          <span className="text-sm font-medium text-gray-700">{t(language, 'shortDescription')}</span>
          <textarea
            rows={2}
            value={form.shortDescription}
            onChange={(e) => updateField('shortDescription', e.target.value)}
            className="mt-1 w-full rounded-xl border border-gray-300 px-4 py-2.5 text-sm"
          />
        </label>

        <label className="block">
          <span className="text-sm font-medium text-gray-700">{t(language, 'description')}</span>
          <textarea
            rows={5}
            value={form.description}
            onChange={(e) => updateField('description', e.target.value)}
            className="mt-1 w-full rounded-xl border border-gray-300 px-4 py-2.5 text-sm"
          />
        </label>

        <label className="inline-flex items-center gap-2 text-sm text-gray-700">
          <input
            type="checkbox"
            checked={form.published}
            onChange={(e) => updateField('published', e.target.checked)}
          />
          {t(language, 'publishedOnSite')}
        </label>

        <div className="flex flex-wrap gap-3">
          <button type="submit" className="btn btn-primary !rounded-xl" disabled={submitting}>
            {submitting ? t(language, 'pleaseWait') : t(language, 'saveChanges')}
          </button>
          <Link to="/admin/trees" className="btn btn-secondary !rounded-xl">
            {t(language, 'cancel')}
          </Link>
        </div>
      </form>
    </div>
  );
}
