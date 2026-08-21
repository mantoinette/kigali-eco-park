import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthLayout from '../components/AuthLayout';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function LoginPage() {
  const { language } = useLanguage();
  const { login } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      const response = await login(email.trim(), password);
      navigate(String(response?.role || '').toUpperCase() === 'ADMIN' ? '/admin' : '/');
    } catch (err) {
      setError(err.message || t(language, 'authError'));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AuthLayout
      title={t(language, 'loginTitle')}
      subtitle={t(language, 'loginSubtitle')}
      footer={(
        <>
          {t(language, 'noAccount')}{' '}
          <Link to="/register" className="font-semibold text-primary hover:underline">
            {t(language, 'register')}
          </Link>
        </>
      )}
    >
      <form className="space-y-5" onSubmit={handleSubmit}>
        {error && (
          <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700" role="alert">
            {error}
          </div>
        )}

        <div className="rounded-xl border border-primary/20 bg-primary/5 px-4 py-3 text-sm text-primary-dark">
          <p className="font-semibold">{t(language, 'adminLoginHintTitle')}</p>
          <p className="mt-1 text-xs leading-relaxed text-gray-700">{t(language, 'adminLoginHint')}</p>
        </div>

        <label className="block">
          <span className="text-sm font-medium text-gray-700">{t(language, 'email')}</span>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            autoComplete="email"
            placeholder="you@example.com"
            className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm transition focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </label>

        <label className="block">
          <span className="text-sm font-medium text-gray-700">{t(language, 'password')}</span>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            autoComplete="current-password"
            minLength={6}
            className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm transition focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </label>

        <button
          type="submit"
          className="btn btn-primary w-full !rounded-xl py-3.5"
          disabled={submitting}
        >
          {submitting ? t(language, 'pleaseWait') : t(language, 'login')}
        </button>
      </form>
    </AuthLayout>
  );
}
