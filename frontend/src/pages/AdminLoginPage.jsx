import { useEffect, useState } from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import AuthLayout from '../components/AuthLayout';
import { useAuth } from '../context/AuthContext';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const ADMIN_EMAIL = 'admin@treescan.rw';

export default function AdminLoginPage() {
  const { language } = useLanguage();
  const { loginAdmin, isAdmin, loading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const redirectTo = location.state?.from || '/admin/dashboard';

  useEffect(() => {
    if (!loading && isAdmin) {
      navigate('/admin/dashboard', { replace: true });
    }
  }, [loading, isAdmin, navigate]);

  if (!loading && isAdmin) {
    return <Navigate to="/admin/dashboard" replace />;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await loginAdmin(email.trim(), password);
      navigate(redirectTo.startsWith('/admin') ? redirectTo : '/admin/dashboard', { replace: true });
    } catch (err) {
      setError(err.message || t(language, 'adminLoginFailed'));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <AuthLayout
      title={t(language, 'adminLoginTitle')}
      subtitle={t(language, 'adminLoginSubtitle')}
      footer={(
        <Link to="/" className="font-semibold text-primary hover:underline">
          {t(language, 'backToWebsite')}
        </Link>
      )}
    >
      <form className="space-y-5" onSubmit={handleSubmit}>
        {error && (
          <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700" role="alert">
            {error}
          </div>
        )}

        <p className="rounded-xl border border-primary/20 bg-primary/5 px-4 py-3 text-xs leading-relaxed text-gray-700">
          {t(language, 'adminLoginNotice')}
        </p>

        <label className="block">
          <span className="text-sm font-medium text-gray-700">{t(language, 'email')}</span>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            autoComplete="username"
            placeholder={ADMIN_EMAIL}
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
          {submitting ? t(language, 'pleaseWait') : t(language, 'adminSignIn')}
        </button>
      </form>
    </AuthLayout>
  );
}
