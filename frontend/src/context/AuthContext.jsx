import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { fetchMe, loginRequest, registerRequest } from '../api/client';

const AuthContext = createContext(null);
const STORAGE_KEY = 'kigali-ecopark-auth';

function isUnauthorizedError(err) {
  const msg = String(err?.message || '');
  return (
    msg.includes('401')
    || /unauthorized/i.test(msg)
    || /invalid session/i.test(msg)
    || /missing authorization/i.test(msg)
  );
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  });
  const [loading, setLoading] = useState(!!user?.token);

  useEffect(() => {
    if (!user?.token) {
      setLoading(false);
      return;
    }

    let cancelled = false;
    fetchMe(user.token)
      .then((profile) => {
        if (cancelled) return;
        const next = { ...profile, token: profile.token || user.token };
        setUser(next);
        localStorage.setItem(STORAGE_KEY, JSON.stringify(next));
      })
      .catch((err) => {
        if (cancelled) return;
        // Keep the session on network/API sleep failures; only clear real auth errors.
        if (isUnauthorizedError(err)) {
          setUser(null);
          localStorage.removeItem(STORAGE_KEY);
        }
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, []);

  const login = async (email, password) => {
    const response = await loginRequest(email, password);
    setUser(response);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(response));
    return response;
  };

  const register = async (fullName, email, password) => {
    const response = await registerRequest(fullName, email, password);
    setUser(response);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(response));
    return response;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem(STORAGE_KEY);
  };

  const isAdmin = String(user?.role || '').toUpperCase() === 'ADMIN';

  const value = useMemo(() => ({
    user,
    loading,
    isAuthenticated: !!user?.token,
    isAdmin,
    login,
    register,
    logout,
  }), [user, loading, isAdmin]);

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}
