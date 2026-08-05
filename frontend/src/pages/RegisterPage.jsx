import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthLayout from '../components/AuthLayout';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function RegisterPage() {
  const { language } = useLanguage();
  const { register } = useAuth();
  const navigate = useNavigate();
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (password !== confirmPassword) {
      setError(t(language, 'passwordMismatch'));
      return;
    }

    setSubmitting(true);
    try {
      await register(fullName.trim(), email.trim(), password);
      navigate('/');
    } catch (err) {
      setError(err.message || t(language, 'authError'));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AuthLayout
      title={t(language, 'registerTitle')}
      subtitle={t(language, 'registerSubtitle')}
      footer={(
        <>
          {t(language, 'haveAccount')}{' '}
          <Link to="/login" className="font-semibold text-primary hover:underline">
            {t(language, 'login')}
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

        <label className="block">
          <span className="text-sm font-medium text-gray-700">{t(language, 'fullName')}</span>
          <input
            type="text"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
            autoComplete="name"
            minLength={2}
            className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm transition focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </label>

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
            autoComplete="new-password"
            minLength={6}
            className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm transition focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </label>

        <label className="block">
          <span className="text-sm font-medium text-gray-700">{t(language, 'confirmPassword')}</span>
          <input
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
            autoComplete="new-password"
            minLength={6}
            className="mt-1.5 w-full rounded-xl border border-gray-300 px-4 py-3 text-sm transition focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
          />
        </label>

        <button
          type="submit"
          className="btn btn-primary w-full !rounded-xl py-3.5"
          disabled={submitting}
        >
          {submitting ? t(language, 'pleaseWait') : t(language, 'createAccount')}
        </button>
      </form>
    </AuthLayout>
  );
}
